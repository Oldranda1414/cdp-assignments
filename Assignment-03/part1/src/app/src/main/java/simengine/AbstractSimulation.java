package simengine;

import java.util.ArrayList;
import java.util.List;

import actor.ActorAgent;
import actor.ActorAgent.Act;
import actor.ActorAgent.SenseDecide;
import actor.Command;
import executor.Task;
import simengine.SimulationListener.ViewUpdate;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

/**
 * Base class for defining concrete simulations
 * 
 */
public abstract class AbstractSimulation<T extends AbstractEnvironment<? extends AbstractAgent>, S extends AbstractSimulation<T, S>>
	extends AbstractBehavior<Command> {

	public static record NextStep(int numSteps) implements Command {}
	public static record Stop() implements Command {}
	public static record Resume() implements Command {}

	private List<ActorRef<Command>> agents;
	private long timePerStep;
	private int t;
	private List<ActorRef<Command>> listeners;
	private long currentWallTime;
	private int numSteps;
	private int currentStep;
	private AbstractEnvironment<? extends AbstractAgent> env;
	private AbstractStates<T> agentStates;
	private List<Task> senseDecideWorks;
	private List<Task> actWorks;
	private int t0;
	private int dt;
	private long startWallTime;
	private long endWallTime;
	private long averageTimePerStep;
	private boolean toBeInSyncWithWallTime;
	private int nStepsPerSec;

	protected AbstractSimulation(ActorContext<Command> context, List<ActorRef<Command>> listeners) {
		super(context);
		senseDecideWorks = new ArrayList<Task>();
		actWorks = new ArrayList<Task>();
		agents = new ArrayList<ActorRef<Command>>();
		this.currentStep = 0;
		this.t = t0;
		this.listeners = listeners;

		logMessage("Simulation started");

		startWallTime = System.currentTimeMillis();

		this.timePerStep = 0;
	}

	public static <S extends AbstractSimulation<?, S>> Behavior<Command> create(Class<S> concreteClass, List<ActorRef<Command>> listeners) {
        return Behaviors.setup(context -> concreteClass.getDeclaredConstructor(ActorContext.class, List.class).newInstance(context, listeners)); //in order to work the constructor must be public
    }

    protected void logMessage(String message) {
        getContext().getLog().info(message);
    }

	@Override
	public Receive<Command> createReceive() {
		return newReceiveBuilder()
			.onMessage(NextStep.class, this::onNextStep)
			.onMessage(Stop.class, this::onStop)
			.onMessage(Resume.class, this::onResume)
			.build();
	}

	private AbstractBehavior<Command> onNextStep(NextStep command) throws InterruptedException {
		if (this.currentStep == 0) {
			this.numSteps = command.numSteps;
			for (var sdTask : senseDecideWorks) {
				final var index = senseDecideWorks.indexOf(sdTask);
				agents.add(getContext().spawn(ActorAgent.create(sdTask, actWorks.get(index)), "agent-" + index));
			}
		}
		this.executeNextStep();
		if (this.numSteps == this.currentStep) {
			this.endWallTime = System.currentTimeMillis();
			this.averageTimePerStep = this.timePerStep / this.numSteps;
			logMessage("Simulation finished");
			for (var listener : this.listeners) {
				listener.tell(new SimulationListener.SimulationFinished());
			}
			getContext().stop(getContext().getSelf());
		}
		getContext().getSelf().tell(new NextStep(command.numSteps));
		for (var listener : this.listeners) {
			listener.tell(new ViewUpdate(this.t, this.currentStep, this.currentWallTime - this.startWallTime, this.env));
		}
		return this;
	}

	private AbstractBehavior<Command> onStop(Stop command) {
		//TODO: implement
		return this;
	}

	private AbstractBehavior<Command> onResume(Resume command) {
		//TODO: implement
		return this;
	}

	protected void setupTimings(int t0, int dt) {
		this.t0 = t0;
		this.dt = dt;
	}

	protected void setupEnvironment(AbstractEnvironment<? extends AbstractAgent> env) {
		this.env = env;
	}

	protected AbstractEnvironment<? extends AbstractAgent> getEnvironment() {
		return this.env;
	}

	protected void setupAgentStates(AbstractStates<T> states) {
		this.agentStates = states;
	}

	protected AbstractStates<T> getAgentStates() {
		return this.agentStates;
	}

	protected void addAct(Task act) {
		this.actWorks.add(act);
	}

	protected void addSenseDecide(Task senseDecide) {
		this.senseDecideWorks.add(senseDecide);
	}

	protected void syncWithTime(int nCyclesPerSec) {
		this.toBeInSyncWithWallTime = true;
		this.nStepsPerSec = nCyclesPerSec;
	}

	private void executeNextStep() {
		this.currentWallTime = System.currentTimeMillis();
		this.env.step(dt);
		this.step("sense-decide");
		this.step("act");
		this.t += this.dt;
		if (this.toBeInSyncWithWallTime) {
			this.syncWithWallTime();
		}
		this.currentStep++;
	}

	private void step(String taskType) {
		Command command = taskType == "sense-decide" ? new SenseDecide() : new Act();
		for (var agent : this.agents) {
			agent.tell(command);
		}
		this.timePerStep += System.currentTimeMillis() - this.currentWallTime;
	}

	private void syncWithWallTime() {
		try {
			long newWallTime = System.currentTimeMillis();
			long delay = 1000 / this.nStepsPerSec;
			long wallTimeDT = newWallTime - currentWallTime;
			if (wallTimeDT < delay) {
				Thread.sleep(delay - wallTimeDT);
			}
		} catch (Exception ex) {
		}
	}
}