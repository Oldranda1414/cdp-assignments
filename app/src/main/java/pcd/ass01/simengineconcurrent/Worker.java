package pcd.ass01.simengineconcurrent;

import pcd.ass01.simengineconcurrent.latch.ResettableLatch;

public class Worker extends Thread {
    private Buffer<Runnable> bagOfTasks;
    private ResettableLatch workersDone;
    private ResettableLatch workReady;

    public Worker(
        Buffer<Runnable> bagOfTasks,
        ResettableLatch workersDone,
        ResettableLatch workReady
    ) {
        this.bagOfTasks = bagOfTasks;
        this.workersDone = workersDone;
        this.workReady = workReady;
    }

    @Override
    public void run() {
        try {
            while (true) {
                this.workReady.await();
                var task = this.bagOfTasks.get();
                task.run();
                this.workersDone.countDown();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
