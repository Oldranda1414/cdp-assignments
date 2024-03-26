package pcd.ass01.thread;

import java.util.concurrent.Phaser;

public abstract class Master extends Thread{

    private Phaser workDoneLatch;
    private Phaser startWorkLatch;
    private int nWorkers;
    private int nAgents;
    private AbstractEnvironment env;

    public Master(final int nWorkers,
            final int nAgents,
            final AbstractEnvironment env
            ){
        this.nWorkers = nWorkers;
        this.nAgents = nAgents;
        this.workDoneLatch = new Phaser(nWorkers);
        this.startWorkLatch = new Phaser(1);
        this.env = env;

    }
    
    public void run(int nSteps){
        log("Starting Simulation");
        Worker[] workers = new Worker[this.nWorkers];
        for(var worker : workers){
            worker = new Worker(this.workDoneLatch, this.startWorkLatch);
        }
        for(int step = 1; step <= nSteps; step++){
            this.env.step();
            //TODO ADD SENSE-DECIDE TASKS TO THE BAG OF WORKS
            this.startWorkLatch.(); //notifing workers that bag is full of work
            this.workDoneLatch.await(); //wait for all workers to finish the works
            //TODO ADD SENSE-DECIDE TASKS TO THE BAG OF WORKS
        }
    }
    
    private void log(String message){
        synchronized(System.out){
            System.out.println("[master]: " + message);
        }
    }

    protected abstract void senseDecide();

    protected abstract void act();
}