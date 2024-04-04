package pcd.ass01.simtrafficexamplesconcurrent.simulations;

import pcd.ass01.masterworker.Task;
import pcd.ass01.simengineconcurrent.AbstractSimulation;
import pcd.ass01.simengineconcurrent.AbstractStates;
import pcd.ass01.simtrafficbaseconcurrent.environment.RoadsEnv;
import pcd.ass01.simtrafficbaseconcurrent.states.CarStates;
import pcd.ass01.simtrafficbaseconcurrent.states.state.AccelerateState;
import pcd.ass01.simtrafficbaseconcurrent.states.state.ConstantSpeedState;
import pcd.ass01.simtrafficbaseconcurrent.states.state.DecelerateState;

/**
 * 
 * Traffic Simulation about 2 cars moving on a single road, no traffic lights
 * 
 */
public class TrafficSimulationSingleRoadTwoCars extends AbstractSimulation<RoadsEnv> {

	private static final double SEEING_DISTANCE = 30;
	private static final double BRAKING_DISTANCE = 20;

	public TrafficSimulationSingleRoadTwoCars() {
		super();
	}
	
	public void setup() {

		final double carMaxSpeed = 10;
		final double carAccelleration = 2;
		final double carDecelleration = 2;
		final double roadLenght = 1000;

		int numberOfCars = 2;

		int t0 = 0;
		int dt = 1;
		
		this.setupNumberOfAgents(numberOfCars);
		this.setupTimings(t0, dt);
		
		RoadsEnv env = new RoadsEnv(numberOfCars);
		this.setupEnvironment(env);
		AbstractStates<RoadsEnv> states = new CarStates();	
		this.setupAgentStates(states);
		var road = env.createRoad(roadLenght);
		for(int i = 1; i <= numberOfCars; i++){
			double position = i * (roadLenght/numberOfCars);
			var id = Integer.toString(i);
			env.createCar(id, road, position, carAccelleration, carDecelleration, carMaxSpeed);
			this.addSenseDecide(this.getSenseDecide(id));
			this.addAct(this.getAct(id));
		}

		/* sync with wall-time: 25 steps per sec */
		//this.syncWithTime(25);
	}	

	public Task getSenseDecide(String id){
		return new Task(() -> {
			
			
			if(isSeeingACar(id)){
				this.getAgentStates().put(id, new ConstantSpeedState());
			}
			else if(isTooCloseToCar(id)){
				this.getAgentStates().put(id, new DecelerateState());
			}
			else{
				this.getAgentStates().put(id, new AccelerateState());
			}
		},
			id, "sense-decide");
	}
	
	public Task getAct(String id){
		return new Task(() -> {
			//TODO the double id necessary seems redundant, maybe there is some way to remove id from act() parameters
			this.getAgentStates().get(id).act(id, (RoadsEnv)this.getEnvironment());	
		}, id, "act");
	}

	public boolean isTooCloseToCar(String id){
		return this.isCloserThanFromCar(id, BRAKING_DISTANCE);
	}

	public boolean isSeeingACar(String id){
		return this.isCloserThanFromCar(id, SEEING_DISTANCE);
	}

	public boolean isCloserThanFromCar(String id, double distance){
		var env = ((RoadsEnv)this.getEnvironment());
		var distanceToClosestCar = env.nearestCarInFrontDistance(id);
		if(distanceToClosestCar.isPresent()){
			if(distanceToClosestCar.get() < distance){
				return true;
			}
		}
		return false;
	}
}
