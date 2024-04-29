package macropart2.reactive;

import macropart2.WordCounterListener;

public interface Listened {
    void registerListener(WordCounterListener listener);
    void unregisterListener(WordCounterListener listener);
}
