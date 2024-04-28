package macropart2.virtualthreads.utils;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class LockConditionPair {
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private boolean isGreen = true;

    public ReentrantLock getLock() {
        lock.lock();
        try{
            return this.lock;
        } finally {
            lock.unlock();
        }
    }

    public Condition getCond() {
        lock.lock();
        try{
            return this.condition;
        } finally {
            lock.unlock();
        }
    }

    public boolean isPaused(){
        lock.lock();
        try{
            return !this.isGreen;
        } finally {
            lock.unlock();
        }
    }

    public void waitForCondition() throws InterruptedException {
        lock.lock();
        try {
            while (!isGreen) {
                condition.await(); // Thread waits here until condition is met
            }
            // if condition is met, threads may continue
        } finally {
            lock.unlock();
        }
    }

    public void pauseThreads(){
        lock.lock();
        try {
            this.isGreen = false;
        } finally {
            lock.unlock();
        }
    }

    public void notifyCondition() {
        lock.lock();
        try {
            this.isGreen = true;
            condition.signalAll(); // Notify all waiting threads that condition is met
        } finally {
            lock.unlock();
        }
    }
}
