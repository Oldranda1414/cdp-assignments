package simtrafficexamples.simulations;

import akka.actor.typed.ActorRef;
import akka.actor.typed.javadsl.ActorContext;

import simengine.AbstractStates;
import simtrafficbase.environment.Road;
import simtrafficbase.environment.RoadsEnv;
import simtrafficbase.states.CarStates;
import utils.P2d;
import utils.Pair;

import java.util.List;

import actor.Command;

/**
 * 
 * Traffic Simulation about 30 cars moving on a single road, no traffic lights
 * 
 */
public class TrafficsimulationSingleRoadMassiveNumberOfCars extends CarSimulation<TrafficsimulationSingleRoadMassiveNumberOfCars> {

	public TrafficsimulationSingleRoadMassiveNumberOfCars(ActorContext<Command> context, List<ActorRef<Command>> listeners) {
		super(context, listeners);
		this.setDistances(20);
		this.setup();
	}

	@Override
	protected void setDistances(double breakingDistance) {
		this.brakingDistance = breakingDistance;
		this.seeingDistance = brakingDistance + 10;
	}

	private void setup() {

		final double carMaxSpeed = 50;
		final double carAccelleration = 2;
		final double carDecelleration = 2;
		final Pair<P2d, P2d> roadPoints = new Pair<>(new P2d(0, 300), new P2d(15000, 300));

		int numberOfCars = 5000;

		int t0 = 0;
		int dt = 1;

		this.setupTimings(t0, dt);

		RoadsEnv env = new RoadsEnv();
		this.setupEnvironment(env);
		AbstractStates<RoadsEnv> states = new CarStates();
		this.setupAgentStates(states);
		Road road = env.createRoad(roadPoints.getFirst(), roadPoints.getSecond());
		for (int i = 1; i <= numberOfCars; i++) {
			double position = i * ((road.getLen() - 2) / numberOfCars);
			String id = Integer.toString(i);
			env.createCar(id, road, position, carAccelleration, carDecelleration, carMaxSpeed);
			this.addSenseDecide(this.getSenseDecide(id));
			this.addAct(this.getAct(id));
		}

		/* sync with wall-time: 25 steps per sec */
		this.syncWithTime(60);
	}

}
