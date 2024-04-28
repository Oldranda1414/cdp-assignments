package macropart2.virtualthreads;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

import macropart2.WordCounter;
import macropart2.WordCounterListener;
import macropart2.virtualthreads.utils.Flag;
import macropart2.virtualthreads.utils.LockConditionPair;
import macropart2.virtualthreads.utils.RWTreeMonitor;

public class WordCounterWithVirtualThreads implements WordCounter{

    private final Map<String, Integer> wordOccurrences = new RWTreeMonitor<String, Integer>(true);
    private Thread mainThread;
    private boolean isLoggingEnabled;
    private final List<WordCounterListener> listenerList = new ArrayList<>();
    private final Flag flag = new Flag(false);
    private final LockConditionPair lockConditionPair = new LockConditionPair();

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
    public void start(String url, String word, int depth) {
        this.mainThread = Thread.ofVirtual().start(new MyTask(url, word, depth, new RWTreeMonitor<>(this.isLoggingEnabled), this.listenerList, this.lockConditionPair, this.isLoggingEnabled));
    }

    @Override
    public void pause() {
        log("pausing threads");
        this.lockConditionPair.pauseThreads();
    }

    @Override
    public boolean isPaused() {
        return this.flag.get();
    }

    @Override
    public void resume() {
        log("resuming threads");
        this.lockConditionPair.notifyCondition();
    }

    @Override
    public void join() {
        try {
            this.mainThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
