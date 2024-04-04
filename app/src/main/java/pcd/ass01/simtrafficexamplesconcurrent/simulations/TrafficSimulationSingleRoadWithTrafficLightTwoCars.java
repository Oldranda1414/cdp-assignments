package pcd.ass01.simtrafficexamplesconcurrent.simulations;

import pcd.ass01.simengineconcurrent.AbstractEnvironment;
import pcd.ass01.simengineconcurrent.AbstractSimulation;
import pcd.ass01.simengineconcurrent.AbstractStates;
import pcd.ass01.simengineconcurrent.Task;
import pcd.ass01.simtrafficbaseconcurrent.agent.CarAgent;
import pcd.ass01.simtrafficbaseconcurrent.environment.RoadsEnv;
import pcd.ass01.simtrafficbaseconcurrent.states.CarStates;
import pcd.ass01.simtrafficbaseconcurrent.states.state.AccelerateState;
import pcd.ass01.simtrafficbaseconcurrent.states.state.ConstantSpeedState;
import pcd.ass01.simtrafficbaseconcurrent.states.state.DecelerateState;

/**
 * 
 * Traffic Simulation about 2 cars moving on a single road, with one traffic light
 * 
 */
public class TrafficSimulationSingleRoadWithTrafficLightTwoCars extends AbstractSimulation<RoadsEnv> {

	private final double seeingDistance = 30;
	private final double brakingDistance = 20;

	public TrafficSimulationSingleRoadWithTrafficLightTwoCars() {
		super();
	}
	
	public void setup() {

		int numberOfCars = 2;

		int t0 = 0;
		int dt = 1;
		
		this.setupNumberOfAgents(numberOfCars);
		this.setupTimings(t0, dt);
		
		AbstractEnvironment<CarAgent> env = new RoadsEnv(numberOfCars);
        //TODO add traffic lights
        //env.addTrafficLight(new TrafficLight(50, TrafficLight.TrafficLightState.GREEN, 75, 25, 100));
		this.setupEnvironment(env);
		AbstractStates<RoadsEnv> states = new CarStates();	
		this.setupAgentStates(states);
		for(int i = 1; i <= numberOfCars; i++){
			var id = Integer.toString(i);
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
		return this.isCloserThanFromCar(id, this.brakingDistance);
	}

	public boolean isSeeingACar(String id){
		return this.isCloserThanFromCar(id, this.seeingDistance);
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

