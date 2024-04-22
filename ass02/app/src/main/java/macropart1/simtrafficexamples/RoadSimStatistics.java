package macropart1.simtrafficexamples;

// import java.util.List;

import macropart1.simengine.AbstractAgent;
import macropart1.simengine.AbstractEnvironment;
import macropart1.simengine.SimulationListener;

/**
 * Simple class keeping track of some statistics about a traffic simulation
 * - average number of cars
 * - min speed
 * - max speed
 */
public class RoadSimStatistics implements SimulationListener {

	private double stepsDurationsSum;
	private int stepNumber;
	private double minSpeed;
	private double maxSpeed;
	
	public RoadSimStatistics() {
		this.stepsDurationsSum = 0;
		this.stepNumber = 0;
		this.minSpeed = 0;
		this.maxSpeed = 0;
	}
	
	@Override
	public void notifyInit(int t, AbstractEnvironment<? extends AbstractAgent> env) {
		//
	}

	@Override
	public void notifyStepDone(int t, int stepNumber, long deltaMillis, AbstractEnvironment<? extends AbstractAgent> env) {
		this.stepNumber = stepNumber;

		double speed = 1000.0 / (double) deltaMillis;	// Number of steps per second
		if (speed == Double.POSITIVE_INFINITY) {
			speed = Double.MAX_VALUE;
		}
		if (stepNumber == 1) {
			this.minSpeed = speed;
			this.maxSpeed = speed;
		}

		this.stepsDurationsSum += speed;

		if (speed < this.minSpeed) {
			this.minSpeed = speed;
		}
		if (speed > this.maxSpeed) {
			this.maxSpeed = speed;
		}

		log(
			"Average step speed: " + this.getAverageSpeed()
			+ ", Min step speed: " + this.getMinSpeed()
			+ ", Max step speed: " + this.getMaxSpeed()
		);
	}
	
	public double getAverageSpeed() {
		return (double) this.stepsDurationsSum / (double) this.stepNumber;
	}

	public double getMinSpeed() {
		return this.minSpeed;
	}
	
	public double getMaxSpeed() {
		return this.maxSpeed;
	}
	
	private void log(String msg) {
		System.out.println("[STAT] " + msg);
	}

}
