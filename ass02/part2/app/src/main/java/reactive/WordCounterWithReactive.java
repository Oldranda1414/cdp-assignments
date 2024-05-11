package reactive;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import wordcounter.AbstractWordCounter;
import wordcounter.WordCounterListener;

public class WordCounterWithReactive extends AbstractWordCounter {
    private AtomicInteger counter = new AtomicInteger(0);
    private Scraper scraper = new SecondScraperImpl(this.counter, this.sem);

    public WordCounterWithReactive(){
        super(null);
    }

    public Map<String, Integer> getWordOccurrences() {
        return this.scraper.getResults();
    }

    public void startTemplate(String url, String word, int depth) {
        this.scraper.scrape(url, word, depth);

        new Thread(() -> {
            while (this.counter.get() > 0) {
                Thread.onSpinWait();
            }
            this.cond.signalAll();
        }).start();
    }

    public void addListener(WordCounterListener listener) {
        this.scraper.registerListener(listener);
    }
}
