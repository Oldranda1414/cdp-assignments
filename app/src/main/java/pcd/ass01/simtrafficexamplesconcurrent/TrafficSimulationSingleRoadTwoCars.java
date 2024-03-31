package pcd.ass01.simtrafficexamplesconcurrent;

import pcd.ass01.simengineconcurrent.AbstractEnvironment;
import pcd.ass01.simengineconcurrent.AbstractSimulation;
import pcd.ass01.simtrafficbaseconcurrent.agent.CarAgent;
import pcd.ass01.simtrafficbaseconcurrent.environment.RoadsEnv;

/**
 * 
 * Traffic Simulation about 2 cars moving on a single road, no traffic lights
 * 
 */
public class TrafficSimulationSingleRoadTwoCars extends AbstractSimulation {

	public TrafficSimulationSingleRoadTwoCars() {
		super();
	}
	
	public void setup() {

		int t0 = 0;
		int dt = 1;
		
		this.setupTimings(t0, dt);
		
		AbstractEnvironment<CarAgent> env = new RoadsEnv();
		this.setupEnvironment(env);

		//TODO add the tasks to the lists

		//Road r = env.createRoad(new P2d(0,300), new P2d(1500,300));
		//CarAgent car1 = new CarAgentBasic("car-1", env, r, 0, 0.1, 0.2, 8);
		//this.addAgent(car1);		
		//CarAgent car2 = new CarAgentBasic("car-2", env, r, 100, 0.1, 0.1, 7);
		//this.addAgent(car2);
		
		/* sync with wall-time: 25 steps per sec */
		//this.syncWithTime(25);
	}	
	
}