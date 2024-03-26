package pcd.ass01.latch;

public interface Latch {
    
    public void reset();

    public void await();

    public void notifyCompletion();

    public void stop();

    public void stopped();

}
