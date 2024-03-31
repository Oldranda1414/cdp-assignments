package pcd.ass01.simtrafficbase.environment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import pcd.ass01.simengineconcurrent.AbstractEnvironment;
import pcd.ass01.simengineconcurrent.RWBuffer;
import pcd.ass01.simengineseq.Action;
import pcd.ass01.simengineseq.Percept;
import pcd.ass01.simtrafficbase.P2d;
import pcd.ass01.simtrafficbase.TrafficLight;
import pcd.ass01.simtrafficbase.TrafficLight.TrafficLightState;
import pcd.ass01.simtrafficbase.agent.CarAgent;

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
			this.put(car.getId(), car);
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
