package macropart1.simtrafficbase.entity;

import macropart1.simengine.*;
import macropart1.simtrafficbase.environment.Road;

/**
 * 
 * Base class modeling the skeleton of an agent modeling a car in the traffic environment
 * 
 */
public class CarAgent extends AbstractAgent implements RoadEntity{
	
	/* car model */
	protected double maxSpeed;
	protected double currentSpeed;
	protected double acceleration;
	protected double deceleration;
	protected double currentPosition;
	protected Road road;

	
	public CarAgent(String id, Road road, 
			double initialPos, 
			double acc, 
			double dec,
			double vmax) {
		super(id);
		this.road = road;
		this.acceleration = acc;
		this.deceleration = dec;
		this.maxSpeed = vmax;
		this.currentSpeed = 0;
		this.currentPosition = initialPos;
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
	
	public Road getRoad() {
		return this.road;
	}

	public double getMaxSpeed(){
		return this.maxSpeed;
	}
}
