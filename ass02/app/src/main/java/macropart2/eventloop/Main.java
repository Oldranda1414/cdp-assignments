package macropart2.eventloop;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Main {
    
    private static final Map<String, Integer> wordOccurrences = new HashMap<>();

    public static void main(String[] args) {
        RunnableEventLoop eventLoop = new EventLoopImpl();
        var link = "http://192.168.1.152/Filo/test1.php";
        var word = "hello";
        var depth = 3;
        enqueueOnEventLoop(eventLoop, link, word, depth);
        eventLoop.run();
        logOccurrences(word);
    }

    private static void getWordOccurrences(final String url, final String word, final int depth, final EventLoop eventLoop) throws IOException {
            if (depth == 0 || wordOccurrences.containsKey(url)) return;
            var connection = Jsoup.connect(url);
            try {
                connection.execute();
            } catch (Exception e) {
                log("Skipping " + url, depth);
                return;
            }
            wordOccurrences.put(url, 0);
            var doc = connection.get();

        searchWord(doc, word, url, eventLoop, depth);

        recursivelyGetWordOccurrences(doc, word, depth, eventLoop);
    }

    private static void searchWord(final Document doc, final String word, final String url, final EventLoop eventLoop, final int depth) {
        log("Searching in " + url, depth);
        if (doc.body().text().toLowerCase().contains(word.toLowerCase())) {
            wordOccurrences.put(url, wordOccurrences.getOrDefault(url, 0) + 1);
        }
    }
    
    private static void recursivelyGetWordOccurrences(final Document doc, final String word, final int depth, final EventLoop eventLoop) {
        var links = doc.select("a");
        var newDepth = depth - 1;
        if (newDepth == 0) return;
        for (var link : links) {
            var href = link.attr("href");
            enqueueOnEventLoop(eventLoop, href, word, newDepth);
        }
    }

    private static void enqueueOnEventLoop(final EventLoop eventLoop, final String url, final String word, final int depth) {
        eventLoop.enqueueTask(() -> {
            try {
                getWordOccurrences(url, word, depth, eventLoop);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private static void log(final String message, int depth) {
        System.out.println(depth + " - [TASK]: " + message);
    }

    private static void logOccurrences(final String word) {
        System.out.println("\nOccurrences:");
        for (var entry : wordOccurrences.entrySet()) {
            System.out.println("In " + entry.getKey() + " were found " + entry.getValue() + " occurrences of the word " + word);
        }
        System.out.println("Total occurrences: " + wordOccurrences.values().stream().mapToInt(Integer::intValue).sum());
    }
}
