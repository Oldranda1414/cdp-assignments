package macropart2.reactiveprogramming;

public interface Listened {
    void registerListener(Listener listener);
    void unregisterListener(Listener listener);
}
