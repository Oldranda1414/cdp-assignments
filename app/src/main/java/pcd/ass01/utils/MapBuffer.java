package pcd.ass01.utils;

public interface MapBuffer<I> {

    public void put(String key, I item) throws InterruptedException;
    
    public I get(String key) throws InterruptedException;
    
    public boolean isEmpty();
}
