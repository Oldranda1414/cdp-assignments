package macropart2.reactive;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;

import macropart2.WordCounter;
import macropart2.WordCounterListener;

public class WordCounterWithReactive implements WordCounter {
    private AtomicInteger counter = new AtomicInteger(0);
    private AtomicBoolean paused = new AtomicBoolean(false);
    private Scraper scraper = new SecondScraperImpl(counter, paused);

    @Override
    public Map<String, Integer> getWordOccurrences() {
        return this.scraper.getResults();
    }

    @Override
    public void start(String url, String word, int depth) {
        this.scraper.scrape(url, word, depth);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void pause() {
        this.paused.set(true);
    }

    @Override
    public boolean isPaused() {
        return this.paused.get();
    }

    @Override
    public void resume() {
        this.paused.set(false);
    }

    // @Override
    // public void join() {
    //     while (counter.get() > 0) {
    //         Thread.onSpinWait();
    //     }
    // }

    @Override
    public void addListener(WordCounterListener listener) {
        this.scraper.registerListener(listener);
    }

    @Override
    public Condition getFinishedCondition() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'join'");
    }
    
}
