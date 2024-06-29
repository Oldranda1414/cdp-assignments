package simtrafficexamples.listeners;

import actor.Command;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;

import simengine.SimulationListener;

/**
 * Simple class keeping track of some statistics about a traffic simulation
 * - average number of cars
 * - min speed
 * - max speed
 */
public class RoadSimStatistics extends SimulationListener {

	private double stepsDurationsSum;
	private int stepNumber;
	private double minSpeed;
	private double maxSpeed;
	
	public static Behavior<Command> create() {
		return Behaviors.setup(RoadSimStatistics::new);
	}

	private RoadSimStatistics(ActorContext<Command> context) {
		super(context);
		this.stepsDurationsSum = 0;
		this.stepNumber = 0;
		this.minSpeed = 0;
		this.maxSpeed = 0;
	}

	@Override
	protected Behavior<Command> onViewUpdate(ViewUpdate command) {
		this.stepNumber = command.stepNumber();

		double speed = 1000.0 / (double) command.deltaMillis();	// Number of steps per second
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
		return this;
	}

	@Override
	protected Behavior<Command> onSimulationFinished(SimulationFinished command) {
		getContext().stop(getContext().getSelf());
		return this;
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
