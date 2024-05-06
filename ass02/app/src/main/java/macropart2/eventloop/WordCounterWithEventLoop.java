package macropart2.eventloop;

import java.util.HashMap;
import macropart2.AbstractWordCounter;
import macropart2.WordCounterListener;
import macropart2.utils.JSoupHandler;

public class WordCounterWithEventLoop extends AbstractWordCounter {

    private final boolean isLoggingEnabled;
    private final RunnableEventLoop eventLoop = new EventLoopImpl(this.sem, this.cond);
    
    public WordCounterWithEventLoop(final boolean isLoggingEnabled) {
        super(new HashMap<>());
        this.isLoggingEnabled = isLoggingEnabled;
    }
    
    public WordCounterWithEventLoop() {
        this(false);
    }

    @Override
    protected void startTemplate(final String url, final String word, final int depth) {
        enqueueOnEventLoop(eventLoop, url, word, depth);
        eventLoop.run();
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
        log("Exploring " + url, depth);
        super.wordOccurrences.put(url, JSoupHandler.findWordOccurrences(url, word));
        log("Found " + super.wordOccurrences.get(url) + " occurrences", depth);
        updateListeners(url);
        recursivelyGetWordOccurrences(url, word, depth, eventLoop);
    }

    private void updateListeners(final String url) {
        this.listeners.forEach(l -> l.onNewWordCounted(url, super.wordOccurrences.get(url)));
    }

    private boolean shouldExploreUrl(final String url, final int depth) {
        return depth != 0 && !super.wordOccurrences.containsKey(url);
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
