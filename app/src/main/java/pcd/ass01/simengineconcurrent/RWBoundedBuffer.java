package pcd.ass01.simengineconcurrent;

public abstract class RWBoundedBuffer<I> implements BoundedBuffer<I> {

    

    @Override
    public void put(I item) throws InterruptedException {
    }

    @Override
    public I get() throws InterruptedException {
    }
    
}
