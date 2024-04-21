package macropart1.utils;

import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class RWTreeMonitor<I> {

    private final Map<String, I> buffer = new SafeTreeMap<String, I>();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();

    public I read(String key) {
        readLock.lock();
        try {
            return buffer.get(key);
        } finally {
            readLock.unlock();
        }
    }

    public void write(String key, I value) {
        writeLock.lock();
        try {
            buffer.put(key, value);
        } finally {
            writeLock.unlock();
        }
    }
}
