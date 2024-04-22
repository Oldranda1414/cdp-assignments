package macropart1.simengine;

// import java.util.List;

public interface SimulationListener {

	/**
	 * Called at the beginning of the simulation
	 * 
	 * @param t
	 * @param env
	 */
	void notifyInit(int t, AbstractEnvironment<? extends AbstractAgent> env);
	
	/**
	 * Called at each step, updater all updates
	 * @param t
	 * @param deltaMillis
	 * @param env
	 */
	void notifyStepDone(int t, int stepNumber, long deltaMillis, AbstractEnvironment<? extends AbstractAgent> env);
}
