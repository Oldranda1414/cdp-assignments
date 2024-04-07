package pcd.ass01.simtrafficexamplesconcurrent.simulations;

import pcd.ass01.simengineconcurrent.AbstractStates;
import pcd.ass01.simtrafficbaseconcurrent.P2d;
import pcd.ass01.simtrafficbaseconcurrent.entity.CarAgent;
import pcd.ass01.simtrafficbaseconcurrent.entity.TrafficLight;
import pcd.ass01.simtrafficbaseconcurrent.environment.Road;
import pcd.ass01.simtrafficbaseconcurrent.environment.RoadsEnv;
import pcd.ass01.simtrafficbaseconcurrent.states.CarStates;
import pcd.ass01.utils.Pair;

/**
 * 
 * Traffic Simulation about 30 cars moving on a single road, no traffic lights
 * 
 */
public class TrafficSimulationWithCrossRoads extends CarSimulation{

	public TrafficSimulationWithCrossRoads() {
		this.setDistances(20);
	}

	@Override
	protected void setDistances(double breakingDistance) {
		this.brakingDistance = breakingDistance;
		this.seeingDistance = brakingDistance + 10;
	}
	
	public void setup() {

		final Pair<P2d, P2d> firstRoadPoints = new Pair<>(new P2d(0, 300), new P2d(1500, 300));
		final Pair<P2d, P2d> secondRoadPoints = new Pair<>(new P2d(750, 0), new P2d(750, 600));

		int t0 = 0;
		int dt = 1;
		
		this.setupNumberOfAgents(4);
		this.setupTimings(t0, dt);
		
		RoadsEnv env = new RoadsEnv();
		this.setupEnvironment(env);
		AbstractStates<RoadsEnv> states = new CarStates();	
		this.setupAgentStates(states);

        //creating first road and related entities
		Road firstRoad = env.createRoad(firstRoadPoints.getFirst(), firstRoadPoints.getSecond());

        env.createTrafficLight(740, firstRoad, TrafficLight.TrafficLightState.GREEN, 75, 25, 100);

        CarAgent car;

        car = env.createCar("1", firstRoad, firstRoad.getLen(), 0.1, 0.3, 6);
		this.addSenseDecide(this.getSenseDecide(car.getId()));
		this.addAct(this.getAct(car.getId()));
        car = env.createCar("2", firstRoad, 100, 0.1, 0.3, 5);
		this.addSenseDecide(this.getSenseDecide(car.getId()));
		this.addAct(this.getAct(car.getId()));

        //creating second road and related entities
		Road secondRoad = env.createRoad(secondRoadPoints.getFirst(), secondRoadPoints.getSecond());

        env.createTrafficLight(290, secondRoad, TrafficLight.TrafficLightState.RED, 75, 25, 100);

        car = env.createCar("3", secondRoad, secondRoad.getLen(), 0.1, 0.2, 5);
		this.addSenseDecide(this.getSenseDecide(car.getId()));
		this.addAct(this.getAct(car.getId()));
        car = env.createCar("4", secondRoad, 100, 0.1, 0.1, 4);
		this.addSenseDecide(this.getSenseDecide(car.getId()));
		this.addAct(this.getAct(car.getId()));

		/* sync with wall-time: 25 steps per sec */
		this.syncWithTime(25);
	}	

}

