package simtrafficexamples;

import simtrafficexamples.simulations.*;

/**
 * 
 * Main class to create and run a simulation with a massive number of cars
 * 
 */
public class RunMassiveTrafficSimulation {

	public static void main(String[] args) {

		final int nSteps = 100;

        TrafficsimulationSingleRoadMassiveNumberOfCars simulation = new TrafficsimulationSingleRoadMassiveNumberOfCars();
		simulation.setup();
		
		try {
			simulation.resume();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		simulation.run(nSteps);

		long d = simulation.getSimulationDuration();
		log("Completed in " + d + " ms - average time per step: " + simulation.getAverageTimePerCycle() + " ms");
	}

	private static void log(String msg) {
		System.out.println("[ SIMULATION ] " + msg);
	}
}


