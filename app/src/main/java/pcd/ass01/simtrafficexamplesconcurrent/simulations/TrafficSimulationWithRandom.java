package pcd.ass01.simtrafficexamplesconcurrent.simulations;

import java.util.Random;

import pcd.ass01.simengineconcurrent.AbstractStates;
import pcd.ass01.simtrafficbaseconcurrent.P2d;
import pcd.ass01.simtrafficbaseconcurrent.entity.TrafficLight;
import pcd.ass01.simtrafficbaseconcurrent.environment.RoadsEnv;
import pcd.ass01.simtrafficbaseconcurrent.states.CarStates;
import pcd.ass01.utils.Pair;

/**
 * 
 * Traffic Simulation with a single road and random cars properties
 * 
 */
public class TrafficSimulationWithRandom extends CarSimulation{

    private Random random;

	public TrafficSimulationWithRandom(Random random) {
        this.random = random;
		this.setDistances(random.nextInt(10, 900));
	}

	@Override
	protected void setDistances(double breakingDistance) {
		this.brakingDistance = breakingDistance;
		this.seeingDistance = brakingDistance + 10;
	}
	
	public void setup() {

		final Pair<P2d, P2d> roadPoints = new Pair<>(new P2d(0, 300), new P2d(1000, 300));

		int numberOfCars = random.nextInt(1, 20);

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
            var carMaxSpeed = random.nextDouble(1, 50);
            var carAccelleration = random.nextDouble(0.01, carMaxSpeed);
            var carDecelleration = random.nextDouble(carMaxSpeed/seeingDistance, carMaxSpeed);
			env.createCar(id, road, position, carAccelleration, carDecelleration, carMaxSpeed);
			this.addSenseDecide(this.getSenseDecide(id));
			this.addAct(this.getAct(id));
		}

		/* sync with wall-time: 25 steps per sec */
		this.syncWithTime(25);
	}	

}