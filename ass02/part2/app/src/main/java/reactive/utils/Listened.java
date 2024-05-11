package reactive.utils;

import wordcounter.WordCounterListener;

public interface Listened {
    void registerListener(WordCounterListener listener);
    void unregisterListener(WordCounterListener listener);
}
