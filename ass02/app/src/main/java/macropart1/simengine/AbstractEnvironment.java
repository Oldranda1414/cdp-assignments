package simengine;

import pcd.ass01.utils.RWBuffer;

/**
*   
* Base class to define the environment of the simulation
*   
*/
public abstract class AbstractEnvironment<I extends AbstractAgent> extends RWBuffer<I> {

	protected AbstractEnvironment() {
		
	}
	
	/**
	 * 
	 * Called at each step of the simulation
	 * 
	 * @param dt
	 */
	public abstract void step(int dt);

}
