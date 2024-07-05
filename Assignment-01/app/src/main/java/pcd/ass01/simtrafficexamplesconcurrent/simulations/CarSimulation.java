package pcd.ass01.simtrafficexamplesconcurrent.simulations;

import java.util.List;
import java.util.Optional;

import pcd.ass01.masterworker.Task;
import pcd.ass01.simengineconcurrent.AbstractSimulation;
import pcd.ass01.simtrafficbaseconcurrent.entity.CarAgent;
import pcd.ass01.simtrafficbaseconcurrent.entity.TrafficLight;
import pcd.ass01.simtrafficbaseconcurrent.environment.Road;
import pcd.ass01.simtrafficbaseconcurrent.environment.RoadsEnv;
import pcd.ass01.simtrafficbaseconcurrent.states.state.AccelerateState;
import pcd.ass01.simtrafficbaseconcurrent.states.state.ConstantSpeedState;
import pcd.ass01.simtrafficbaseconcurrent.states.state.DecelerateState;

public abstract class CarSimulation extends AbstractSimulation<RoadsEnv>{
    
	protected double brakingDistance;
	protected double seeingDistance;

	protected abstract void setDistances(double brakingDistance);

	protected Task getSenseDecide(String id) {
		return new Task(() -> {
			CarAgent car = ((CarAgent) getEnvironment().get(id));
			if (isTooCloseToCar(id) || isSeeingAHaltingTrafficLight(id)) {
				if (car.getCurrentSpeed() < car.getDeceleration()) {
					car.setCurrentSpeed(0);
					this.getAgentStates().put(id, new ConstantSpeedState());
				} else {
					this.getAgentStates().put(id, new DecelerateState());
				}
			} else if (isSeeingACar(id)) {
				this.getAgentStates().put(id, new ConstantSpeedState());
			} else {
				if (car.getCurrentSpeed() >= car.getMaxSpeed()) {
					this.getAgentStates().put(id, new ConstantSpeedState());
				} else {
					this.getAgentStates().put(id, new AccelerateState());
				}
			}
		}, id, "sense-decide");
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
		RoadsEnv env = ((RoadsEnv)this.getEnvironment());
		Optional<Double> distanceToClosestCar = env.nearestCarInFrontDistance(id);
		if(distanceToClosestCar.isPresent()){
			if(distanceToClosestCar.get() < distance){
				return true;
			}
		}
		return false;
	}

	protected boolean isSeeingAHaltingTrafficLight(String id){
		RoadsEnv env = ((RoadsEnv)this.getEnvironment());
		CarAgent car = env.get(id);
		Road road = car.getRoad();
		List<TrafficLight> trafficLights = env.getTrafficLights();
		return trafficLights.stream()
			.filter(trafficLight -> trafficLight.getRoad() == road)
			.filter(trafficLight -> trafficLight.isRed() || trafficLight.isYellow())
			.map(trafficLight -> trafficLight.getCurrentPosition() + (trafficLight.getCurrentPosition() < car.getCurrentPosition() ? road.getLen() : 0))
			.anyMatch(trafficLightPos -> (trafficLightPos - car.getCurrentPosition()) < brakingDistance);
	}
}
