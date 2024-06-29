package simengine;

import java.util.ArrayList;
import java.util.List;

import actor.Command;
import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import executor.Task;
import simengine.SimulationListener.ViewUpdate;

/**
 * Base class for defining concrete simulations
 * 
 */
public abstract class AbstractSimulation<T extends AbstractEnvironment<? extends AbstractAgent>, S extends AbstractSimulation<T, S>>
	extends AbstractBehavior<Command> {

	public static record NextStep() implements Command {}

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

	protected AbstractSimulation(ActorContext<Command> context, int numSteps, List<ActorRef<Command>> listeners) {
		super(context);
		senseDecideWorks = new ArrayList<Task>();
		actWorks = new ArrayList<Task>();
		this.numSteps = numSteps;
		this.currentStep = 0;
		this.t = t0;
		this.listeners = listeners;

		getContext().getLog().info("Simulation started");

		startWallTime = System.currentTimeMillis();

		this.timePerStep = 0;
	}

	public static <S extends AbstractSimulation<?, S>> Behavior<Command> create(Class<S> concreteClass, int numSteps, List<ActorRef<Command>> listeners) {
        return Behaviors.setup(context -> concreteClass.getDeclaredConstructor(ActorContext.class).newInstance(context, numSteps, listeners));
    }

    protected void logMessage(String message) {
        getContext().getLog().info(message);
    }

	@Override
	public Receive<Command> createReceive() {
		return newReceiveBuilder()
			.onMessage(NextStep.class, this::onNextStep)
			.build();
	}

	private AbstractBehavior<Command> onNextStep(NextStep command) throws InterruptedException {
		this.executeNextStep();
		if (this.numSteps == this.currentStep) {
			endWallTime = System.currentTimeMillis();
			this.averageTimePerStep = this.timePerStep / numSteps;
			logMessage("Simulation finished");
			for (var listener : listeners) {
				listener.tell(new SimulationListener.SimulationFinished());
			}
			getContext().stop(getContext().getSelf());
		}
		getContext().getSelf().tell(new NextStep());
		for (var listener : listeners) {
			listener.tell(new ViewUpdate(t, currentStep, currentWallTime - startWallTime, env));
		}
		return this;
	}

	public long getSimulationDuration() {
		return endWallTime - startWallTime;
	}

	public long getAverageTimePerCycle() {
		return averageTimePerStep;
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
		currentWallTime = System.currentTimeMillis();
		this.env.step(dt);
		step("sense-decide", senseDecideWorks);
		step("act", actWorks);
		t += dt;
		if (toBeInSyncWithWallTime) {
			syncWithWallTime();
		}
		this.currentStep++;
	}

	private void step(String taskType, List<Task> tasks) {
		// List<Future<?>> futures = new ArrayList<>();
		// fillBag(taskType, tasks, futures);
		// for (var future : futures) {
		// 	future.get();
		// }
	}

	// private void fillBag(/*String workName, List<Task> works, List<Future<?>> futures*/) {
		// for (Task work : works) {
		// 	futures.add(executor.submit(work));
		// }
	// }

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