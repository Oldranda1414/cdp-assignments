package utils;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class RWTreeMonitor<I> {

    private final Map<String, I> buffer = new SafeTreeMap<String, I>();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();

    public I get(String key) {
        readLock.lock();
        try {
            return buffer.get(key);
        } finally {
            readLock.unlock();
        }
    }

    public void put(String key, I value) {
        writeLock.lock();
        try {
            buffer.put(key, value);
        } finally {
            writeLock.unlock();
        }
    }
    
    public Collection<I> values(){
        readLock.lock();
        try {
            return buffer.values();
        } finally {
            readLock.unlock();
        }
    }
}
