package pcd.ass01.simengineconcurrent;

import java.util.ArrayList;
import java.util.List;

import pcd.ass01.simengineseq_improved.SimulationListener;

/**
 * Base class for defining concrete simulations
 *  
 */
public abstract class AbstractSimulation {

	private AbstractEnvironment<? extends AbstractAgent> env;
	private AbstractStates agentStates;
	private List<Runnable> senseDecideWorks;
	private List<Runnable> actWorks;
	private List<SimulationListener> listeners;
	private int t0;
	private int dt;
	private int numberOfAgents;
	private long startWallTime;
	private long endWallTime;
	private long averageTimePerStep;
	private Master master;

	protected AbstractSimulation() {
		listeners = new ArrayList<SimulationListener>();
		senseDecideWorks = new ArrayList<Runnable>();
		actWorks = new ArrayList<Runnable>();
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
		startWallTime = System.currentTimeMillis();
		env.setupNumberOfAgents(this.numberOfAgents);

		env.setup();
		
		long timePerStep = 0;

		this.master = new Master(ComputeBestNumOfWorkers(), this.senseDecideWorks, this.actWorks, this.env, this.t0, this.dt, numSteps, this.listeners);
		try {
			master.start();
			master.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		endWallTime = System.currentTimeMillis();
		this.averageTimePerStep = timePerStep / numSteps;
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

	protected void setupAgentStates(AbstractStates states){
		this.agentStates = states;
	}

	protected AbstractStates getAgentStates(){
		return this.agentStates;
	}

	protected void addAct(Runnable act) {
		this.actWorks.add(act);
	}

	protected void addSenseDecide(Runnable senseDecide) {
		this.senseDecideWorks.add(senseDecide);
	}

	protected void setupNumberOfAgents(int numberOfAgents){
		this.numberOfAgents = numberOfAgents;
	}

	/*
	 * If it would return less than 1, it returns 1 instead
	 */
	private int ComputeBestNumOfWorkers() {
		int cores = Runtime.getRuntime().availableProcessors();
		int standardThreads = 2; //number of threads to be used for other processes (in this case I calculate 1 thread for Master and 1 for the gui)
		int availableThreads = cores - standardThreads;
		return (availableThreads > 0) ? availableThreads : 1;
	}
}