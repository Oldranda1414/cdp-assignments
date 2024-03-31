package pcd.ass01.simtrafficbase;

import java.util.Optional;

import pcd.ass01.simengineconcurrent.*;

/**
 * 
 * Base class modeling the skeleton of an agent modeling a car in the traffic environment
 * 
 */
public abstract class CarAgent extends AbstractAgent {
	
	/* car model */
	protected double maxSpeed;
	protected double currentSpeed;
	protected double acceleration;
	protected double deceleration;

	
	public CarAgent(String id, Road road, 
			double initialPos, 
			double acc, 
			double dec,
			double vmax) {
		super(id);
		this.acceleration = acc;
		this.deceleration = dec;
		this.maxSpeed = vmax;
	}

	public double getCurrentSpeed() {
		return currentSpeed;
	}
	
	protected void log(String msg) {
		System.out.println("[CAR " + this.getId() + "] " + msg);
	}

}
