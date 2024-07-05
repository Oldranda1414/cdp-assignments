package wordcounter;
import java.util.Map;
import java.util.concurrent.locks.Condition;

/**
 * A word counter that can count the occurrences of a word in a web page.
 */
public interface WordCounter {

    /**
     * Get the number of occurrences of a word in a web page and its links.
     * @param url The URL of the web page.
     * @param word The word to search for.
     * @param depth The depth of the search.
     * @return The number of occurrences of the word in the web page and its links.
     */
    Map<String, Integer> getWordOccurrences();

    /**
     * Start the word counter.
     * @param url The URL of the web page.
     * @param word The word to search for.
     * @param depth The depth of the search.
     */
    void start(String url, String word, int depth);

    /**
     * Pause the word counter.
     */
    void pause();

    /**
     * True if the word counter is paused, false otherwise.
     * @return True if the word counter is paused, false otherwise.
     */
    boolean isPaused();

    /**
     * Resume the word counter.
     */
    void resume();

    /**
     * Join the word counter.
     */
    Condition getFinishedCondition();

    /**
     * Add a listener to the word counter.
     * @param listener The listener to add.
     */
    void addListener(WordCounterListener listener);
}
