package macropart2.eventloop;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

import macropart2.JSoupHandler;
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
        eventLoop.enqueueTask(() -> {System.out.println(url); getWordOccurrencesWithEventLoop(eventLoop, url, word, depth);});
    }

    private void getWordOccurrencesWithEventLoop(final EventLoop eventLoop, final String url, final String word, final int depth) {
        if (!shouldExploreUrl(url, depth)) return; 
        log("Exploring " + url, depth);
        this.wordOccurrences.put(url, JSoupHandler.findWordOccurrences(url, word));
        log("Found " + this.wordOccurrences.get(url) + " occurrences", depth);
        updateListeners(url);
        recursivelyGetWordOccurrences(url, word, depth, eventLoop);
    }

    private void updateListeners(final String url) {
        this.listeners.forEach(l -> l.onNewWordCounted(url, this.wordOccurrences.get(url)));
    }

    private boolean shouldExploreUrl(final String url, final int depth) {
        return depth != 0 && !wordOccurrences.containsKey(url);
    }

    private void recursivelyGetWordOccurrences(final String url, final String word, final int depth, final EventLoop eventLoop) {
        var links = JSoupHandler.getLinksFromUrl(url);
        var newDepth = depth - 1;
        for (var link : links) {
            enqueueOnEventLoop(eventLoop, link, word, newDepth);
        }
    }

    private void log(final String message, final int depth) {
        if (this.isLoggingEnabled) {
            System.out.println("[EVENT-LOOP | " + depth + "]: " + message);
        }
    }
}
