package pcd.ass01.simengineconcurrent;

import pcd.ass01.simengineconcurrent.latch.ResettableLatch;
import pcd.ass01.utils.Buffer;

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
                log("awaiting on workReady");
                this.workReady.await();
                log("working...");
                while(!this.bagOfTasks.isEmpty()){
                    log("fetching and running one work");
                    var task = this.bagOfTasks.get();
                    task.run();
                }
                log("bag of tasks is empty, stopping work");
                this.workersDone.countDown();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void log(String message){
        synchronized(System.out){
            System.out.println("[" + this.getName() + "]: " + message);
        }
    }
}
