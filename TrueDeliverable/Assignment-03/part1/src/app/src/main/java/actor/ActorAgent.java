package actor;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import simengine.AbstractSimulation.ActDone;
import simengine.AbstractSimulation.SenseDecideDone;

public class ActorAgent extends AbstractBehavior<Command> {

    public static record SenseDecide() implements Command {}
    public static record Act() implements Command {}
    public static record SimulationFinished() implements Command {}

    private Task senseDecide;
    private Task act;
    private ActorRef<Command> master;

    public static Behavior<Command> create(Task senseDecide, Task act, ActorRef<Command> master) {
        return Behaviors.setup(context -> new ActorAgent(context, senseDecide, act, master));
    }

    private ActorAgent(ActorContext<Command> context, Task senseDecide, Task act, ActorRef<Command> master) {
        super(context);
        this.senseDecide = senseDecide;
        this.act = act;
        this.master = master;
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(SenseDecide.class, this::onSenseDecide)
                .onMessage(Act.class, this::onAct)
                .onMessage(SimulationFinished.class, this::onSimulationFinished)
                .build();
    }

    private Behavior<Command> onSenseDecide(SenseDecide command) throws Exception {
        this.senseDecide.apply(command);
        this.master.tell(new SenseDecideDone());
        return this;
    }

    private Behavior<Command> onAct(Act command) throws Exception {
        this.act.apply(command);
        this.master.tell(new ActDone());
        return this;
    }

    private Behavior<Command> onSimulationFinished(SimulationFinished command) throws Exception {
		return Behaviors.stopped();
    }
}
