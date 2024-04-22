package macropart1.executor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

import macropart1.simengine.AbstractAgent;
import macropart1.simengine.AbstractEnvironment;
import macropart1.simengine.SimulationListener;

public class Master extends Thread {

    private List<Task> senseDecideWorks;
    private List<Task> actWorks;
    private List<SimulationListener> listeners;
    private AtomicBoolean simulationOver;
    private AbstractEnvironment<? extends AbstractAgent> env;
    private int dt;
    private int t0;
    private int nSteps;
    private boolean toBeInSyncWithWallTime;
    private int nStepsPerSec;
    private long currentWallTime;
    private Semaphore startAndStop;
    private ExecutorService executor;

    public Master(
        final int nThreads,
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
        this.senseDecideWorks = senseDecideWorks;
        this.actWorks = actWorks;
        this.env = env;
        this.dt = dt;
        this.t0 = t0;
        this.simulationOver = new AtomicBoolean(false);
        this.nSteps = nSteps;
        this.listeners = listeners;
        this.toBeInSyncWithWallTime = toBeInSyncWithWallTime;
        this.nStepsPerSec = nStepsPerSec;
        this.startAndStop = startAndStop;
		this.executor = Executors.newFixedThreadPool(nThreads);
    }

    @Override
    public void run() {
        /* initialize the env and the agents inside */
		int t = t0;
        try {
            this.notifyReset(t, env);
            
            for(int step = 1; step <= nSteps; step++) {
                
                startAndStop.acquire();
                
                currentWallTime = System.currentTimeMillis();

                this.env.step(dt);
                
                try {
                    step("sense-decide", senseDecideWorks);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                
                try {
                    step("act", actWorks);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                t += dt;

                startAndStop.release();
                
                if (toBeInSyncWithWallTime) {
                    syncWithWallTime();
                }
                
                notifyNewStep(t, step, System.currentTimeMillis() - currentWallTime, env);
            }
            this.simulationOver.set(true);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void step(String taskType, List<Task> tasks) throws InterruptedException, ExecutionException{
        List<Future<?>> futures = new ArrayList<>();
        fillBag(taskType, tasks, futures);
        for(var future : futures){
            future.get();
        }
    }

    private void fillBag(String workName, List<Task> works, List<Future<?>> futures) throws InterruptedException{
        for(Task work : works){
            futures.add(executor.submit(work));
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