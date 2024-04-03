package pcd.ass01.utils;

import java.util.Optional;

public interface Buffer<I> {

    public void put(I item) throws InterruptedException;
    
    public Optional<I> get() throws InterruptedException;
    
    public boolean isEmpty();
}
