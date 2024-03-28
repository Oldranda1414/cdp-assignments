package pcd.ass01.simengineconcurrent;

import java.util.List;

import pcd.ass01.simengineconcurrent.latch.*;

public class Master extends Thread{

    private List<Runnable> works;
    private int nWorkers;
    private int nAgents;
    private BoundedBuffer<Runnable> bagOfTasks;
    private ResettableLatch workersDone;
    private ResettableLatch workReady;
    private AbstractEnvironment env;
    private int dt;

    public Master(final int nWorkers,
            // final int nAgents,
            final List<Runnable> works,
            final AbstractEnvironment env,
            int dt){
        this.nWorkers = nWorkers;
        this.nAgents = works.size();
        this.works = works;
        this.env = env;
        this.dt = dt;
        this.workersDone = new ResettableLatchImpl(nWorkers);
        this.workReady = new ResettableLatchImpl(1);
        this.bagOfTasks = new BagOfTasks(this.nAgents);

    }
    
    public void run(int nSteps) throws InterruptedException{
        log("Starting Simulation");
        Worker[] workers = new Worker[this.nWorkers];
        for(int i = 0; i < this.nWorkers; i++){
            workers[i] = new Worker(this.bagOfTasks, this.workersDone, this.workReady/*, this.env, this.agentWills*/);
            workers[i].start();
        }
        for(int step = 1; step <= nSteps; step++){
            log("executing step " + step + " of the simulation");
            this.env.step(dt);
            log("filling the bag with tasks sense-decide-act");
            for(var work : works){
                this.bagOfTasks.put(work);
            }
            this.workReady.countDown(); //notifing workers that bag is full of tasks
            log("going to sleep until workers finish current tasks");
            this.workersDone.await(); //wait for all workers to finish the tasks
            log("finished executing step " + step + " of the simulation");
            this.workReady.reset(); //reset the latch for the next step
        }
    }
    
    private void log(String message){
        synchronized(System.out){
            System.out.println("[master]: " + message);
        }
    }

}