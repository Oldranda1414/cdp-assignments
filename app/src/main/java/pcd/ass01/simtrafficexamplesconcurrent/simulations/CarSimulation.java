package pcd.ass01.simtrafficexamplesconcurrent.simulations;

import pcd.ass01.masterworker.Task;
import pcd.ass01.simengineconcurrent.AbstractSimulation;
import pcd.ass01.simtrafficbaseconcurrent.environment.RoadsEnv;
import pcd.ass01.simtrafficbaseconcurrent.states.state.AccelerateState;
import pcd.ass01.simtrafficbaseconcurrent.states.state.ConstantSpeedState;
import pcd.ass01.simtrafficbaseconcurrent.states.state.DecelerateState;

public abstract class CarSimulation extends AbstractSimulation<RoadsEnv>{
    
	private static final double SEEING_DISTANCE = 30;
	private static final double BRAKING_DISTANCE = 20;

	protected Task getSenseDecide(String id){
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
	
	protected Task getAct(String id){
		return new Task(() -> {
			//TODO the double id necessary seems redundant, maybe there is some way to remove id from act() parameters
			this.getAgentStates().get(id).act(id, (RoadsEnv)this.getEnvironment());	
		}, id, "act");
	}

	protected boolean isTooCloseToCar(String id){
		return this.isCloserThanFromCar(id, BRAKING_DISTANCE);
	}

	protected boolean isSeeingACar(String id){
		return this.isCloserThanFromCar(id, SEEING_DISTANCE);
	}

	protected boolean isCloserThanFromCar(String id, double distance){
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
