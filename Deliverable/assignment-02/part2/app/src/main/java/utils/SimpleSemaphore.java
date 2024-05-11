package utils;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class SimpleSemaphore {
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private boolean isGreen = true;

    public boolean isRed(){
        lock.lock();
        try{
            return !this.isGreen;
        } finally {
            lock.unlock();
        }
    }

    public void waitForGreen() throws InterruptedException {
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

    public void setToRed(){
        lock.lock();
        try {
            if(!this.isGreen){
                throw new IllegalStateException("setToGreen was called when semaphore was already green");
            }
            this.isGreen = false;
        } finally {
            lock.unlock();
        }
    }

    public void setToGreen() {
        lock.lock();
        try {
            if(this.isGreen){
                throw new IllegalStateException("setToGreen was called when semaphore was already green");
            }
            this.isGreen = true;
            condition.signalAll(); // Notify all waiting threads that condition is met
        } finally {
            lock.unlock();
        }
    }
}
