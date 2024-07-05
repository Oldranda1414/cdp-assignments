package pcd.ass01.masterworker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

import pcd.ass01.simengineconcurrent.AbstractAgent;
import pcd.ass01.simengineconcurrent.AbstractEnvironment;
import pcd.ass01.simengineconcurrent.SimulationListener;
import pcd.ass01.utils.Buffer;
import pcd.ass01.utils.latch.*;

public class Master extends Thread {

    private List<Task> senseDecideWorks;
    private List<Task> actWorks;
    private List<SimulationListener> listeners;
    private int nWorkers;
    private List<Worker> workers;
    private Buffer<Task> bagOfTasks;
    private ResettableLatch workersReady;
    private AtomicBoolean simulationOver;
    private AbstractEnvironment<? extends AbstractAgent> env;
    private int dt;
    private int t0;
    private int nSteps;
    private boolean toBeInSyncWithWallTime;
    private int nStepsPerSec;
    private long currentWallTime;
    private Semaphore startAndStop;

    public Master(
        final int nWorkers,
        final List<Task> senseDecideWorks,
        final List<Task> actWorks,
        final AbstractEnvironment<? extends AbstractAgent> env,
        final int t0,
        final int dt,
        final int nSteps,
        final List<SimulationListener> listeners,
        final boolean toBeInSyncWithWallTime,
        final int nStepsPerSec,
        final Semaphore startAndStop
    ) {
        this.nWorkers = nWorkers;
        this.senseDecideWorks = senseDecideWorks;
        this.actWorks = actWorks;
        this.env = env;
        this.dt = dt;
        this.t0 = t0;
        this.workersReady = new ResettableLatchImpl(nWorkers);
        this.bagOfTasks = new BagOfTasks(this.workersReady);
        this.simulationOver = new AtomicBoolean(false);
        this.nSteps = nSteps;
        this.listeners = listeners;
        this.toBeInSyncWithWallTime = toBeInSyncWithWallTime;
        this.nStepsPerSec = nStepsPerSec;
        this.startAndStop = startAndStop;
    }

    @Override
    public void run() {
        /* initialize the env and the agents inside */
		int t = t0;
        try {
            this.notifyReset(t, env);
            
            this.initWorkers();
            this.workersReady.await();   //wait for all workers to be ready
            this.workersReady.reset();

            for(int step = 1; step <= nSteps; step++) {
                
                startAndStop.acquire();
                
                currentWallTime = System.currentTimeMillis();

                this.env.step(dt);
                
                bagStep("sense-decide", senseDecideWorks);
                
                bagStep("act", actWorks);

                t += dt;

                startAndStop.release();
                
                if (toBeInSyncWithWallTime) {
                    syncWithWallTime();
                }
                
                notifyNewStep(t, step, System.currentTimeMillis() - currentWallTime, env);
            }
            this.simulationOver.set(true);

            for(Worker worker : this.workers) {
                worker.interrupt();
            }
            for(Worker worker : this.workers) {
                worker.join();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void initWorkers(){
        this.workers = new ArrayList<Worker>();
        for (int i = 0; i < this.nWorkers; i++) {
            Worker w = new Worker(this.bagOfTasks, this.simulationOver);
            this.workers.add(w);
            w.start();
        }
    }

    private void bagStep(String workName, List<Task> works) throws InterruptedException{
        fillBag(workName, works);

        if (!this.simulationOver.get()) {
            this.workersReady.await(); //wait for all workers to finish the tasks
            this.workersReady.reset();
        }
    }

    private void fillBag(String workName, List<Task> works) throws InterruptedException{
        for(Task work : works){
            this.bagOfTasks.put(work);
        }
    }
	
	private void notifyReset(int t0, AbstractEnvironment<? extends AbstractAgent> env) {
		for (SimulationListener l: listeners) {
			l.notifyInit(t0, env);
		}
	}

	private void notifyNewStep(int t, int stepNumber, long deltaMillis, AbstractEnvironment<? extends AbstractAgent> env) {
		for (SimulationListener l: listeners) {
			l.notifyStepDone(t, stepNumber, deltaMillis, env);
		}
	}

    private void syncWithWallTime() {
		try {
			long newWallTime = System.currentTimeMillis();
			long delay = 1000 / this.nStepsPerSec;
			long wallTimeDT = newWallTime - currentWallTime;
			if (wallTimeDT < delay) {
				Thread.sleep(delay - wallTimeDT);
			}
		} catch (Exception ex) {}
	}
}