package simtrafficexamples.simulations;

import simengine.AbstractStates;
import simtrafficbase.environment.Road;
import simtrafficbase.environment.RoadsEnv;
import simtrafficbase.states.CarStates;
import simtrafficbase.entity.TrafficLight;
import utils.Pair;
import utils.P2d;

/**
 * 
 * Traffic Simulation about 2 cars moving on a single road, with one semaphore
 * 
 */
public class TrafficSimulationSingleRoadWithTrafficLightTwoCars extends CarSimulation{

	public TrafficSimulationSingleRoadWithTrafficLightTwoCars() {
		this.setDistances(40);
	}

	@Override
	protected void setDistances(double breakingDistance) {
		this.brakingDistance = breakingDistance;
		this.seeingDistance = brakingDistance + 10;
	}
	
	public void setup() {

		final double carMaxSpeed = 5;
		final double carAccelleration = 1;
		final double carDecelleration = 1;
		final Pair<P2d, P2d> roadPoints = new Pair<>(new P2d(0, 300), new P2d(1000, 300));

		int numberOfCars = 2;

		int t0 = 0;
		int dt = 1;
		
		this.setupTimings(t0, dt);
		
		RoadsEnv env = new RoadsEnv();
		this.setupEnvironment(env);
		AbstractStates<RoadsEnv> states = new CarStates();	
		this.setupAgentStates(states);
		Road road = env.createRoad(roadPoints.getFirst(), roadPoints.getSecond());
		env.createTrafficLight(500, road, TrafficLight.TrafficLightState.GREEN, 75, 25, 400);
		for(int i = 1; i <= numberOfCars; i++){
			double position = i * (road.getLen()/numberOfCars);
			String id = Integer.toString(i);
			env.createCar(id, road, position, carAccelleration, carDecelleration, carMaxSpeed);
			this.addSenseDecide(this.getSenseDecide(id));
			this.addAct(this.getAct(id));
		}

		/* sync with wall-time: 25 steps per sec */
		this.syncWithTime(25);
	}	

}