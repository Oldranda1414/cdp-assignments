package pcd.ass01.simengineconcurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import pcd.ass01.simengineseq_improved.SimulationListener;
import pcd.ass01.utils.Buffer;
import pcd.ass01.utils.latch.*;

public class Master extends Thread {

    private List<Runnable> senseDecideWorks;
    private List<Runnable> actWorks;
    //private List<SimulationListener> listeners;
    private int nWorkers;
    private int nAgents;
    private List<Worker> workers;
    private Buffer<Runnable> bagOfTasks;
    private ResettableLatch workersReady;
    private ResettableLatch workReady;
    private AtomicBoolean simulationOver;
    private AbstractEnvironment<? extends AbstractAgent> env;
    private int dt;
    //private int t0;
    private int nSteps;

    public Master(
        final int nWorkers,
        final List<Runnable> senseDecideWorks,
        final List<Runnable> actWorks,
        final AbstractEnvironment<? extends AbstractAgent> env,
        final int t0,
        final int dt,
        final int nSteps,
        final List<SimulationListener> listeners
    ) {
        this.nWorkers = nWorkers;
        this.nAgents = senseDecideWorks.size();
        this.senseDecideWorks = senseDecideWorks;
        this.actWorks = actWorks;
        this.env = env;
        this.dt = dt;
        //this.t0 = t0;
        this.workersReady = new ResettableLatchImpl(nWorkers);
        this.workReady = new ResettableLatchImpl(1);
        this.bagOfTasks = new BagOfTasks();
        this.simulationOver = new AtomicBoolean(false);
        this.nSteps = nSteps;
        //this.listeners = listeners;
    }

    @Override
    public void run() {
        /* initialize the env and the agents inside */
		//int t = t0;
        try {
            //this.notifyReset(t, env);
            log("Starting Simulation");
            
            this.initWorkers();
            log("awaiting for workers to be ready");
            this.workersReady.await();   //wait for all workers to be ready
            this.workersReady.reset();

            for(int step = 1; step <= nSteps; step++) {
                log("executing step " + step + " of the simulation");

                this.env.step(dt);

                bagStep("sense-decide", senseDecideWorks);
                
                bagStep("act", actWorks);

                log("finished executing step " + step + " of the simulation");

                //t += dt;

                //notifyNewStep(t, env);
            }

            this.simulationOver.set(true);
            this.workReady.countDown();

            for(var worker : this.workers)worker.join();

            log("simulation finished");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void initWorkers(){
        this.workers = new ArrayList<Worker>();
        for (int i = 0; i < this.nWorkers; i++) {
            var w = new Worker(this.bagOfTasks, this.workersReady, this.workReady, this.simulationOver);
            this.workers.add(w);
            w.start();
        }
        log("workers initialized");
    }

    private void bagStep(String workName, List<Runnable> works) throws InterruptedException{
        fillBag(workName, works);

        this.workReady.countDown(); //notifing workers that bag is full of tasks
        log("going to sleep until workers finish " + workName + " works");
        this.workReady.reset(); //reset the latch for the next tasks
        this.workersReady.await(); //wait for all workers to finish the tasks
        this.workersReady.reset();
    }

    private void fillBag(String workName, List<Runnable> works) throws InterruptedException{
        for(var work : works){
            this.bagOfTasks.put(work);
        }
        log("filled bag with " + workName + " works");
    }
    
    private void log(String message){
        synchronized(System.out){
            System.out.println("[master]: " + message);
        }
    }
	
    /* 
	private void notifyReset(int t0, AbstractEnvironment<? extends AbstractAgent> env) {
		for (var l: listeners) {
			// l.notifyInit(t0, env.getAgents(), env);
		}
	}

	private void notifyNewStep(int t, AbstractEnvironment<? extends AbstractAgent> env) {
		for (var l: listeners) {
			// l.notifyStepDone(t, env.getAgents(), env);
		}
	}
    */
}