package simtrafficexamples.simulations;

import java.util.List;
import java.util.Optional;

import akka.actor.typed.ActorRef;
import akka.actor.typed.javadsl.ActorContext;

import executor.Task;
import simengine.AbstractSimulation;
import simtrafficbase.entity.CarAgent;
import simtrafficbase.entity.TrafficLight;
import simtrafficbase.environment.Road;
import simtrafficbase.environment.RoadsEnv;
import simtrafficbase.states.state.AccelerateState;
import simtrafficbase.states.state.ConstantSpeedState;
import simtrafficbase.states.state.DecelerateState;
import utils.Command;

public abstract class CarSimulation<T extends AbstractSimulation<RoadsEnv, T>> extends AbstractSimulation<RoadsEnv, T> {

	protected double brakingDistance;
	protected double seeingDistance;

	protected CarSimulation(ActorContext<Command> context, int numSteps, List<ActorRef<Command>> listeners) {
		super(context, numSteps, listeners);
	}

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

	protected Task getAct(String id) {
		return new Task(() -> {
			this.getAgentStates().get(id).act(id, (RoadsEnv) this.getEnvironment());
		}, id, "act");
	}

	protected boolean isTooCloseToCar(String id) {
		return this.isCloserThanFromCar(id, brakingDistance);
	}

	protected boolean isSeeingACar(String id) {
		return this.isCloserThanFromCar(id, seeingDistance);
	}

	protected boolean isCloserThanFromCar(String id, double distance) {
		RoadsEnv env = ((RoadsEnv) this.getEnvironment());
		Optional<Double> distanceToClosestCar = env.nearestCarInFrontDistance(id);
		if (distanceToClosestCar.isPresent()) {
			if (distanceToClosestCar.get() < distance) {
				return true;
			}
		}
		return false;
	}

	protected boolean isSeeingAHaltingTrafficLight(String id) {
		RoadsEnv env = ((RoadsEnv) this.getEnvironment());
		CarAgent car = env.get(id);
		Road road = car.getRoad();
		List<TrafficLight> trafficLights = env.getTrafficLights();
		return trafficLights.stream()
				.filter(trafficLight -> trafficLight.getRoad() == road)
				.filter(trafficLight -> trafficLight.isRed() || trafficLight.isYellow())
				.map(trafficLight -> trafficLight.getCurrentPosition()
						+ (trafficLight.getCurrentPosition() < car.getCurrentPosition() ? road.getLen() : 0))
				.anyMatch(trafficLightPos -> (trafficLightPos - car.getCurrentPosition()) < brakingDistance);
	}
}
