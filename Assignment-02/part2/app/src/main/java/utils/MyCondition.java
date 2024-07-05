package utils;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MyCondition implements Condition{
    final private ReentrantLock lock = new ReentrantLock();
    final private Condition cond = lock.newCondition();

    @Override
    public void signalAll(){
        this.lock.lock();
        try{
            this.cond.signalAll();
        }
        finally{
            lock.unlock();
        }
    }

    @Override
    public void signal(){
        this.lock.lock();
        try{
            this.cond.signal();
        }
        finally{
            lock.unlock();
        }
    }

    @Override
    public void await() throws InterruptedException {
        this.lock.lock();
        try{
            this.cond.await();
        }
        finally{
            lock.unlock();
        }
    }

    @Override
    public void awaitUninterruptibly() {
        this.lock.lock();
        try{
            this.cond.awaitUninterruptibly();
        }
        finally{
            lock.unlock();
        }
    }

    @Override
    public long awaitNanos(long nanosTimeout) throws InterruptedException {
        this.lock.lock();
        try{
            return this.cond.awaitNanos(nanosTimeout);
        }
        finally{
            lock.unlock();
        }
    }

    @Override
    public boolean await(long time, TimeUnit unit) throws InterruptedException {
        this.lock.lock();
        try{
            return this.cond.await(time, unit);
        }
        finally{
            lock.unlock();
        }
    }

    @Override
    public boolean awaitUntil(Date deadline) throws InterruptedException {
        this.lock.lock();
        try{
            return this.cond.awaitUntil(deadline);
        }
        finally{
            lock.unlock();
        }
    }
}
