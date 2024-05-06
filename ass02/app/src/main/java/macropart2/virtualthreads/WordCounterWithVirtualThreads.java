package macropart2.virtualthreads;

import java.util.ArrayList;
import java.util.List;

import macropart2.AbstractWordCounter;
import macropart2.WordCounterListener;
import macropart2.utils.SimpleSemaphore;
import macropart2.virtualthreads.utils.RWTreeMonitor;

public class WordCounterWithVirtualThreads extends AbstractWordCounter{

    private Thread mainThread;
    private boolean isLoggingEnabled;
    private final List<WordCounterListener> listenerList = new ArrayList<>();
    private final SimpleSemaphore sem = new SimpleSemaphore();

    public WordCounterWithVirtualThreads (final boolean isLoggingEnabled){
        super(new RWTreeMonitor<>(false));
        this.isLoggingEnabled = isLoggingEnabled;
    }

    public WordCounterWithVirtualThreads (){
        this(false);
    }

    @Override
    protected void startTemplate(String url, String word, int depth) {
        this.mainThread = Thread.ofVirtual().start(new MyTask(url, word, depth, super.wordOccurrences, this.listenerList, this.sem, this.isLoggingEnabled));

        Thread.ofVirtual().start(() -> {
            try {
                this.mainThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.cond.signalAll();
        });
    }

    @Override
    public void addListener(WordCounterListener listener) {
        this.listenerList.add(listener);
    }
}
