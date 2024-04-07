package pcd.ass01.simtrafficbaseconcurrent.environment;

import java.util.ArrayList;
import java.util.List;

import java.util.Optional;

import pcd.ass01.simengineconcurrent.AbstractEnvironment;
import pcd.ass01.simtrafficbaseconcurrent.P2d;
import pcd.ass01.simtrafficbaseconcurrent.entity.TrafficLight;
import pcd.ass01.simtrafficbaseconcurrent.entity.CarAgent;

public class RoadsEnv extends AbstractEnvironment<CarAgent>{

	private static final int MIN_DIST_ALLOWED = 15;
	
	/* list of roads */
	private List<Road> roads;

	/* traffic lights */
	private List<TrafficLight> trafficLights;

	public RoadsEnv() {
		trafficLights = new ArrayList<>();
		roads = new ArrayList<>();
	}
	
	@Override
	public void step(int dt) {
		for(TrafficLight tf : this.trafficLights) {
			tf.step(dt);
		}
	}

	public CarAgent createCar(String id, Road road, double position, double accelleration, double decelleration, double maxSpeed){
		if (position > road.getLen() || position <= 0) {
			throw new IllegalArgumentException("The value " + position + " is not valid. You must pass a position that is more than or equals to 0 and less than the road length.");
		}
		CarAgent car = new CarAgent(id, road, position, accelleration, decelleration, maxSpeed);
		super.put(id, car);
		return car;
	}

	public Road createRoad(P2d from, P2d to) {
		Road r = new Road(from, to);
		this.roads.add(r);
		return r;
	}

	public TrafficLight createTrafficLight(double pos, Road road, TrafficLight.TrafficLightState initialState, int greenDuration, int yellowDuration, int redDuration) {
		TrafficLight tl = new TrafficLight(pos, road, initialState, greenDuration, yellowDuration, redDuration);
		this.trafficLights.add(tl);
		return tl;
	}

	public void updateCar(String id, int decision){
		CarAgent car = super.get(id);
		takeCarDecision(car, decision);
		moveCar(car);
	}

	//TODO decision should be an enum
	private void takeCarDecision(CarAgent car, int decision){ 
		if(decision < 0){
			decelerateCar(car);
		}
		else if(decision > 0){
			accelerateCar(car);
		}
	}

	private void decelerateCar(CarAgent car){
		double currentSpeed = car.getCurrentSpeed();
		double delta = car.getDeceleration();
		double newSpeed = currentSpeed + (delta * -1);
		changeCarSpeed(car, newSpeed);	
	}

	private void accelerateCar(CarAgent car){
		double currentSpeed = car.getCurrentSpeed();
		double newSpeed = currentSpeed + car.getAcceleration();
		changeCarSpeed(car, newSpeed);	
	}

	private void changeCarSpeed(CarAgent car, double newSpeed){
		double maxSpeed = car.getMaxSpeed();
		if(newSpeed > maxSpeed){
			car.setCurrentSpeed(maxSpeed);
		}
		else if(newSpeed < 0){
			car.setCurrentSpeed(0);
		}
		else{
			car.setCurrentSpeed(newSpeed);
		}
	}

	/**
	 * moves a car(agent) to position if possible
	 * @param id
	 * @param position
	 */
	private void moveCar(CarAgent car){
		double currentSpeed = car.getCurrentSpeed();
		double currentPosition = car.getCurrentPosition();
		double position = (currentPosition + currentSpeed) % car.getRoad().getLen();
		if(canMove(car.getId(), position)){
			car.setCurrentPosition(position);
		}
	}

	public boolean canMove(String id, double position){
		CarAgent car = super.get(id);
		return !super.entrySet().stream()
			.filter(couple -> id != couple.getKey())
			.map(couple -> couple.getValue())
			.anyMatch(agent -> Math.abs(agent.getCurrentPosition() - position) < MIN_DIST_ALLOWED) && position < car.getRoad().getLen();
	}

	public Optional<Double> nearestCarInFrontDistance(String id){
		CarAgent currentCar = super.get(id);
		double currentPosition = currentCar.getCurrentPosition();
		Road currentRoad = currentCar.getRoad();
		return super.values().stream()
			.filter(car -> {return car != currentCar && car.getRoad() == currentRoad;})
			.map(car -> {return car.getCurrentPosition() + currentRoad.getLen();})
			.map(carPos -> {return carPos - currentPosition;})
			.reduce((dist1, dist2) -> {return dist1 < dist2 ? dist2 : dist1;});
	}
 
	public List<CarAgent> getAgentInfo() {
		return new ArrayList<>(super.values());
	}

	public List<Road> getRoads(){
		return roads;
	}
	
	public List<TrafficLight> getTrafficLights(){
		return trafficLights;
	}

}
