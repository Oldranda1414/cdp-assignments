package simtrafficexamples;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import simengine.AbstractSimulation;
import simtrafficexamples.listeners.RoadSimStatistics;
import simtrafficexamples.simulations.*;

import java.util.ArrayList;
import java.util.List;

import utils.Command;

/**
 * 
 * Main class to create and run a simulation with a massive number of cars
 * 
 */
public class RunMassiveTrafficSimulation extends AbstractBehavior<RunMassiveTrafficSimulation.StartSimulation> {

	public static record StartSimulation() {}

	public static void main(String[] args) {
		
		final ActorSystem<StartSimulation> system = ActorSystem.create(RunMassiveTrafficSimulation.create(), "SimulationSystem");
		system.tell(new RunMassiveTrafficSimulation.StartSimulation());

		// long d = simulation.getSimulationDuration();
		// log("Completed in " + d + " ms - average time per step: " + simulation.getAverageTimePerCycle() + " ms");
	}

	public static Behavior<StartSimulation> create() {
		return Behaviors.setup(RunMassiveTrafficSimulation::new);
	}

	private RunMassiveTrafficSimulation(ActorContext<StartSimulation> context) {
		super(context);
	}

	@Override
	public Receive<StartSimulation> createReceive() {
		return newReceiveBuilder().onMessage(StartSimulation.class, this::onStartSimulation).build();
	}

	private Behavior<StartSimulation> onStartSimulation(StartSimulation command) {
		final int nSteps = 100;
		final List<ActorRef<Command>> listeners = new ArrayList<>();
		listeners.add(getContext().spawn(RoadSimStatistics.create(), "Simulation Statistics"));
		final ActorRef<Command> simulation = getContext()
			.spawn(AbstractSimulation.create(TrafficsimulationSingleRoadMassiveNumberOfCars.class, nSteps, listeners), "Simulation with cross roads");
		simulation.tell(new AbstractSimulation.NextStep());
		return this;
	}
}


