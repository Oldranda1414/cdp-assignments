package pcd.ass01.simengineconcurrent;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class RWBuffer<I> implements Buffer<I> {

    protected final Map<String, I> map = new TreeMap<String, I>();
    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock readLock = readWriteLock.readLock();
    private final Lock writeLock = readWriteLock.writeLock();


    @Override
    public I get(String key) {
        readLock.lock();
        try {
            return map.get(key);
        } finally {
            readLock.unlock();
        }
    }

    public String[] allKeys() {
        readLock.lock();
        try {
            return map.keySet().toArray(new String[0]);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public void put(String key, I value) {
        writeLock.lock();
        try {
            map.put(key, value);
        } finally {
            writeLock.unlock();
        }
    }

    public void clear() {
        writeLock.lock();
        try {
            map.clear();
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public boolean isEmpty() {
        readLock.lock();
        try {
            return this.map.isEmpty();
        } finally {
            readLock.unlock();
        }
    }

}
