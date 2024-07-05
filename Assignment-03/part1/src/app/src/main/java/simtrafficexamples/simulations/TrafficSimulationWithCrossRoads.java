package simtrafficexamples.simulations;

import akka.actor.typed.ActorRef;
import akka.actor.typed.javadsl.ActorContext;
import simengine.AbstractStates;
import simtrafficbase.environment.Road;
import simtrafficbase.environment.RoadsEnv;
import simtrafficbase.states.CarStates;
import simtrafficbase.entity.TrafficLight;
import simtrafficbase.entity.CarAgent;
import utils.Pair;
import utils.P2d;

import java.util.List;

import actor.Command;

/**
 * 
 * Traffic Simulation about 30 cars moving on a single road, no traffic lights
 * 
 */
public class TrafficSimulationWithCrossRoads extends CarSimulation<TrafficSimulationWithCrossRoads> {

	public TrafficSimulationWithCrossRoads(ActorContext<Command> context, List<ActorRef<Command>> listeners) {
		super(context, listeners);
		this.setDistances(40);
		final Pair<P2d, P2d> firstRoadPoints = new Pair<>(new P2d(0, 300), new P2d(1000, 300));
		final Pair<P2d, P2d> secondRoadPoints = new Pair<>(new P2d(750, 0), new P2d(750, 600));
		
		int t0 = 0;
		int dt = 1;
		this.setupTimings(t0, dt);
		
		RoadsEnv env = new RoadsEnv();
		this.setupEnvironment(env);
		AbstractStates<RoadsEnv> states = new CarStates();	
		this.setupAgentStates(states);

        //creating first road and related entities
		Road firstRoad = env.createRoad(firstRoadPoints.getFirst(), firstRoadPoints.getSecond());
        env.createTrafficLight(740, firstRoad, TrafficLight.TrafficLightState.GREEN, 75, 25, 100);
        CarAgent car;
        car = env.createCar("1", firstRoad, firstRoad.getLen(), 0.1, 0.7, 6);
		super.addSenseDecide(super.getSenseDecide(car.getId()));
		super.addAct(super.getAct(car.getId()));
        car = env.createCar("2", firstRoad, 100, 0.1, 0.7, 5);
		super.addSenseDecide(super.getSenseDecide(car.getId()));
		super.addAct(super.getAct(car.getId()));
        // creating second road and related entities
		Road secondRoad = env.createRoad(secondRoadPoints.getFirst(), secondRoadPoints.getSecond());
        env.createTrafficLight(290, secondRoad, TrafficLight.TrafficLightState.RED, 75, 25, 100);
        car = env.createCar("3", secondRoad, secondRoad.getLen(), 0.1, 0.7, 5);
		super.addSenseDecide(super.getSenseDecide(car.getId()));
		super.addAct(super.getAct(car.getId()));
        car = env.createCar("4", secondRoad, 100, 0.1, 0.7, 5);
		super.addSenseDecide(super.getSenseDecide(car.getId()));
		super.addAct(super.getAct(car.getId()));
		/* sync with wall-time: 25 steps per sec */
		super.syncWithTime(25);
	}

	@Override
	protected void setDistances(double breakingDistance) {
		this.brakingDistance = breakingDistance;
		this.seeingDistance = brakingDistance + 10;
	}
}

