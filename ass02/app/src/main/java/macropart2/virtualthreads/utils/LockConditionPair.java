package macropart2.virtualthreads.utils;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class LockConditionPair {
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private boolean conditionMet = true;

    public ReentrantLock getLock() {
        return this.lock;
    }

    public Condition getCond() {
        return this.condition;
    }

    public void waitForCondition() throws InterruptedException {
        lock.lock();
        try {
            while (!conditionMet) {
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
            this.conditionMet = false;
        } finally {
            lock.unlock();
        }
    }

    public void notifyCondition() {
        lock.lock();
        try {
            this.conditionMet = true;
            condition.signalAll(); // Notify all waiting threads that condition is met
        } finally {
            lock.unlock();
        }
    }
}
