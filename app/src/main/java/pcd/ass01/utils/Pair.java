package pcd.ass01.utils;

public class Pair<K, V> {
    private K firstItem;
    private V secondItem;

    public Pair(K firstItem, V secondItem){
        this.firstItem = firstItem;
        this.secondItem = secondItem;
    }

    public K getFirst(){
        return this.firstItem;
    }

    public V getSecond(){
        return this.secondItem;
    }
}
