package pcd.ass01.simtrafficexamplesconcurrent.simulations;

import java.util.List;

import pcd.ass01.masterworker.Task;
import pcd.ass01.simengineconcurrent.AbstractSimulation;
import pcd.ass01.simtrafficbaseconcurrent.entity.TrafficLight;
import pcd.ass01.simtrafficbaseconcurrent.environment.RoadsEnv;
import pcd.ass01.simtrafficbaseconcurrent.states.state.AccelerateState;
import pcd.ass01.simtrafficbaseconcurrent.states.state.ConstantSpeedState;
import pcd.ass01.simtrafficbaseconcurrent.states.state.DecelerateState;

public abstract class CarSimulation extends AbstractSimulation<RoadsEnv>{
    
	protected double brakingDistance;
	protected double seeingDistance;

	protected abstract void setBrakingDistance();

	protected Task getSenseDecide(String id){
		return new Task(() -> {
			if(isSeeingACar(id)){
				this.getAgentStates().put(id, new ConstantSpeedState());
			}
			else if(isTooCloseToCar(id) || isSeeingAHaltingTrafficLight(id)){
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
			this.getAgentStates().get(id).act(id, (RoadsEnv)this.getEnvironment());	
		}, id, "act");
	}

	protected boolean isTooCloseToCar(String id){
		return this.isCloserThanFromCar(id, brakingDistance);
	}

	protected boolean isSeeingACar(String id){
		return this.isCloserThanFromCar(id, seeingDistance);
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

	protected boolean isSeeingAHaltingTrafficLight(String id){
		var env = ((RoadsEnv)this.getEnvironment());
		var car = env.get(id);
		var road = car.getRoad();
		List<TrafficLight> trafficLights = env.getTrafficLights();
		return trafficLights.stream()
			.filter(trafficLight -> {return trafficLight.getRoad() == road;})
			.filter(trafficLight -> {return trafficLight.isRed() || trafficLight.isYellow();})
			.map(trafficLight -> {return trafficLight.getCurrentPosition() < car.getCurrentPosition() ? trafficLight.getCurrentPosition() + road.getLen() : trafficLight.getCurrentPosition();})
			.anyMatch(trafficLightPos -> {return (trafficLightPos - car.getCurrentPosition()) < brakingDistance;});
	}
}
