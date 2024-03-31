package pcd.ass01.simengineconcurrent;

/**
*   
* Base class to define the environment of the simulation
*   
*/
public abstract class AbstractEnvironment<I extends AbstractAgent> extends RWBuffer<I> {

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

	protected void setupNumberOfAgents(int numberOfAgents){
		this.numberOfAgents = numberOfAgents;
	}
    
}
