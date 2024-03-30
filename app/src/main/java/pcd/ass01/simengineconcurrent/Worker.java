package pcd.ass01.simengineconcurrent;

// import java.util.List;

import pcd.ass01.simengineconcurrent.latch.ResettableLatch;
/**
 * !the worker do NOT need the reference to the environment. Although it's him that will execute the duty, it do not need the reference due to the fact that the impl 
 * !of the duty is in the task itself. The task will have the reference to the environment and will execute the duty.
 */
public class Worker extends Thread{
    
    private BoundedBuffer<Runnable> bagOfTasks;
    private ResettableLatch workersDone;
    private ResettableLatch workReady;
    // private AbstractEnvironment env;
    // private List<Wills> agentWills;

    public Worker(BoundedBuffer<Runnable> bagOfTasks,
            ResettableLatch workersDone,
            ResettableLatch workReady//,
            // AbstractEnvironment env,
            // List<Wills> agentWills
            ){
        this.bagOfTasks = bagOfTasks;
        this.workReady = workReady;
        this.workersDone = workersDone;
        // this.env = env;
        // this.agentWills = agentWills;
    }
    
    public void run(){
        try {
            while(true){    //TODO i think that the run method is already executed in loop by the thread class, so no need for the while(true)
                this.workReady.await(); //The worker waits for the bag to be filled

                //as long as the bag is not empty the worker keeps executing the tasks
                while(!this.bagOfTasks.isEmpty()){
                    Runnable task;
                    task = this.bagOfTasks.get();
                    task.run();
                }
                this.workersDone.countDown(); //when the bag is empty the worker notifies he is going to sleep
            }
        } catch (InterruptedException e) {
            // TODO understand if there is a better way to handle this exception
            e.printStackTrace();
        }
    }
    
    @SuppressWarnings("unused")
    private void log(String message){
        synchronized(System.out){
            System.out.println("[worker]: " + message);
        }
    }

}
