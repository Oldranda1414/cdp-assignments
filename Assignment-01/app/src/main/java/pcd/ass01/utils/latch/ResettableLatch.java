package pcd.ass01.utils.latch;

public interface ResettableLatch {
    
    public void reset();

    public void await() throws InterruptedException;

    public void countDown();

}
