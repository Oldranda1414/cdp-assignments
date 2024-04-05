package pcd.ass01.simtrafficexamplesconcurrent.part3;

import java.util.Random;

import pcd.ass01.simtrafficexamplesconcurrent.RoadSimStatistics;
import pcd.ass01.simtrafficexamplesconcurrent.RoadSimView;
import pcd.ass01.simtrafficexamplesconcurrent.simulations.*;

/**
 * 
 * Main class to create and run a simulation
 * 
 */
public class RunRandomTrafficSimulation {

	public static void main(String[] args) {

		final int nSteps = 10_000;

        var simulation = new TrafficSimulationWithRandom(new Random(37));
		simulation.setup();
		
		RoadSimView view = new RoadSimView(simulation);
		RoadSimStatistics stat = new RoadSimStatistics();
		view.display();
		
		simulation.addSimulationListener(stat);
		simulation.addSimulationListener(view);
		simulation.run(nSteps);
	}
}

