package macropart2.reactiveprogramming;

public interface Scraper extends Listened {
    void scrape();
    Results getResults();
}
