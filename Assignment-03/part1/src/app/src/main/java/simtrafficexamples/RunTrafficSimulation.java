package simtrafficexamples;

import simtrafficexamples.listeners.RoadSimStatistics;
import simtrafficexamples.simulations.*;
import simengine.SimulationListener.ViewUpdate;

import java.util.ArrayList;
import java.util.List;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

/**
 * 
 * Main class to create and run a simulation
 * 
 */
public class RunTrafficSimulation extends AbstractBehavior<RunTrafficSimulation.StartSimulation> {

	public static record StartSimulation() {}

	private final List<ActorRef<ViewUpdate>> listeners = new ArrayList<>();

	public static void main(String[] args) throws InterruptedException {

		final int nSteps = 100;
		// TrafficSimulationSingleRoadTwoCars simulation = new TrafficSimulationSingleRoadTwoCars();
		//var simulation = new TrafficSimulationSingleRoadSeveralCars();
		// TrafficSimulationSingleRoadWithTrafficLightTwoCars simulation = new TrafficSimulationSingleRoadWithTrafficLightTwoCars();
		TrafficSimulationWithCrossRoads simulation = new TrafficSimulationWithCrossRoads();
		simulation.setup();
		
		
		simulation.run(nSteps);

		final ActorSystem<StartSimulation> system = ActorSystem.create(RunTrafficSimulation.create(), "SimulationSystem");
		system.tell(new RunTrafficSimulation.StartSimulation());
	}

	public static Behavior<StartSimulation> create() {
		return Behaviors.setup(RunTrafficSimulation::new);
	}

	private RunTrafficSimulation(ActorContext<StartSimulation> context) {
		super(context);
	}

	@Override
	public Receive<StartSimulation> createReceive() {
		return newReceiveBuilder().onMessage(StartSimulation.class, this::onStartSimulation).build();
	}

	private Behavior<StartSimulation> onStartSimulation(StartSimulation command) {
		listeners.add(getContext().spawn(RoadSimStatistics.create(), "Simulation Statistics"));
		return this;
	}
}
