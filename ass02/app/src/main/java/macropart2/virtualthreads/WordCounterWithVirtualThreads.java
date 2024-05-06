package macropart2.virtualthreads;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import macropart2.AbstractWordCounter;
import macropart2.WordCounterListener;
import macropart2.utils.SimpleSemaphore;
import macropart2.virtualthreads.utils.RWTreeMonitor;

public class WordCounterWithVirtualThreads extends AbstractWordCounter{

    private final Map<String, Integer> wordOccurrences = new RWTreeMonitor<String, Integer>(false);
    private Thread mainThread;
    private boolean isLoggingEnabled;
    private final List<WordCounterListener> listenerList = new ArrayList<>();
    private final SimpleSemaphore sem = new SimpleSemaphore();

    public WordCounterWithVirtualThreads (final boolean isLoggingEnabled){
        this.isLoggingEnabled = isLoggingEnabled;
    }

    public WordCounterWithVirtualThreads (){
        this.isLoggingEnabled = false;
    }

    @Override
    public Map<String, Integer> getWordOccurrences() {
        return wordOccurrences;
    }

    @Override
    protected void startTemplate(String url, String word, int depth) {
        this.mainThread = Thread.ofVirtual().start(new MyTask(url, word, depth, new RWTreeMonitor<>(this.isLoggingEnabled), this.listenerList, this.sem, this.isLoggingEnabled));

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
    
	@SuppressWarnings("unused")
    private void log(String msg) {
        if(this.isLoggingEnabled){
		    System.out.println("[ word counter ] " + msg);
        }
	}
}
