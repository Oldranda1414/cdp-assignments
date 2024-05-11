package wordcounter;
import java.util.concurrent.locks.Condition;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import utils.MyCondition;
import utils.SimpleSemaphore;

public abstract class AbstractWordCounter implements WordCounter {

    protected final SimpleSemaphore sem = new SimpleSemaphore();
    protected final MyCondition cond = new MyCondition();
    protected final List<WordCounterListener> listeners = new ArrayList<>();
    protected final Map<String, Integer> wordOccurrences;
    private boolean isStarted = false;

    protected AbstractWordCounter(final Map<String, Integer> wordOccurrences) {
        this.wordOccurrences = wordOccurrences;
    }

    @Override
    public void start(String url, String word, int depth){
        this.isStarted = true;
        this.startTemplate(url, word, depth);
    }

    @Override
    public Map<String, Integer> getWordOccurrences() {
        return new HashMap<>(this.wordOccurrences);
    }

    @Override
    public void pause() {
        checkStarted();
        this.sem.setToRed();
    }

    @Override
    public boolean isPaused() {
        return this.sem.isRed();
    }

    @Override
    public void resume() {
        checkStarted();
        this.sem.setToGreen();
    }

    @Override
    public Condition getFinishedCondition() {
        return this.cond;
    }

    @Override
    public void addListener(WordCounterListener listener) {
        this.listeners.add(listener);
    }

    private void checkStarted() {
        if(!this.isStarted){
            throw new IllegalStateException("Cannot enter this state until WordCounter is started");
        }
    }

    abstract protected void startTemplate(String url, String word, int depth);

}
