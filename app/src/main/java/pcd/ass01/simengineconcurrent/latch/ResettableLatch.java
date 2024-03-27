package pcd.ass01.simengineconcurrent.latch;

public interface ResettableLatch {
    
    public void reset();

    public void await();

    public void countDown();

}
