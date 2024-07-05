package reactive;

import java.util.Map;

import reactive.utils.Listened;

public interface Scraper extends Listened {
    void scrape();
    void scrape(final String url, final String word, final int depth);
    Map<String, Integer> getResults();
}
