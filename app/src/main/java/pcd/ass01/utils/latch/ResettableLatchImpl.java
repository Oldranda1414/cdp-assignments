package pcd.ass01.utils.latch;

import java.util.concurrent.CountDownLatch;

public class ResettableLatchImpl implements ResettableLatch{

    private CountDownLatch latch;
    private int count;

    public ResettableLatchImpl(final int count){
        this.count = count;
        this.latch = new CountDownLatch(count);
    }

    @Override
    public void reset() {
        synchronized(this){
            if(this.latch.getCount() != 0){
                throw new IllegalStateException("The latch cannot be resetted because threads are awaiting on the latch");
            }
            this.latch = new CountDownLatch(count);
        }
    }

    @Override
    public void await() throws InterruptedException {
        this.latch.await();
    }

    @Override
    public void countDown() {
        this.latch.countDown();
    }    
}
