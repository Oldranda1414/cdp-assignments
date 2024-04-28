package macropart2;

import java.util.Map;

import macropart2.View.GUI;
import macropart2.eventloop.WordCounterWithEventLoop;
import macropart2.virtualthreads.WordCounterWithVirtualThreads;
import macropart2.reactive.WordCounterWithReactive;

@SuppressWarnings("unused")
public class Main {

    public static void main(String[] args) {
        //var wordCounter = new WordCounterWithEventLoop();
        //var wordCounter = new WordCounterWithReactive();
        var wordCounter = new WordCounterWithVirtualThreads();
        new GUI(wordCounter).display();
    }

    @SuppressWarnings("unused")
    private static void logOccurrences(final String word, Map<String, Integer> wordOccurrences) {
        System.out.println("\nOccurrences:");
        for (var entry : wordOccurrences.entrySet()) {
            System.out.println("In " + entry.getKey() + " were found " + entry.getValue() + " occurrences of the word " + word);
        }
        System.out.println("Total occurrences: " + wordOccurrences.values().stream().mapToInt(Integer::intValue).sum());
    }
}
