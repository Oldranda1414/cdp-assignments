package pcd.ass01.simtrafficexamplesconcurrent.simulations;

import pcd.ass01.simengineconcurrent.AbstractStates;
import pcd.ass01.simtrafficbaseconcurrent.P2d;
import pcd.ass01.simtrafficbaseconcurrent.environment.RoadsEnv;
import pcd.ass01.simtrafficbaseconcurrent.states.CarStates;
import pcd.ass01.utils.Pair;

/**
 * 
 * Traffic Simulation about 2 cars moving on a single road, no traffic lights
 * 
 */
public class TrafficSimulationSingleRoadTwoCars extends CarSimulation{

	public TrafficSimulationSingleRoadTwoCars() {
		super();
	}
	
	public void setup() {

		final double carMaxSpeed = 5;
		final double carAccelleration = 1;
		final double carDecelleration = 2;
		final Pair<P2d, P2d> roadPoints = new Pair<>(new P2d(0, 300), new P2d(1000, 300));


		int numberOfCars = 2;

		int t0 = 0;
		int dt = 1;
		
		this.setupNumberOfAgents(numberOfCars);
		this.setupTimings(t0, dt);
		
		RoadsEnv env = new RoadsEnv();
		this.setupEnvironment(env);
		AbstractStates<RoadsEnv> states = new CarStates();	
		this.setupAgentStates(states);
		var road = env.createRoad(roadPoints.getFirst(), roadPoints.getSecond());
		for(int i = 1; i <= numberOfCars; i++){
			double position = i * (road.getLen()/numberOfCars);
			var id = Integer.toString(i);
			env.createCar(id, road, position, carAccelleration, carDecelleration, carMaxSpeed + i);
			this.addSenseDecide(this.getSenseDecide(id));
			this.addAct(this.getAct(id));
		}

		/* sync with wall-time: 25 steps per sec */
		this.syncWithTime(60);
	}	

}
