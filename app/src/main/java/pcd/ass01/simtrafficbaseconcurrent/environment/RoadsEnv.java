package pcd.ass01.simtrafficbaseconcurrent.environment;

import java.util.ArrayList;
import java.util.List;

import pcd.ass01.simengineconcurrent.AbstractEnvironment;
import pcd.ass01.simengineconcurrent.AgentState;
import pcd.ass01.simtrafficbaseconcurrent.P2d;
import pcd.ass01.simtrafficbaseconcurrent.TrafficLight;
import pcd.ass01.simtrafficbaseconcurrent.agent.CarAgent;

public class RoadsEnv extends AbstractEnvironment<CarAgent>{
		
	private static final int ROAD_LENGHT = 100; 
	private static final int MIN_DIST_ALLOWED = 5;
	private static final double CAR_MAX_SPEED = 10;
	private static final double CAR_ACCELLERATION = 2;
	private static final double CAR_DECELLERATION = 2;
	private static final double NUMBER_OF_CARS = 2;
	
	/* list of roads */
	private List<Road> roads;

	/* traffic lights */
	private List<TrafficLight> trafficLights;


	public RoadsEnv() {
		trafficLights = new ArrayList<>(); //unused for now
		roads = new ArrayList<>();
	}
	
	@Override
	public void setup() {
		var road = this.createRoad(ROAD_LENGHT);
		//TODO setup traffic lights
		for(int i = 1; i > 0; i++){
			double position = i * (ROAD_LENGHT/NUMBER_OF_CARS);
			var car = new CarAgent(Integer.toString(i), road, position, 
				CAR_ACCELLERATION, 
				CAR_DECELLERATION, 
				CAR_MAX_SPEED);
			this.agents.put(car.getId(), car);
		}
	}
	
	@Override
	public void step(int dt) {
		//TODO step for traffic lights
	}

	public Road createRoad(int lenght) {
		Road r = new Road(lenght);
		this.roads.add(r);
		return r;
	}

	public TrafficLight createTrafficLight(P2d pos, TrafficLight.TrafficLightState initialState, int greenDuration, int yellowDuration, int redDuration) {
		TrafficLight tl = new TrafficLight(pos, initialState, greenDuration, yellowDuration, redDuration);
		this.trafficLights.add(tl);
		return tl;
	}

	public void updateAgent(String id, int decision){
		changeCarSpeed(id, decision);
		moveCar(id);
	}

	private void changeCarSpeed(String id, int decision){ //TODO decision should be an enum or something of the sorts
		var speed = this.agents.get(id).getCurrentSpeed();
		var acceleration = this.agents.get(id).getAcceleration();
		var newSpeed = speed + (acceleration * Math.signum(decision));
		this.agents.get(id).setCurrentSpeed(newSpeed);
	}

	/**
	 * moves a car(agent) to position if possible
	 * @param id
	 * @param position
	 */
	private void moveCar(String id){
		var currentSpeed = this.agents.get(id).getCurrentSpeed();
		var currentPosition = this.agents.get(id).getCurrentPosition();
		var position = currentPosition + currentSpeed;
		if(canMove(id, position)){
			this.agents.get(id).setCurrentPosition(position);
		}
	}

	public boolean canMove(String id, double position){
		return !this.agents.entrySet().stream()
			.filter(couple -> id != couple.getKey())
			.map(couple -> couple.getValue())
			.anyMatch(agent -> Math.abs(agent.getCurrentPosition() - position) < MIN_DIST_ALLOWED) && position < ROAD_LENGHT;
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
