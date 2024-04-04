package pcd.ass01.utils;


public interface Buffer<I> {

    public void put(I item) throws InterruptedException;
    
    public I get(String id) throws InterruptedException;
    
    public boolean isEmpty();
}
