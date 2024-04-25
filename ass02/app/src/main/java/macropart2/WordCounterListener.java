package macropart2;

/**
 * A listener for word counting events.
 */
public interface WordCounterListener {
    
    /**
     * This method is called when a new word is counted.
     * @param url The URL of the web page.
     * @param count The number of occurrences of the word in the web page.
     */
    void onNewWordCounted(String url, int count);
}
