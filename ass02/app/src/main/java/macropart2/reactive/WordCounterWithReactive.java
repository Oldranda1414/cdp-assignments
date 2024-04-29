package macropart2.reactive;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import macropart2.WordCounter;
import macropart2.WordCounterListener;

public class WordCounterWithReactive implements WordCounter {
    private AtomicInteger counter = new AtomicInteger(0);
    private Scraper scraper = new SecondScraperImpl(counter);

    @Override
    public Map<String, Integer> getWordOccurrences() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getWordOccurrences'");
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'pause'");
    }

    @Override
    public boolean isPaused() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isPaused'");
    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'resume'");
    }

    @Override
    public void join() {
        while (counter.get() > 0) {}
    }

    @Override
    public void addListener(WordCounterListener listener) {
        this.scraper.registerListener(listener);
    }
    
}
