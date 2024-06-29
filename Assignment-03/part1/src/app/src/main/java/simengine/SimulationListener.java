package simengine;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Receive;

public abstract class SimulationListener extends AbstractBehavior<SimulationListener.ViewUpdate> {

    public static record ViewUpdate(int t, int stepNumber, long deltaMillis, AbstractEnvironment<? extends AbstractAgent> env) {}

    protected SimulationListener(ActorContext<ViewUpdate> context) {
        super(context);
    }

	/**
     * Called at each step, updates all updates
     */
    @Override
    public Receive<ViewUpdate> createReceive() {
        return newReceiveBuilder()
                .onMessage(ViewUpdate.class, this::onViewUpdate)
                .build();
    }

    /**
     * Called at each step, updates all updates
     */
    protected abstract Behavior<ViewUpdate> onViewUpdate(ViewUpdate command);
}
