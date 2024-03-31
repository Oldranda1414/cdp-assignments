package pcd.ass01.simengineconcurrent;

import java.util.Map;

/**
*   
* Base class to define the environment of the simulation
*   
*/
public abstract class AbstractEnvironment {

	protected Map<String, AbstractAgent> agents;

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

	public void addAgent(AbstractAgent agent){
		this.agents.put(agent.getId(), agent);
	}
    
}
