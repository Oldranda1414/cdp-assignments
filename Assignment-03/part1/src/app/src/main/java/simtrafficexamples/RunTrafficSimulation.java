package simtrafficexamples;

import simtrafficexamples.listeners.RoadSimStatistics;
import simtrafficexamples.listeners.RoadSimView;
import simtrafficexamples.simulations.*;
import simengine.AbstractSimulation;
import actor.Command;

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
public class RunTrafficSimulation extends AbstractBehavior<Command> {

	public static record StartSimulation() implements Command {}

	public static void main(String[] args) throws InterruptedException {
		final ActorSystem<Command> system = ActorSystem.create(RunTrafficSimulation.create(), "SimulationSystem");
		system.tell(new RunTrafficSimulation.StartSimulation());
	}

	public static Behavior<Command> create() {
		return Behaviors.setup(RunTrafficSimulation::new);
	}

	private RunTrafficSimulation(ActorContext<Command> context) {
		super(context);
	}

	@Override
	public Receive<Command> createReceive() {
		return newReceiveBuilder().onMessage(StartSimulation.class, this::onStartSimulation).build();
	}

	private Behavior<Command> onStartSimulation(StartSimulation command) {
		// final int nSteps = 100;
		final List<ActorRef<Command>> listeners = new ArrayList<>();
		final ActorRef<Command> simulation = getContext()
				.spawn(AbstractSimulation.create(TrafficSimulationWithCrossRoads.class, listeners), "Simulation");
		listeners.add(getContext().spawn(RoadSimStatistics.create(), "Simulation-Statistics"));
		final var view = getContext().spawn(RoadSimView.create(simulation), "Simulation-View");
		listeners.add(view);
		// simulation.tell(new AbstractSimulation.NextStep(nSteps));
		view.tell(new RoadSimView.Show());
		return this;
	}
}
