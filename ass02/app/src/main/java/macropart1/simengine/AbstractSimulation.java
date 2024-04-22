package macropart1.simengine;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Base class for defining concrete simulations
 *  
 */
public abstract class AbstractSimulation<T extends AbstractEnvironment<? extends AbstractAgent>> extends Thread{

	private int numberOfAgents;
	private AbstractEnvironment<? extends AbstractAgent> env;
	private AbstractStates<T> agentStates;
	private List<SimulationListener> listeners;
	private int t0;
	private int dt;
	private long startWallTime;
	private long endWallTime;
	private long averageTimePerStep;
	private boolean toBeInSyncWithWallTime;
	private int nStepsPerSec;
	private Semaphore startAndStop;
	private ExecutorService executor;

	protected AbstractSimulation(int numberOfAgents) {
		this.numberOfAgents = numberOfAgents;
		executor = Executors.newFixedThreadPool(this.ComputeBestNumOfThreads());
		this.listeners = new ArrayList<SimulationListener>();
		this.startAndStop = new Semaphore(0);
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
		System.out.println("Simulation started");

		startWallTime = System.currentTimeMillis();

		long timePerStep = 0;

		endWallTime = System.currentTimeMillis();
		this.averageTimePerStep = timePerStep / numSteps;

		System.out.println("Simulation finished");
	}

	public long getSimulationDuration() {
		return endWallTime - startWallTime;
	}
	
	public long getAverageTimePerCycle() {
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

	protected void syncWithTime(int nCyclesPerSec){
		this.toBeInSyncWithWallTime = true;
		this.nStepsPerSec = nCyclesPerSec;
	}

	/*
	 * If it would return less than 1, it returns 1 instead
	 */
	private int ComputeBestNumOfThreads() {
		int cores = Runtime.getRuntime().availableProcessors();
		int standardThreads = 2; //number of threads to be used for other processes (in this case I calculate 1 thread for this and 1 for the gui)
		int availableThreads = cores - standardThreads;
		return availableThreads;
	}
}