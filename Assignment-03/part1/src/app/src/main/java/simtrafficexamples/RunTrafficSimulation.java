package simtrafficexamples;

import simtrafficexamples.listeners.RoadSimStatistics;
import simtrafficexamples.simulations.*;
import simengine.AbstractSimulation;

import java.util.ArrayList;
import java.util.List;

import actor.Command;
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

	public static void main(String[] args) throws InterruptedException {
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
		final int nSteps = 100;
		final List<ActorRef<Command>> listeners = new ArrayList<>();
		listeners.add(getContext().spawn(RoadSimStatistics.create(), "Simulation Statistics"));
		final ActorRef<Command> simulation = getContext()
			.spawn(AbstractSimulation.create(TrafficSimulationWithCrossRoads.class, nSteps, listeners), "Simulation with cross roads");
		simulation.tell(new AbstractSimulation.NextStep());
		return this;
	}
}
