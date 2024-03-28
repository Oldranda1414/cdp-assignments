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
	private List<Runnable> works;
	private int t0;
	private int dt;
	private long startWallTime;
	private long endWallTime;
	private long averageTimePerStep;
	
	/* simulation listeners */
	// private List<SimulationListener> listeners;

	protected AbstractSimulation() {
        //TODO GESTIRE I LISTENER DELLA GUI
		//listeners = new ArrayList<SimulationListener>();
		agents = new ArrayList<AbstractAgent>();
		works = new ArrayList<>();
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

		/* initialize the env and the agents inside */
		int t = t0;

		env.init();
		for (var agent: agents) {
			agent.init(env);
		}

		this.notifyReset(t, agents, env);
		
		long timePerStep = 0;
		
		var master = new Master(ComputeBestNumOfWorkers(), this.works, this.env, this.dt);
		try {
			master.run(numSteps);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		endWallTime = System.currentTimeMillis();
		this.averageTimePerStep = timePerStep / numSteps;
	}

	public long getSimulationDuration() {
		//TODO: it do not work. It should await master end to retrieve correct infos
		return endWallTime - startWallTime;
	}
	
	public long getAverageTimePerCycle() {
		//TODO: it do not work. It should await master end to retrieve correct infos
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

	protected void addRunnable(Runnable runnable) {
		this.works.add(runnable);
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

	private int ComputeBestNumOfWorkers() { //TODO: should this method be protected abstract?
		return 3;
	}
}