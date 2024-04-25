package macropart2;

import java.util.Map;

import macropart2.eventloop.WordCounterWithEventLoop;

public class Main {

    public static void main(String[] args) {
        var link = "https://www.google.com";
        var word = "hello";
        var depth = 3;
        var wordCounter = new WordCounterWithEventLoop(link, word, depth); //TODO use the PrintStream constructor to log the occurrences while the word counter is running
        //? we can actually pass 2 kinds of printStream, one for the logging and one for the results
        wordCounter.resume();
        wordCounter.join();
        var occurrences = wordCounter.getWordOccurrences();
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
