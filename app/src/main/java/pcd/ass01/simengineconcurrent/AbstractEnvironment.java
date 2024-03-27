package pcd.ass01.simengineconcurrent;

/**
*   
* Base class to define the environment of the simulation
*   
*/
public abstract class AbstractEnvironment {

	private String id;
	
	protected AbstractEnvironment(String id) {
		this.id = id;		
	}
	
	public String getId() {
		return id;
	}
	
	/**
	 * 
	 * Called at the beginning of the simulation
	 */
	public abstract void init();
	
	/**
	 * 
	 * Called at each step of the simulation
	 * 
	 * @param dt
	 */
	public abstract void step();
    
}
