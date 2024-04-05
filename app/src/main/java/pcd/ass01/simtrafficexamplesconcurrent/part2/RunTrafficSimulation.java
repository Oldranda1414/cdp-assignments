package pcd.ass01.simtrafficexamplesconcurrent.part2;

import pcd.ass01.simtrafficexamplesconcurrent.RoadSimStatistics;
import pcd.ass01.simtrafficexamplesconcurrent.RoadSimView;
import pcd.ass01.simtrafficexamplesconcurrent.simulations.*;

/**
 * 
 * Main class to create and run a simulation
 * 
 */
public class RunTrafficSimulation {

	public static void main(String[] args) {

		// final int nSteps = 10_000;

		var simulation = new TrafficSimulationSingleRoadTwoCars();
		//var simulation = new TrafficSimulationSingleRoadSeveralCars();
		//var simulation = new TrafficSimulationSingleRoadWithTrafficLightTwoCars();
		// var simulation = new TrafficSimulationWithCrossRoads();
		simulation.setup();
		
		RoadSimView view = new RoadSimView(simulation);
		RoadSimStatistics stat = new RoadSimStatistics();
		view.display();
		
		simulation.addSimulationListener(stat);
		simulation.addSimulationListener(view);
		// simulation.run(nSteps);
	}
}
