package pcd.ass01.simtrafficbaseconcurrent.environment;

import java.util.ArrayList;
import java.util.List;

import java.util.Optional;

import pcd.ass01.simengineconcurrent.AbstractEnvironment;
import pcd.ass01.simtrafficbaseconcurrent.P2d;
import pcd.ass01.simtrafficbaseconcurrent.TrafficLight;
import pcd.ass01.simtrafficbaseconcurrent.agent.CarAgent;

public class RoadsEnv extends AbstractEnvironment<CarAgent>{
		
	private static final int ROAD_LENGHT = 100; 
	private static final int MIN_DIST_ALLOWED = 5;
	
	/* list of roads */
	private List<Road> roads;

	/* traffic lights */
	private List<TrafficLight> trafficLights;


	public RoadsEnv(int numberOfAgents) {
		trafficLights = new ArrayList<>(); //unused for now
		roads = new ArrayList<>();
	}
	
	@Override
	public void step(int dt) {
		//TODO step for traffic lights
	}

	public CarAgent createCar(String id, Road road, double position, double accelleration, double decelleration, double maxSpeed){
		var car = new CarAgent(id, road, position, accelleration, decelleration, maxSpeed);
		this.map.put(id, car);
		return car;
	}

	public Road createRoad(double lenght) {
		Road r = new Road(lenght);
		this.roads.add(r);
		return r;
	}

	public TrafficLight createTrafficLight(P2d pos, TrafficLight.TrafficLightState initialState, int greenDuration, int yellowDuration, int redDuration) {
		TrafficLight tl = new TrafficLight(pos, initialState, greenDuration, yellowDuration, redDuration);
		this.trafficLights.add(tl);
		return tl;
	}

	public void updateCar(String id, int decision){
		var car = this.map.get(id);
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
		var currentSpeed = car.getCurrentSpeed();
		var delta = car.getDeceleration();
		var newSpeed = currentSpeed + (delta * -1);
		changeCarSpeed(car, newSpeed);	
	}

	private void accelerateCar(CarAgent car){
		var currentSpeed = car.getCurrentSpeed();
		var delta = car.getAcceleration();
		var newSpeed = currentSpeed + delta;
		changeCarSpeed(car, newSpeed);	
	}

	private void changeCarSpeed(CarAgent car, double newSpeed){
		var maxSpeed = car.getMaxSpeed();
		if(newSpeed > maxSpeed){
			car.setCurrentSpeed(maxSpeed);
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
		var currentSpeed = car.getCurrentSpeed();
		var currentPosition = car.getCurrentPosition();
		var position = currentPosition + currentSpeed;
		if(canMove(car.getId(), position)){
			car.setCurrentPosition(position);
		}
	}

	public boolean canMove(String id, double position){
		return !this.map.entrySet().stream()
			.filter(couple -> id != couple.getKey())
			.map(couple -> couple.getValue())
			.anyMatch(agent -> Math.abs(agent.getCurrentPosition() - position) < MIN_DIST_ALLOWED) && position < ROAD_LENGHT;
	}

	//TODO maybe this should be redone with steams as with canMove()
	public Optional<Double> nearestCarInFrontDistance(String id){
		var currentCar = this.map.get(id);
		var currentPosition = currentCar.getCurrentPosition();
		var currentRoad = currentCar.getRoad();
		double minDist = Double.POSITIVE_INFINITY;
		for(var car : this.map.values()){
			//check if this car is not the current car
			if(car != currentCar){
				var carRoad = car.getRoad();

				//check if this car and the current car are on the same road
				if(carRoad == currentRoad){
					var carPosition = car.getCurrentPosition();

					//adjust because of pacman effect
					if(carPosition < currentPosition) carPosition =+ currentRoad.getLen();
	
					var currentDistance = carPosition - currentPosition;
	
					//if the distance from this car to the current car is the min found until now, update the min
					if(currentDistance < minDist) minDist = currentDistance;
				}
			}
		}

		return (minDist == Double.POSITIVE_INFINITY)? Optional.empty(): Optional.of(minDist);
	}

	/* 
	public List<CarAgent> getAgentInfo(){
		return this.registeredCars.entrySet().stream().map(el -> el.getValue()).toList();
	}
	*/
	public List<Road> getRoads(){
		return roads;
	}
	
	public List<TrafficLight> getTrafficLights(){
		return trafficLights;
	}

}
