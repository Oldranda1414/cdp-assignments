package pcd.ass01.simengineconcurrent;

import java.util.Optional;

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
                this.workersReady.countDown();
                log("awaiting on workReady");
                this.workReady.await();
                log("woke up");
                Optional<Runnable> task;
                do{
                    log("fetching a task");
                    task = this.bagOfTasks.get();
                    if(task.isPresent()){
                        log("running a task");
                        task.get().run();
                    }
                }while(task.isPresent());
                //TODO check which version is prefered (while/do-while)
                /*
                while(!this.bagOfTasks.isEmpty()){
                    log("fetching and running one work");
                    var task = this.bagOfTasks.get();
                    if(task.isPresent()){
                        task.get().run();
                    }
                }
                */
                log("bag of tasks is empty, stopping work");
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
