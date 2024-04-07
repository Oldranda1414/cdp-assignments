package pcd.ass01.simengineconcurrent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Semaphore;

import pcd.ass01.masterworker.Master;
import pcd.ass01.masterworker.Task;
// import pcd.ass01.simengineconcurrent.SimulationListener;
import pcd.ass01.simtrafficbaseconcurrent.entity.CarAgent;

/**
 * Base class for defining concrete simulations
 *  
 */
public abstract class AbstractSimulation<T extends AbstractEnvironment<? extends AbstractAgent>> {

	private AbstractEnvironment<? extends AbstractAgent> env;
	private AbstractStates<T> agentStates;
	private List<Task> senseDecideWorks;
	private List<Task> actWorks;
	private List<SimulationListener> listeners;
	private int t0;
	private int dt;
	private long startWallTime;
	private long endWallTime;
	private long averageTimePerStep;
	private Master master;
	private boolean toBeInSyncWithWallTime;
	private int nStepsPerSec;
	private Semaphore startAndStop;

	protected AbstractSimulation() {
		listeners = new ArrayList<SimulationListener>();
		senseDecideWorks = new ArrayList<Task>();
		actWorks = new ArrayList<Task>();
		startAndStop = new Semaphore(0);
	}
	
	/**
	 * 
	 * Method used to configure the simulation, specifying env and agents
	 * 
	 */
	protected abstract void setup();
	
	/**
	 * Method running the simulation for a number of steps,
	 * using a sequential approach
	 * 
	 * @param numSteps
	 */
	public void run(int numSteps) {
		try {
			this.resume();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Simulation started");

		startWallTime = System.currentTimeMillis();

		long timePerStep = 0;
		this.master = new Master(
			ComputeBestNumOfWorkers(), 
			this.senseDecideWorks, 
			this.actWorks, 
			this.env, 
			this.t0, 
			this.dt, 
			numSteps, 
			this.listeners,
			this.toBeInSyncWithWallTime,
			this.nStepsPerSec,
			this.startAndStop
		);
		new Thread(() -> {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			while (this.master.isAlive()) {
				String s = "[";
				for (AbstractAgent c : this.env.values()) {
					s += c.getId() + ": " + ((CarAgent) c).getCurrentSpeed() + ", ";
				}
				s += "]";
				System.out.println(s);
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
		try {
			master.start();
			master.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		endWallTime = System.currentTimeMillis();
		this.averageTimePerStep = timePerStep / numSteps;

		System.out.println("Simulation finished");
	}

	public void stop() throws InterruptedException {
		startAndStop.acquire();
	}

	public void resume() throws InterruptedException {
		startAndStop.release();
	}

	public long getSimulationDuration() {
		if (this.master.isAlive()) {
			return 0;
		}
		return endWallTime - startWallTime;
	}
	
	public long getAverageTimePerCycle() {
		if (this.master.isAlive()) {
			return 0;
		}
		return averageTimePerStep;
	}

	public void addSimulationListener(SimulationListener l) {
		this.listeners.add(l);
	}

	protected void setupTimings(int t0, int dt) {
		this.t0 = t0;
		this.dt = dt;
	}
	
	protected void setupEnvironment(AbstractEnvironment<? extends AbstractAgent> env) {
		this.env = env;
	}

	protected AbstractEnvironment<? extends AbstractAgent> getEnvironment(){
		return this.env;
	}

	protected void setupAgentStates(AbstractStates<T> states){
		this.agentStates = states;
	}

	protected AbstractStates<T> getAgentStates(){
		return this.agentStates;
	}

	protected void addAct(Task act) {
		this.actWorks.add(act);
	}

	protected void addSenseDecide(Task senseDecide) {
		this.senseDecideWorks.add(senseDecide);
	}

	protected void syncWithTime(int nCyclesPerSec){
		this.toBeInSyncWithWallTime = true;
		this.nStepsPerSec = nCyclesPerSec;
	}

	/*
	 * If it would return less than 1, it returns 1 instead
	 */
	private int ComputeBestNumOfWorkers() {
		int cores = Runtime.getRuntime().availableProcessors();
		int standardThreads = 2; //number of threads to be used for other processes (in this case I calculate 1 thread for Master and 1 for the gui)
		int availableThreads = cores - standardThreads;
		int numberOfAgents = this.env.allKeys().length;
		return Math.min((availableThreads > 0) ? availableThreads : 1, numberOfAgents); //* 4;
	}
}