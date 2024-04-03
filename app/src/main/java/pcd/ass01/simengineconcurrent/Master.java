package pcd.ass01.simengineconcurrent;

import java.util.ArrayList;
import java.util.List;

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
    private ResettableLatch workersDone;
    private ResettableLatch workReady;
    private AbstractEnvironment<? extends AbstractAgent> env;
    private AbstractStates<AbstractEnvironment<? extends AbstractAgent>> states;
    private int dt;
    //private int t0;
    private int nSteps;

    public Master(
        final int nWorkers,
        final List<Runnable> senseDecideWorks,
        final List<Runnable> actWorks,
        final AbstractEnvironment<? extends AbstractAgent> env,
        final AbstractStates<AbstractEnvironment<? extends AbstractAgent>> states,
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
        this.states = states;
        this.dt = dt;
        //this.t0 = t0;
        this.workersDone = new ResettableLatchImpl(nWorkers);
        this.workReady = new ResettableLatchImpl(1);
        this.bagOfTasks = new BagOfTasks(this.nAgents);
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
            
            for(int step = 1; step <= nSteps; step++) {
                log("executing step " + step + " of the simulation");
                this.env.step(dt);
                this.states.clear();

                log("filling the bag with tasks sense-decide");
                for(var work : senseDecideWorks){
                        this.bagOfTasks.put(work);
                }
                this.workReady.countDown(); //notifing workers that bag is full of tasks
                log("going to sleep until workers finish current tasks");
                this.workReady.reset(); //reset the latch for the next tasks
                this.workersDone.await(); //wait for all workers to finish the tasks
                log("filling the bag with tasks act");
                for(var work : actWorks){
                        this.bagOfTasks.put(work);
                }
                this.workReady.countDown(); //notifing workers that bag is full of tasks
                log("going to sleep until workers finish current tasks");
                this.workersDone.await(); //wait for all workers to finish the tasks
                log("finished executing step " + step + " of the simulation");
                //reset the latches for the next step
                this.workReady.reset(); 
                this.workersDone.reset();

                //t += dt;

                //notifyNewStep(t, env);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void initWorkers(){
        this.workers = new ArrayList<Worker>();
        for (int i = 0; i < this.nWorkers; i++) {
            var w = new Worker(this.bagOfTasks, this.workersDone, this.workReady);
            this.workers.add(w);
            w.start();
        }
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