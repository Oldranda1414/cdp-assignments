package macropart2.eventloop;

import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import macropart2.WordCounter;

public class WordCounterWithEventLoop implements WordCounter {

    private final Map<String, Integer> wordOccurrences = new HashMap<>();
    private final PrintStream outputStream;
    private final RunnableEventLoop eventLoop = new EventLoopImpl();
    private boolean isStarted = false;

    public WordCounterWithEventLoop(final PrintStream outputStream, final String url, final String word, final int depth) {
        this.outputStream = outputStream;
        enqueueOnEventLoop(eventLoop, url, word, depth);
    }
    
    public WordCounterWithEventLoop(final String url, final String word, final int depth) {
        this(System.out, url, word, depth);
    }

    /**
     * This method returns the number of occurrences of a word in a given URL and its children URLs using an event-loop based approach.
     */
    @Override
    public Map<String, Integer> getWordOccurrences() {
        return new HashMap<>(wordOccurrences);
    }

    @Override
    public void pause() {
        eventLoop.stop();
    }

    @Override
    public void resume() {
        if (!isStarted) {
            isStarted = true;
            eventLoop.run();
        } else {
            eventLoop.resume();
        }
    }

    @Override
    public boolean isPaused() {
        return eventLoop.isStopped();
    }

    public void join() {
        while (!eventLoop.isFinished()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void enqueueOnEventLoop(final EventLoop eventLoop, final String url, final String word, final int depth) {
        eventLoop.enqueueTask(() -> getWordOccurrencesWithEventLoop(eventLoop, url, word, depth));
    }

    private void getWordOccurrencesWithEventLoop(final EventLoop eventLoop, final String url, final String word, final int depth) {
        if (!shouldExploreUrl(url, depth)) return;
        wordOccurrences.put(url, 0);
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
        if (doc.body().text().toLowerCase().contains(word.toLowerCase())) {
            wordOccurrences.put(doc.baseUri(), wordOccurrences.getOrDefault(doc.baseUri(), 0) + 1);
        }
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
        this.outputStream.println("[EVENT-LOOP | " + depth + "]: " + message);
    }
}
