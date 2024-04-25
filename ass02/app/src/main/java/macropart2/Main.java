package macropart2;

import java.util.Map;

import macropart2.eventloop.WordCounterWithEventLoop;

public class Main {

    public static void main(String[] args) {
        var link = "https://www.google.com";
        var word = "hello";
        var depth = 3;
        var wordCounter = new WordCounterWithEventLoop(true);
        var occurrences = wordCounter.getWordOccurrences(link, word, depth);
        logOccurrences(word, occurrences);

        //TODO: Implement the GUI
    }

    private static void logOccurrences(final String word, Map<String, Integer> wordOccurrences) {
        System.out.println("\nOccurrences:");
        for (var entry : wordOccurrences.entrySet()) {
            System.out.println("In " + entry.getKey() + " were found " + entry.getValue() + " occurrences of the word " + word);
        }
        System.out.println("Total occurrences: " + wordOccurrences.values().stream().mapToInt(Integer::intValue).sum());
    }
}
