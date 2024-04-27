package macropart2.virtualthreads.utils;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class RWTreeMonitor<K, V> implements Map<K, V>{

    private final Map<K, V> buffer = new SafeTreeMap<K, V>();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();
    private boolean isLogger;

    public RWTreeMonitor(boolean isLogger){
        this.isLogger = isLogger;
    }

    @Override
    public V get(Object key) {
        readLock.lock();
        try {
            return buffer.get(key);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public V put(K key, V value) {
        writeLock.lock();
        try {
            if(this.isLogger) log("putting: " + key + ", " + value);
            buffer.put(key, value);
            return value;
        } finally {
            writeLock.unlock();
        }
    }
    
    @Override
    public Collection<V> values(){
        readLock.lock();
        try {
            return buffer.values();
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public int size() {
        readLock.lock();
        try {
            return buffer.size();
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public boolean isEmpty() {
        readLock.lock();
        try {
            return buffer.isEmpty();
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public boolean containsKey(Object key) {
        readLock.lock();
        try {
            return buffer.containsKey(key);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public boolean containsValue(Object value) {
        readLock.lock();
        try {
            return buffer.containsValue(value);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public V remove(Object key) {
        writeLock.lock();
        try {
            return buffer.remove(key);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        writeLock.lock();
        try {
            buffer.putAll(m);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void clear() {
        writeLock.lock();
        try {
            buffer.clear();
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public Set<K> keySet() {
        readLock.lock();
        try {
            return buffer.keySet();
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        readLock.lock();
        try {
            return buffer.entrySet();
        } finally {
            readLock.unlock();
        }
    }

	@SuppressWarnings("unused")
    private static void log(String msg) {
		System.out.println("[ map ] " + msg);
	}
}
