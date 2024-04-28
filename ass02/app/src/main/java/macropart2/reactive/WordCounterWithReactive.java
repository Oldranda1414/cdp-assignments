package macropart2.reactive;

import java.util.Map;

import macropart2.WordCounter;
import macropart2.WordCounterListener;

public class WordCounterWithReactive implements WordCounter{

    @Override
    public Map<String, Integer> getWordOccurrences() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getWordOccurrences'");
    }

    @Override
    public void start(String url, String word, int depth) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'start'");
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'join'");
    }

    @Override
    public void addListener(WordCounterListener listener) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addListener'");
    }
    
}
