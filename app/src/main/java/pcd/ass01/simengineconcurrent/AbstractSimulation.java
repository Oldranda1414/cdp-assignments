package pcd.ass01.simengineconcurrent;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for defining concrete simulations
 *  
 */
public abstract class AbstractSimulation {

	/* environment of the simulation */
	private AbstractEnvironment env;
	private List<AbstractAgent> agents;
	private List<Runnable> senseDecideWorks;
	private List<Runnable> actWorks;
	private int t0;
	private int dt;
	private long startWallTime;
	private long endWallTime;
	private long averageTimePerStep;
	private Master master;
	
	/* simulation listeners */
	// private List<SimulationListener> listeners;

	protected AbstractSimulation() {
        //TODO GESTIRE I LISTENER DELLA GUI
		//listeners = new ArrayList<SimulationListener>();
		//agents = new ArrayList<AbstractAgent>(); I don't think this is necessary
		senseDecideWorks = new ArrayList<Runnable>();
		actWorks = new ArrayList<Runnable>();
	}
	
	/**
	 * 
	 * Method used to configure the simulation, specifying env and agents
	 * 
	 */
	protected abstract void setup(int numberOfSteps);
	
	/**
	 * Method running the simulation for a number of steps,
	 * using a sequential approach
	 * 
	 * @param numSteps
	 */
	public void run(int numSteps) {
		startWallTime = System.currentTimeMillis();

		/* initialize the env and the agents inside */
		int t = t0;

		env.init();
		// I don't think this is necessary
		/*
		for (var agent: agents) {
			agent.init(env);
		}
		*/

		this.notifyReset(t, agents, env); //TODO This needs some rework
		
		long timePerStep = 0;
		
		this.master = new Master(ComputeBestNumOfWorkers(), this.senseDecideWorks, this.actWorks, this.env, this.dt, numSteps);
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

	protected void setupTimings(int t0, int dt) {
		this.t0 = t0;
		this.dt = dt;
	}
	
	protected void setupEnvironment(AbstractEnvironment env) {
		this.env = env;
	}

	protected void addAgent(AbstractAgent agent) {
		this.agents.add(agent);
	}

	protected void addAct(Runnable act) {
		this.actWorks.add(act);
	}

	protected void addSenseDecide(Runnable senseDecide) {
		this.senseDecideWorks.add(senseDecide);
	}
	
	/* methods for listeners */
	
	// public void addSimulationListener(SimulationListener l) {
	// 	this.listeners.add(l);
	// }
	
	private void notifyReset(int t0, List<AbstractAgent> agents, AbstractEnvironment env) {
		// for (var l: listeners) {
		// 	l.notifyInit(t0, agents, env);
		// }
	}

	// private void notifyNewStep(int t, List<AbstractAgent> agents, AbstractEnvironment env) {
	// 	for (var l: listeners) {
	// 		l.notifyStepDone(t, agents, env);
	// 	}
	// }

	/*
	 * If it would return less than 1, it returns 1 instead
	 */
	private int ComputeBestNumOfWorkers() { //TODO: should this method be protected abstract?
		int cores = Runtime.getRuntime().availableProcessors();
		int standardThreads = 2; //number of threads to be used for other processes (in this case I calculate 1 thread for Master and 1 for the gui)
		int availableThreads = cores - standardThreads;
		return (availableThreads > 0) ? availableThreads : 1;
	}
}