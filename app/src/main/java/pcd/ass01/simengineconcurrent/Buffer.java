package pcd.ass01.simengineconcurrent;

public interface Buffer<I> {

    public void put(I item) throws InterruptedException;
    
    public I get() throws InterruptedException;
    
    public boolean isEmpty();
}
