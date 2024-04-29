package macropart2.reactiveprogramming;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SecondScraperImpl implements Scraper {
    private final String initialUrl;
    private final int maxDepth;
    private final String wordToFind;
    private final Set<Listener> listeners;
    private final Set<String> visitedUrls;
    private Results results;
    private AtomicInteger counter;

    public SecondScraperImpl(
        String initialUrl,
        int maxDepth,
        String wordToFind,
        AtomicInteger counter
    ) {
        this.initialUrl = initialUrl;
        this.maxDepth = maxDepth;
        this.wordToFind = wordToFind;
        this.counter = counter;
        this.listeners = new HashSet<>();
        this.visitedUrls = new HashSet<>();
        this.results = new Results();
    }

    @Override
    public void registerListener(Listener listener) {
        this.listeners.add(listener);
    }

    @Override
    public void unregisterListener(Listener listener) {
        if (this.listeners.contains(listener)) {
            this.listeners.remove(listener);
        }
    }

    private void updateListeners() {
        this.listeners.forEach(t -> t.onUpdate(this.results));
    }

    private void createNewObserver(final String url, final int depth) {
        Observable.create(emitter -> {
            // new Thread(() -> {
                log("Scraping url " + url + " on depth " + depth);

                // Getting links in current page
                var links = getLinksFromUrl(url);

                // Notifying observer (pasing each link found)
                links.forEach(emitter::onNext);

                // Searching word in current page
                int occurrencies = JSoupHandler.findWordOccurrences(url, this.wordToFind);
                this.results.addOccurrencies(occurrencies);
                log(url + ": " + occurrencies + " occurrencies found. Total: " + this.results.getOccurrencies());
                this.updateListeners();

                // Completing action
                emitter.onComplete();
            // }).start();
        }).subscribeOn(Schedulers.io())
        .subscribe(subUrl -> {
            if (depth + 1 > this.maxDepth) {
                log("Reached max depth, stopping");
            } else {
                if (this.visitedUrls.contains(subUrl)) {
                    log("Encountered url that has been already visited");
                } else {
                    this.visitedUrls.add((String) subUrl);
                    createNewObserver((String) subUrl, depth + 1);
                }
            }
        });
    }

    private List<String> getLinksFromUrl(final String url) {
        return JSoupHandler.getLinksFromUrl(url);
    }

    private void log(final String s) {
        // System.out.println("[" + Thread.currentThread().getName() + "] " + s);
    }

    @Override
    public void scrape() {
        this.createNewObserver(this.initialUrl, 0);
    }

    @Override
    public Results getResults() {
        return this.results;
    }

}
