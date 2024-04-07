package pcd.ass01.utils;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class RWBuffer<I> implements MapBuffer<I> {

    private final Map<String, I> map = new SafeTreeMap<String, I>();
    private final Object lock = new Object();
    private int readers = 0;
    private int writers = 0;


    @Override
    public I get(String key){
        startRead();
        I value = map.get(key);
        endRead();
        return value;
    }

    public String[] allKeys(){
        startRead();
        String[] value = map.keySet().toArray(new String[0]);
        endRead();
        return value;
    }

    public Set<Entry<String, I>> entrySet(){
        startRead();
        Set<Entry<String, I>> set = map.entrySet();
        endRead();
        return set;
    }

    @Override
    public void put(String key, I value){
        startWrite();
        map.put(key, value);
        endWrite();
    }

    public void clear(){
        startWrite();
        map.clear();
        endWrite();
    }

    @Override
    public boolean isEmpty(){
        startRead();
        boolean value = this.map.isEmpty();
        endRead();
        return value;
    }

    public Collection<I> values() {
        return this.map.values();
    }

    private void startRead(){
        synchronized (lock) {
            while (writers > 0) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            readers++;
        }
    }

    private void endRead() {
        synchronized (lock) {
            readers--;
            if (readers == 0) {
                lock.notifyAll();
            }
        }
    }

    private void startWrite(){
        synchronized (lock) {
            while (readers > 0 || writers > 0) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            writers++;
        }
    }

    private void endWrite() {
        synchronized (lock) {
            writers--;
            lock.notifyAll();
        }
    }
}