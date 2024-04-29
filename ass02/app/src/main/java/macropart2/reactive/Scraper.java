package macropart2.reactive;

public interface Scraper extends Listened {
    void scrape();
    void scrape(final String url, final String word, final int depth);
    Results getResults();
}
