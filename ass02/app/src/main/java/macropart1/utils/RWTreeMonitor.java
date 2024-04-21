package utils;

import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class RWTreeMonitor<K, V> {

    private final Map<K, V> buffer = new SafeTreeMap<K, V>();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();

    public V read(K key) {
        readLock.lock();
        try {
            return buffer.get(key);
        } finally {
            readLock.unlock();
        }
    }

    public void write(K key, V value) {
        writeLock.lock();
        try {
            buffer.put(key, value);
        } finally {
            writeLock.unlock();
        }
    }
}
