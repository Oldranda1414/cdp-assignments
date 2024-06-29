package simtrafficexamples;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class HelloWorldBot extends AbstractBehavior<HelloWorldBot.Greeted> {

    public static Behavior<Greeted> create(int max) {
        return Behaviors.setup(context -> new HelloWorldBot(context, max));
    }

    private final int max;
    private int greetingCounter;

    private HelloWorldBot(ActorContext<Greeted> context, int max) {
        super(context);
        this.max = max;
    }

    public static record Greeted(String whom, ActorRef<HelloWorld.Greet> from) {
    }

    @Override
    public Receive<Greeted> createReceive() {
        return newReceiveBuilder().onMessage(Greeted.class, this::onGreeted).build();
    }

    private Behavior<Greeted> onGreeted(Greeted message) {
        greetingCounter++;
        getContext().getLog().info("Greeting {} for {}", greetingCounter, message.from);
        if (greetingCounter == max) {
            return Behaviors.stopped();
        } else {
            message.from.tell(new HelloWorld.Greet(message.whom, getContext().getSelf()));
            return this;
        }
    }
}