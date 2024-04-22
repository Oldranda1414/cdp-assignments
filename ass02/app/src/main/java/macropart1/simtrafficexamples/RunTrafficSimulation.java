package macropart1.simtrafficexamples;

import macropart1.simtrafficexamples.listeners.RoadSimStatistics;
import macropart1.simtrafficexamples.listeners.RoadSimView;
import macropart1.simtrafficexamples.simulations.*;

/**
 * 
 * Main class to create and run a simulation
 * 
 */
public class RunTrafficSimulation {

	public static void main(String[] args) throws InterruptedException {

		// final int nSteps = 100;
		// TrafficSimulationSingleRoadTwoCars simulation = new TrafficSimulationSingleRoadTwoCars();
		//var simulation = new TrafficSimulationSingleRoadSeveralCars();
		// TrafficSimulationSingleRoadWithTrafficLightTwoCars simulation = new TrafficSimulationSingleRoadWithTrafficLightTwoCars();
		TrafficSimulationWithCrossRoads simulation = new TrafficSimulationWithCrossRoads();
		simulation.setup();
		RoadSimView view = new RoadSimView(simulation);
		RoadSimStatistics stat = new RoadSimStatistics();
		view.display();
		
		simulation.addSimulationListener(stat);
		simulation.addSimulationListener(view);
		// simulation.run(nSteps);
	}
}
