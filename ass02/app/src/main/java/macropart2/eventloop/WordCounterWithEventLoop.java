package macropart2.eventloop;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import macropart2.WordCounter;

public class WordCounterWithEventLoop implements WordCounter {

    private final Map<String, Integer> wordOccurrences = new HashMap<>();
    private boolean isLogEnabled;

    public WordCounterWithEventLoop() {
        this.isLogEnabled = true;
    }

    public WordCounterWithEventLoop(final boolean isLogEnabled) {
        this.isLogEnabled = isLogEnabled;
    }

    /**
     * This method returns the number of occurrences of a word in a given URL and its children URLs using an event-loop based approach.
     */
    @Override
    public Map<String, Integer> getWordOccurrences(final String url, final String word, final int depth) {
        final RunnableEventLoop eventLoop = new EventLoopImpl();
        enqueueOnEventLoop(eventLoop, url, word, depth);
        eventLoop.run();
        return wordOccurrences;
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
        
        if (this.isLogEnabled) {
            System.out.println(depth + " - [TASK]: " + message);
        }
    }
}
