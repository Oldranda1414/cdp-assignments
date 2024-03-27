package pcd.ass01.simengineconcurrent;

import java.util.List;

import pcd.ass01.simengineconcurrent.latch.ResettableLatch;
import pcd.ass01.simengineconcurrent.latch.ResettableLatchImpl;

public abstract class Master extends Thread{

    private List<Runnable> works;
    private int nWorkers;
    private int nAgents;
    private BoundedBuffer<Runnable> bagOfTasks;
    private ResettableLatch workersDone;
    private ResettableLatch workReady;

    public Master(final int nWorkers,
            final int nAgents,
            final List<Runnable> works
            ){
        this.nWorkers = nWorkers;
        this.nAgents = nAgents;
        this.works = works;
        this.workersDone = new ResettableLatchImpl(nWorkers);
        this.workReady = new ResettableLatchImpl(1);
        this.bagOfTasks = new BagOfTasks(nAgents);

    }
    
    public void run(int nSteps){
        log("Starting Simulation");
        Worker[] workers = new Worker[this.nWorkers];
        for(var worker : workers){
            worker = new Worker(this.workersDone, this.workReady);
        }
        for(int step = 1; step <= nSteps; step++){
            log("executing step " + step + " of the simulation");
            this.env.step();
            for(var work : works){
                log("filling the bag with tasks");
                //add the works in the bag of tasks
                this.workReady.countDown(); //notifing workers that bag is full of tasks
                log("going to sleep until workers finish current tasks");
                this.workersDone.await(); //wait for all workers to finish the tasks
            }
            //TODO UPDATE SIMULATION HISTORY
            log("finished executing step " + step + " of the simulation");
        }
    }
    
    private void log(String message){
        synchronized(System.out){
            System.out.println("[master]: " + message);
        }
    }

}