package macropart2.eventloop;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import macropart2.WordCounter;
import macropart2.WordCounterListener;

public class WordCounterWithEventLoop implements WordCounter {

    private final Map<String, Integer> wordOccurrences = new HashMap<>();
    private final boolean isLoggingEnabled;
    private final RunnableEventLoop eventLoop = new EventLoopImpl();
    private final List<WordCounterListener> listeners = new ArrayList<>();

    public WordCounterWithEventLoop(final boolean isLoggingEnabled) {
        this.isLoggingEnabled = isLoggingEnabled;
    }
    
    public WordCounterWithEventLoop() {
        this(false);
    }

    @Override
    public Map<String, Integer> getWordOccurrences() {
        return new HashMap<>(this.wordOccurrences);
    }

    @Override
    public void start(final String url, final String word, final int depth) {
        enqueueOnEventLoop(eventLoop, url, word, depth);
        eventLoop.run();
    }

    @Override
    public void pause() {
        eventLoop.stop();
    }

    @Override
    public void resume() {
        eventLoop.resume();
    }

    @Override
    public boolean isPaused() {
        return eventLoop.isStopped();
    }

    @Override
    public void join() {
        while (!eventLoop.isFinished()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void addListener(final WordCounterListener listener) {
        listeners.add(listener);
    }
    
    private void enqueueOnEventLoop(final EventLoop eventLoop, final String url, final String word, final int depth) {
        eventLoop.enqueueTask(() -> getWordOccurrencesWithEventLoop(eventLoop, url, word, depth));
    }

    private void getWordOccurrencesWithEventLoop(final EventLoop eventLoop, final String url, final String word, final int depth) {
        if (!shouldExploreUrl(url, depth)) return;
        this.wordOccurrences.put(url, 0);
        try {
            Document doc = Jsoup.connect(url).get();
            searchWord(doc, word, eventLoop, depth);
            recursivelyGetWordOccurrences(doc, word, depth, eventLoop);
        } catch (IOException e) {
            //this catch should never be reached since I already checked if the url is reachable
            e.printStackTrace();
        }
    }

    private boolean shouldExploreUrl(final String url, final int depth) {
        if (depth == 0 || wordOccurrences.containsKey(url)) return false;
        try {
            var connection = Jsoup.connect(url);
            connection.execute();
        } catch (Exception e) {
            log("Skipping " + url, depth);
            return false;
        }
        return true;
    }

    private void searchWord(final Document doc, final String word, final EventLoop eventLoop, final int depth) {
        log("Searching in " + doc.baseUri(), depth);
        var body = doc.body().text().toLowerCase();
        int count = 0;
        int index = body.indexOf(word.toLowerCase());
        while (index != -1) {
            count++;
            index = body.indexOf(word.toLowerCase(), index + 1);
        }
        final int finalCount = count;
        this.listeners.forEach(l -> l.onNewWordCounted(doc.baseUri(), finalCount));
    }

    private void recursivelyGetWordOccurrences(final Document doc, final String word, final int depth, final EventLoop eventLoop) {
        var links = doc.select("a");
        var newDepth = depth - 1;
        if (newDepth == 0) return;
        for (var link : links) {
            var href = link.attr("href");
            enqueueOnEventLoop(eventLoop, href, word, newDepth);
        }
    }

    private void log(final String message, final int depth) {
        if (this.isLoggingEnabled) {
            System.out.println("[EVENT-LOOP | " + depth + "]: " + message);
        }
    }
}
