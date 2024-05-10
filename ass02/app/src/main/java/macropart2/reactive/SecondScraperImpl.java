package macropart2.reactive;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import macropart2.WordCounterListener;
import macropart2.utils.JSoupHandler;
import macropart2.utils.SimpleSemaphore;

public class SecondScraperImpl implements Scraper {
    private String initialUrl;
    private int maxDepth;
    private String wordToFind;
    private final Set<WordCounterListener> listeners;
    private final Set<String> visitedUrls;
    private AtomicInteger counter;
    private Map<String, Integer> results;
    private SimpleSemaphore sem;

    public SecondScraperImpl(
        String initialUrl,
        int maxDepth,
        String wordToFind,
        AtomicInteger threadsCounter,
        SimpleSemaphore sem
    ) {
        this(threadsCounter, sem);
        this.initialUrl = initialUrl;
        this.maxDepth = maxDepth;
        this.wordToFind = wordToFind;
        this.counter = threadsCounter;
    }

    public SecondScraperImpl(
        AtomicInteger counter,
        SimpleSemaphore sem
    ) {
        this.counter = counter;
        this.sem = sem;
        this.listeners = new HashSet<>();
        this.visitedUrls = new ConcurrentSkipListSet<>();
        this.results = new ConcurrentHashMap<>();
    }

    @Override
    public void registerListener(WordCounterListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public void unregisterListener(WordCounterListener listener) {
        if (this.listeners.contains(listener)) {
            this.listeners.remove(listener);
        }
    }

    private void updateListeners(
        final String currentUrl,
        final int currentOccurrencies
    ) {
        this.listeners.forEach(t ->
            t.onNewWordCounted(currentUrl, currentOccurrencies)
        );
    }

    private void createNewObserver(final String url, final int depth) {
        Observable.create(emitter -> {
                this.counter.incrementAndGet();
                this.pauseIfPaused();
                log("Scraping url " + url + " on depth " + depth);

                // Getting links in current page
                var links = this.getLinksFromUrl(url);

                // Notifying observer (pasing each link found)
                links.forEach(emitter::onNext);

                // Searching word in current page
                int currentOccurrencies = this
                    .findWordOccurrences(url);

                // Updating results
                this.pauseIfPaused();
                this.results.put(url, currentOccurrencies);
                int totalOccurrencies = this.results.values().stream()
                    .reduce(0, Integer::sum);
                log(url + ": "
                    + currentOccurrencies + " occurrencies found. "
                    + "Total: " + totalOccurrencies
                );
                this.updateListeners(url, currentOccurrencies);

                // Completing action
                emitter.onComplete();
                this.counter.decrementAndGet();
            }).subscribeOn(Schedulers.io())
            .subscribe(subUrl -> {
                if (depth + 1 > this.maxDepth - 1) {
                    log("Reached max depth, stopping");
                } else {
                    if (this.visitedUrls.contains(subUrl)) {
                        log("Encountered url that has been already visited");
                    } else {
                        this.visitedUrls.add((String) subUrl);
                        createNewObserver((String) subUrl, depth + 1);
                    }
                }
            }
        );
    }


    private void pauseIfPaused() {
        // while (this.paused.get()) { Thread.onSpinWait(); }
        try {
            this.sem.waitForGreen();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private List<String> getLinksFromUrl(final String url) {
        return JSoupHandler.getLinksFromUrl(url);
    }

    private int findWordOccurrences(final String url) {
        return JSoupHandler.findWordOccurrences(url, this.wordToFind);
    }

    private void log(final String s) {
        boolean logging = false;
        if (logging) {
            System.out.println(
                "[" + Thread.currentThread().getName() + "] "
                + s
            );
        }
    }

    @Override
    public void scrape() {
        if (this.initialUrl == "" || this.wordToFind == "") {
            throw new IllegalStateException("Parameters not set");
        }
        this.createNewObserver(this.initialUrl, 0);
    }

    @Override
    public void scrape(String url, String word, int depth) {
        this.initialUrl = url;
        this.wordToFind = word;
        this.maxDepth = depth;
        this.scrape();
    }

    @Override
    public Map<String, Integer> getResults() {
        return this.results;
    }

}
