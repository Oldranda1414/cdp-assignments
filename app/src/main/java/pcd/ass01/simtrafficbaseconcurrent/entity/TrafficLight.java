package pcd.ass01.simtrafficbaseconcurrent.entity;

import pcd.ass01.simtrafficbaseconcurrent.environment.Road;

public class TrafficLight implements RoadEntity{
	public static enum TrafficLightState {GREEN, YELLOW, RED}
	private TrafficLightState state;
	private int currentTimeInState;
	private int redDuration, greenDuration, yellowDuration;
	private double position;
    private Road road;
	
	public TrafficLight(double pos, Road road, TrafficLightState initialState, int greenDuration, int yellowDuration, int redDuration) {
		this.redDuration = redDuration;
		this.greenDuration = greenDuration;
		this.yellowDuration = yellowDuration;
		this.position = pos;
        this.road = road;
		state = initialState;
		currentTimeInState = 0;
	}

	public void step(int dt) {
		switch (state) {
		case GREEN: 
			currentTimeInState += dt;
			if (currentTimeInState >= greenDuration) {
				state = TrafficLightState.YELLOW; 
				currentTimeInState = 0;
			}
			break;
		case RED: 
			currentTimeInState += dt;
			if (currentTimeInState >= redDuration) {
				state = TrafficLightState.GREEN; 
				currentTimeInState = 0;
			}
			break;
		case YELLOW: 
			currentTimeInState += dt;
			if (currentTimeInState >= yellowDuration) {
				state = TrafficLightState.RED; 
				currentTimeInState = 0;
			}
			break;
		default:
			break;
		}
	}
	
	public boolean isGreen() {
		return state.equals(TrafficLightState.GREEN);
	}
	
	public boolean isRed() {
		return state.equals(TrafficLightState.RED);
	}

	public boolean isYellow() {
		return state.equals(TrafficLightState.YELLOW);
	}
	
    @Override
	public double getCurrentPosition() {
		return position;
	}

    @Override
    public Road getRoad() {
        return road;
    }

}
