package simengine;

import actor.Command;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Receive;

public abstract class SimulationListener extends AbstractBehavior<Command> {

    public static record ViewUpdate(int t, int stepNumber, long deltaMillis, AbstractEnvironment<? extends AbstractAgent> env) implements Command {}

    public static record SimulationFinished() implements Command {}

    protected SimulationListener(ActorContext<Command> context) {
        super(context);
    }

	/**
     * Called at each step, updates all updates
     */
    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(ViewUpdate.class, this::onViewUpdate)
                .onMessage(SimulationFinished.class, this::onSimulationFinished)
                .build();
    }

    /**
     * Called at each step, updates all updates
     */
    protected abstract Behavior<Command> onViewUpdate(ViewUpdate command);

    /**
     * Called at the end of the simulation
     */
    protected abstract Behavior<Command> onSimulationFinished(SimulationFinished command);
}