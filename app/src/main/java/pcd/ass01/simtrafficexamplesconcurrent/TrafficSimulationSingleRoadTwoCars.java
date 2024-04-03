package pcd.ass01.simtrafficexamplesconcurrent;

import pcd.ass01.simengineconcurrent.AbstractEnvironment;
import pcd.ass01.simengineconcurrent.AbstractSimulation;
import pcd.ass01.simengineconcurrent.AbstractStates;
import pcd.ass01.simtrafficbaseconcurrent.agent.CarAgent;
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

	private final double seeingDistance = 30;
	private final double brakingDistance = 20;

	public TrafficSimulationSingleRoadTwoCars() {
		super();
	}
	
	public void setup() {

		int numberOfAgents = 2;

		int t0 = 0;
		int dt = 1;
		
		this.setupNumberOfAgents(numberOfAgents);
		this.setupTimings(t0, dt);
		
		AbstractEnvironment<CarAgent> env = new RoadsEnv(numberOfAgents);
		env.setup();
		this.setupEnvironment(env);
		AbstractStates<RoadsEnv> states = new CarStates();	
		this.setupAgentStates(states);
		for(int i = 1; i <= numberOfAgents; i++){
			var id = Integer.toString(i);
			this.addSenseDecide(this.getSenseDecide(id));
			this.addAct(this.getAct(id));
		}

		//Road r = env.createRoad(new P2d(0,300), new P2d(1500,300));
		//CarAgent car1 = new CarAgentBasic("car-1", env, r, 0, 0.1, 0.2, 8);
		//this.addAgent(car1);		
		//CarAgent car2 = new CarAgentBasic("car-2", env, r, 100, 0.1, 0.1, 7);
		//this.addAgent(car2);
		
		/* sync with wall-time: 25 steps per sec */
		//this.syncWithTime(25);
	}	

	public Runnable getSenseDecide(String id){
		return () -> {
			
			
			if(isSeeingACar(id)){
				this.getAgentStates().put(id, new ConstantSpeedState());
			}
			else if(isTooCloseToCar(id)){
				this.getAgentStates().put(id, new DecelerateState());
			}
			else{
				this.getAgentStates().put(id, new AccelerateState());
			}
		};
	}
	
	public Runnable getAct(String id){
		return () -> {
			//TODO the double id necessary seems redundant, maybe there is some way to remove id from act() parameters
			this.getAgentStates().get(id).act(id, (RoadsEnv)this.getEnvironment());	
		};
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
