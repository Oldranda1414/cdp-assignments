package pcd.ass01.simengineconcurrent;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import pcd.ass01.utils.Buffer;
import pcd.ass01.utils.latch.ResettableLatch;

public class Worker extends Thread {
    private Buffer<Task> bagOfTasks;
    private ResettableLatch workersReady;
    private ResettableLatch workReady;
    private AtomicBoolean simulationOver;

    public Worker(
        Buffer<Task> bagOfTasks,
        ResettableLatch workersReady,
        ResettableLatch workReady,
        AtomicBoolean simulationOver
    ) {
        this.bagOfTasks = bagOfTasks;
        this.workersReady = workersReady;
        this.workReady = workReady;
        this.simulationOver = simulationOver;
    }

    @Override
    public void run() {
        try {
            while (!simulationOver.get()) {
                log("bag of tasks is empty, stopping work");
                this.workersReady.countDown();
                log("awaiting on workReady");
                this.workReady.await();
                log("woke up");
                Optional<Task> task;
                do{
                    log("fetching a task");
                    task = this.bagOfTasks.get();
                    if(task.isPresent()){
                        var actualTask = task.get();
                        log("running a " + actualTask.getTypeOfTask() + " task for agent " + actualTask.getAgentId());
                        actualTask.run();
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
            }

            log("worker killed");
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
