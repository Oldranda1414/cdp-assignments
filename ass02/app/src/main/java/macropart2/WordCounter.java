package macropart2;

/**
 * A word counter that can count the occurrences of a word in a web page.
 */
public interface WordCounter {

    /**
     * Get the number of occurrences of a word in a web page.
     * @param url The URL of the web page.
     * @param word The word to search for.
     * @param depth The depth of the search.
     * @return The number of occurrences of the word in the web page.
     * @throws Exception If an error occurs while fetching the web page.
     */
    int getWordOccurrences(final String url, final String word, final int depth) throws Exception;
}
