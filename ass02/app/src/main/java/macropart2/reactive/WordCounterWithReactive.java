package macropart2.reactive;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;

import macropart2.AbstractWordCounter;
import macropart2.WordCounter;
import macropart2.WordCounterListener;

public class WordCounterWithReactive extends AbstractWordCounter {
    private AtomicInteger counter = new AtomicInteger(0);
    private AtomicBoolean paused = new AtomicBoolean(false);
    private Scraper scraper = new SecondScraperImpl(counter, paused);

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
