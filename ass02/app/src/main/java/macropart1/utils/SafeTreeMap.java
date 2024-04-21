package macropart1.utils;

import java.util.Comparator;
import java.util.TreeMap;

public class SafeTreeMap<K, V> extends TreeMap<K, V> {
    
    public SafeTreeMap() {
        super();
    }

    public SafeTreeMap(Comparator<? super K> comparator) {
        super(comparator);
    }

    // Override the get method to throw an exception if null is returned
    @Override
    public V get(Object key) {
        V value = super.get(key);
        if (value == null) {
            throw new NullPointerException("Value associated with key '" + key + "' is null");
        }
        return value;
    }
}