package pcd.ass01.utils;

import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
// import java.util.concurrent.locks.Lock;
// import java.util.concurrent.locks.ReentrantReadWriteLock;

public class RWBuffer<I> implements MapBuffer<I> {

    protected final Map<String, I> map = new SafeTreeMap<String, I>();
    // private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    // private final Lock readLock = readWriteLock.readLock();
    // private final Lock writeLock = readWriteLock.writeLock();
    private final Object lock = new Object();


    @Override
    public I get(String key) {
        // readLock.lock();
        // try {
        //     return map.get(key);
        // } finally {
        //     readLock.unlock();
        // }
        synchronized(lock) {
            return map.get(key);
        }
    }

    public String[] allKeys() {
        // readLock.lock();
        // try {
        //     return map.keySet().toArray(new String[0]);
        // } finally {
        //     readLock.unlock();
        // }
        synchronized(lock) {
            return map.keySet().toArray(new String[0]);
        }
    }

    public Set<Entry<String, I>> entrySet() {
        // readLock.lock();
        // try {
        //     return map.entrySet();
        // } finally {
        //     readLock.unlock();
        // }
        synchronized(lock) {
            return map.entrySet();
        }
    }

    @Override
    public void put(String key, I value) {
        // writeLock.lock();
        // try {
        //     map.put(key, value);
        // } finally {
        //     writeLock.unlock();
        // }
        synchronized(lock) {
            map.put(key, value);
        }
    }

    public void clear() {
        // writeLock.lock();
        // try {
        //     map.clear();
        // } finally {
        //     writeLock.unlock();
        // }
        synchronized(lock) {
            map.clear();
        }
    }

    @Override
    public boolean isEmpty() {
        // readLock.lock();
        // try {
        //     return this.map.isEmpty();
        // } finally {
        //     readLock.unlock();
        // }
        synchronized (lock) {
            return this.map.isEmpty();
        }
    }

}
