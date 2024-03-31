package pcd.ass01.simtrafficbaseconcurrent.agent;

import java.util.Optional;

import pcd.ass01.simengineconcurrent.*;
import pcd.ass01.simtrafficbaseconcurrent.environment.Road;

/**
 * 
 * Base class modeling the skeleton of an agent modeling a car in the traffic environment
 * 
 */
public class CarAgent extends AbstractAgent {
	
	/* car model */
	protected double maxSpeed;
	protected double currentSpeed;
	protected double acceleration;
	protected double deceleration;
	protected double currentPosition;

	
	public CarAgent(String id, Road road, 
			double initialPos, 
			double acc, 
			double dec,
			double vmax) {
		super(id);
		this.acceleration = acc;
		this.deceleration = dec;
		this.maxSpeed = vmax;
		this.currentSpeed = 0;
	}

	public double getCurrentSpeed() {
		return currentSpeed;
	}
	
	public void setCurrentSpeed(double currentSpeed) {
		this.currentSpeed = currentSpeed;
	}
	
	public double getAcceleration() {
		return acceleration;
	}
	
	public void setAcceleration(double acceleration) {
		this.acceleration = acceleration;
	}
	
	public double getDeceleration() {
		return deceleration;
	}
	
	public void setDeceleration(double deceleration) {
		this.deceleration = deceleration;
	}
	
	public double getCurrentPosition() {
		return currentPosition;
	}

	public void setCurrentPosition(double currentPosition) {
		this.currentPosition = currentPosition;
	}
	
	protected void log(String msg) {
		System.out.println("[CAR " + this.getId() + "] " + msg);
	}
	
}
