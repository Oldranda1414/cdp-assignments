package simengine;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import actor.ActorAgent;
import actor.ActorAgent.Act;
import actor.ActorAgent.SenseDecide;
import actor.Command;
import actor.Task;

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

	public static record StartSimulation(int numSteps) implements Command {
	}

	public static record NextStep() implements Command {
	}

	public static record SenseDecideDone() implements Command {
	}

	public static record ActDone() implements Command {
	}

	public static record Stop() implements Command {
	}

	public static record Resume() implements Command {
	}

	private boolean isStopped = false;
	private List<ActorRef<Command>> agents;
	private long timePerStep;
	private int t;
	private List<ActorRef<Command>> listeners;
	private long currentWallTime;
	private int numSteps;
	private int currentStep;
	private AbstractEnvironment<? extends AbstractAgent> env;
	private AbstractStates<T> agentStates;
	private List<Task> senseDecideTasks;
	private List<Task> actTasks;
	private int t0;
	private int dt;
	private long startWallTime;
	private long endWallTime;
	private long averageTimePerStep;
	private boolean toBeInSyncWithWallTime;
	private int nStepsPerSec;
	// Used to determine how many actors have processed the current step
	private int updatedActors;

	protected AbstractSimulation(ActorContext<Command> context, List<ActorRef<Command>> listeners) {
		super(context);
		senseDecideTasks = new ArrayList<Task>();
		actTasks = new ArrayList<Task>();
		agents = new ArrayList<ActorRef<Command>>();
		this.currentStep = 0;
		this.t = t0;
		this.listeners = listeners;

		startWallTime = System.currentTimeMillis();

		this.timePerStep = 0;
	}

	public static <S extends AbstractSimulation<?, S>> Behavior<Command> create(Class<S> concreteClass,
			List<ActorRef<Command>> listeners) {
		return Behaviors.setup(context -> concreteClass.getDeclaredConstructor(ActorContext.class, List.class)
				.newInstance(context, listeners)); // in order to work the constructor must be public
	}

	@Override
	public Receive<Command> createReceive() {
		return newReceiveBuilder()
				.onMessage(StartSimulation.class, this::onStartSimulation)
				.onMessage(NextStep.class, this::onNextStep)
				.onMessage(SenseDecideDone.class, this::OnSenseDecideDone)
				.onMessage(ActDone.class, this::OnActDone)
				.onMessage(Stop.class, this::onStop)
				.onMessage(Resume.class, this::onResume)
				.build();
	}

	private Behavior<Command> onStartSimulation(StartSimulation command) {
		logMessage("Simulation started");
		this.numSteps = command.numSteps;
		this.currentStep = 1;
		this.updatedActors = 0;
		for (var sdTask : senseDecideTasks) {
			final var index = senseDecideTasks.indexOf(sdTask);
			agents.add(getContext().spawn(ActorAgent.create(sdTask, actTasks.get(index), getContext().getSelf()),
					"agent-" + (index + 1)));
		}
		getContext().getSelf().tell(new NextStep());
		return this;
	}

	private Behavior<Command> onNextStep(NextStep command) {
		if (!isStopped) {
			this.currentWallTime = System.currentTimeMillis();
			this.env.step(dt);
			this.updatedActors = 0;
			this.sendToAgents("sense-decide");
		}
		return this;
	}

	private Behavior<Command> OnSenseDecideDone(SenseDecideDone command) {
		this.updatedActors++;
		int numberOfActors = this.agents.size();
		if (this.updatedActors == numberOfActors) {
			this.updatedActors = 0;
			this.sendToAgents("act");
		}
		return this;
	}

	synchronized private Behavior<Command> OnActDone(ActDone command) {
		this.updatedActors++;
		int numberOfActors = this.agents.size();
		if (this.updatedActors == numberOfActors) {
			this.updatedActors = 0;
			this.t += this.dt;
			this.timePerStep += System.currentTimeMillis() - this.currentWallTime;
			for (var listener : this.listeners) {
				listener.tell(new SimulationListener.ViewUpdate(this.t, this.currentStep,
						this.currentWallTime - this.startWallTime, this.env));
			}
			this.currentStep++;
			// checking if the simulation is finished
			if (this.numSteps == this.currentStep) {
				return this.endSimulation();
			} else {
				if (this.toBeInSyncWithWallTime) {
					this.syncWithWallTime();
				} else {
					getContext().getSelf().tell(new NextStep());
				}
			}
		}
		return this;
	}

	private Behavior<Command> onStop(Stop command) {
		this.isStopped = true;
		return this;
	}

	private Behavior<Command> onResume(Resume command) {
		this.isStopped = false;
		getContext().getSelf().tell(new NextStep());
		return this;
	}

	private void sendToAgents(String taskType) {
		Command command = taskType == "sense-decide" ? new SenseDecide() : new Act();
		for (var agent : this.agents) {
			agent.tell(command);
		}
	}

	private Behavior<Command> endSimulation() {
		this.endWallTime = System.currentTimeMillis();
		this.averageTimePerStep = this.timePerStep / this.numSteps;
		logMessage("Simulation finished");
		logMessage("Total time: " + (this.endWallTime - this.startWallTime) + "ms");
		logMessage("Average time per step: " + this.averageTimePerStep + "ms");
		for (var listener : this.listeners) {
			listener.tell(new SimulationListener.SimulationFinished());
		}
		for (var agent : this.agents) {
			agent.tell(new ActorAgent.SimulationFinished());
		}
		return Behaviors.stopped();
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
		this.actTasks.add(act);
	}

	protected void addSenseDecide(Task senseDecide) {
		this.senseDecideTasks.add(senseDecide);
	}

	protected void syncWithTime(int nCyclesPerSec) {
		this.toBeInSyncWithWallTime = true;
		this.nStepsPerSec = nCyclesPerSec;
	}

	private void syncWithWallTime() {
		long newWallTime = System.currentTimeMillis();
		long delay = 1000 / this.nStepsPerSec;
		long wallTimeDT = newWallTime - currentWallTime;
		if (wallTimeDT < delay) {
			getContext().scheduleOnce(Duration.ofMillis(delay - wallTimeDT), getContext().getSelf(), new NextStep());
		}
	}

	protected void logMessage(String message) {
		getContext().getLog().info(message);
	}
}