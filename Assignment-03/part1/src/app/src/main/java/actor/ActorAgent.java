package actor;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import executor.Task;

public class ActorAgent extends AbstractBehavior<Command> {

    public static record SenseDecide() implements Command {}
    public static record Act() implements Command {}

    private Task onSenseDecide;
    private Task onAct;

    public static Behavior<Command> create(Task onSenseDecide, Task onAct) {
        return Behaviors.setup(context -> new ActorAgent(context, onSenseDecide, onAct));
    }

    private ActorAgent(ActorContext<Command> context, Task onSenseDecide, Task onAct) {
        super(context);
        this.onSenseDecide = onSenseDecide;
        this.onAct = onAct;
    }

	/**
     * Called at each step, updates all updates
     */
    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(SenseDecide.class, this.onSenseDecide::apply)
                .onMessage(Act.class, this.onAct::apply)
                .build();
    }
}
