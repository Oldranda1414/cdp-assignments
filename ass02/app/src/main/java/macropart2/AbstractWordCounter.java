package macropart2;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public abstract class AbstractWordCounter implements WordCounter{

    protected SimpleSemaphore sem = new SimpleSemaphore();
    protected Condition cond = new ReentrantLock().newCondition();
    private boolean isStarted = false;

    @Override
    public void start(String url, String word, int depth){
        this.isStarted = true;
        this.innerStart(url, word, depth);
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
    public Condition join() {
        return this.cond;
    }

    private void checkStarted(){
        if(!this.isStarted){
            throw new IllegalStateException("Cannot enter this state until WordCounter is started");
        }
    }

    abstract protected void innerStart(String url, String word, int depth);
}
