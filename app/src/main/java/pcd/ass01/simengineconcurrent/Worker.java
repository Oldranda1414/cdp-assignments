package pcd.ass01.simengineconcurrent;

import java.util.List;

import pcd.ass01.simengineconcurrent.latch.ResettableLatch;
import pcd.ass01.simengineseq.AbstractEnvironment;

public class Worker extends Thread{
    
    private BoundedBuffer<Runnable> bagOfTasks;
    private ResettableLatch workersDone;
    private ResettableLatch workReady;
    private AbstractEnvironment env;
    private List<Wills> agentWills;

    public Worker(BoundedBuffer<Runnable> bagOfTasks,
            ResettableLatch workersDone,
            ResettableLatch workReady,
            AbstractEnvironment env,
            List<Wills> agentWills
            ){
        this.bagOfTasks = bagOfTasks;
        this.workReady = workReady;
        this.workersDone = workersDone;
        this.env = env;
        this.agentWills = agentWills;
    }
    
    public void run(){
        while(true){
            this.workReady.await();
            while(!this.bagOfTasks.isEmpty()){
                var task = this.bagOfTasks.get();
                task.run(); //TODO EXECUTE THE TASK CORRECTLY
            }
        }
    }
    
    @SuppressWarnings("unused")
    private void log(String message){
        synchronized(System.out){
            System.out.println("[worker]: " + message);
        }
    }

}
