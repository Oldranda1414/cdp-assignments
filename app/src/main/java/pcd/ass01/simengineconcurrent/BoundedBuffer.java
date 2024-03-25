package pcd.ass01.simengineconcurrent;

public interface BoundedBuffer<I> {

    void put(I item) throws InterruptedException;
    
    I get() throws InterruptedException;
    
}
