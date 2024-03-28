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
		setup(); //TODO: setup should be called from the outside or from this method?
		env.init();
		// notifyReset(0, agents, env);
		var master = new Master(ComputeBestNumOfWorkers(), this.works, this.env, this.t0, this.dt);
		try {
			master.run(numSteps);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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
	
	// private void notifyReset(int t0, List<AbstractAgent> agents, AbstractEnvironment env) {
	// 	for (var l: listeners) {
	// 		l.notifyInit(t0, agents, env);
	// 	}
	// }

	// private void notifyNewStep(int t, List<AbstractAgent> agents, AbstractEnvironment env) {
	// 	for (var l: listeners) {
	// 		l.notifyStepDone(t, agents, env);
	// 	}
	// }

	private int ComputeBestNumOfWorkers() { //TODO: should this method be protected abstract?
		return 3;
	}
}