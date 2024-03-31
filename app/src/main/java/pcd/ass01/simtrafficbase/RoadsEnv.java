package pcd.ass01.simtrafficbase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import pcd.ass01.simengineconcurrent.AbstractEnvironment;
import pcd.ass01.simengineseq.Action;
import pcd.ass01.simengineseq.Percept;

public class RoadsEnv extends AbstractEnvironment {
		
	private static final int ROAD_LENGHT = 100; 
	private static final int MIN_DIST_ALLOWED = 5;
	
	/* list of roads */
	private List<Road> roads;

	/* traffic lights */
	private List<TrafficLight> trafficLights;


	public RoadsEnv() {
		this.agents = new HashMap<>();	
		trafficLights = new ArrayList<>(); //unused for now
		roads = new ArrayList<>();
	}
	
	@Override
	public void setup() {
		var road = this.createRoad(ROAD_LENGHT);
		//TODO setup traffic lights
		for(int i = 1; i > 0; i++){
			var car = new CarAgent(/*TODO FIX CAR AGENT AND STUFF (CONCRETE ABSTRACT AGENT) */);
			this.registerNewCar(car);
		}
	}
	
	@Override
	public void step(int dt) {
		//TODO step for traffic lights
	}
	
	public void registerNewCar(CarAgent car) {
		agents.put(car.getId(), car);
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
