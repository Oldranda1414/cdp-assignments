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
	
	/* simulation listeners */
	private List<SimulationListener> listeners;

	protected AbstractSimulation() {
        //TODO GESTIRE I LISTENER DELLA GUI
		//listeners = new ArrayList<SimulationListener>();
		agents = new ArrayList<AbstractAgent>();

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
		// setup(); //TODO: setup should be called from the outside or from this method?
		env.init();
		notifyReset(0, agents, env);
		var master = new Master(3, ); //TODO: find a way to choose num of workers
		try {
			master.run(numSteps);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	protected void setupEnvironment(AbstractEnvironment env) {
		this.env = env;
	}

	protected void addAgent(AbstractAgent agent) {
		agents.add(agent);
	}
	
	/* methods for listeners */
	
	public void addSimulationListener(SimulationListener l) {
		this.listeners.add(l);
	}
	
	private void notifyReset(int t0, List<AbstractAgent> agents, AbstractEnvironment env) {
		for (var l: listeners) {
			l.notifyInit(t0, agents, env);
		}
	}

	private void notifyNewStep(int t, List<AbstractAgent> agents, AbstractEnvironment env) {
		for (var l: listeners) {
			l.notifyStepDone(t, agents, env);
		}
	}
}