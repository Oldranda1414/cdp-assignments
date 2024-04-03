package pcd.ass01.simengineconcurrent;

import pcd.ass01.utils.Buffer;
import pcd.ass01.utils.latch.ResettableLatch;

public class Worker extends Thread {
    private Buffer<Runnable> bagOfTasks;
    private ResettableLatch workersReady;
    private ResettableLatch workReady;

    public Worker(
        Buffer<Runnable> bagOfTasks,
        ResettableLatch workersReady,
        ResettableLatch workReady
    ) {
        this.bagOfTasks = bagOfTasks;
        this.workersReady = workersReady;
        this.workReady = workReady;
    }

    @Override
    public void run() {
        try {
            while (true) {
                log("woke up");
                while(!this.bagOfTasks.isEmpty()){
                    log("fetching and running one work");
                    var task = this.bagOfTasks.get();
                    task.run();
                }
                log("bag of tasks is empty, stopping work");
                this.workersReady.countDown();
                log("awaiting on workReady");
                this.workReady.await();
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
