package simtrafficexamples;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import simengine.AbstractSimulation;
import simtrafficexamples.simulations.*;

import java.util.List;

import actor.Command;

/**
 * 
 * Main class to create and run a simulation with a massive number of cars
 * 
 */
public class RunMassiveTrafficSimulation extends AbstractBehavior<Command> {

	public static record StartSimulation() implements Command {}

	public static void main(String[] args) {
		
		final ActorSystem<Command> system = ActorSystem.create(RunMassiveTrafficSimulation.create(), "SimulationSystem");
		system.tell(new StartSimulation());

		// long d = simulation.getSimulationDuration();
		// log("Completed in " + d + " ms - average time per step: " + simulation.getAverageTimePerCycle() + " ms");
	}

	public static Behavior<Command> create() {
		return Behaviors.setup(RunMassiveTrafficSimulation::new);
	}

	private RunMassiveTrafficSimulation(ActorContext<Command> context) {
		super(context);
	}

	@Override
	public Receive<Command> createReceive() {
		return newReceiveBuilder().onMessage(StartSimulation.class, this::onStartSimulation).build();
	}

	private Behavior<Command> onStartSimulation(StartSimulation command) {
		final int nSteps = 100;
		final ActorRef<Command> simulation = getContext()
			.spawn(
				AbstractSimulation
					.create(TrafficsimulationSingleRoadMassiveNumberOfCars.class, List.of()), "Single-road-massive-number-of-cars");
		simulation.tell(new AbstractSimulation.NextStep(nSteps));
		return this;
	}
}


