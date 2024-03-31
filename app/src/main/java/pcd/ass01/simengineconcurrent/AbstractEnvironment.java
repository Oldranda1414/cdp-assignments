package pcd.ass01.simengineconcurrent;

import java.util.Collection;
import java.util.Map;

/**
*   
* Base class to define the environment of the simulation
*   
*/
public abstract class AbstractEnvironment {

	protected Map<String, AbstractAgent> agents;
	protected int numberOfAgents;

	protected AbstractEnvironment() {
		
	}
	
	/**
	 * 
	 * Called at the beginning of the simulation
	 */
	public abstract void setup();
	
	/**
	 * 
	 * Called at each step of the simulation
	 * 
	 * @param dt
	 */
	public abstract void step(int dt);

	public Collection<AbstractAgent> getAgents(){
		return this.agents.values();
	}

	protected void setupNumberOfAgents(int numberOfAgents){
		this.numberOfAgents = numberOfAgents;
	}
    
}
