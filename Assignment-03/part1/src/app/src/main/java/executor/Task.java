package executor;

import actor.Command;
import akka.actor.typed.Behavior;
import akka.japi.function.Function;

public class Task implements Function<Command, Behavior<Command>> {
    private Function<Command, Behavior<Command>> function;
    private String agentId;
    private String typeOfTask;

    public Task(Function<Command, Behavior<Command>> function, String agentId, String type){
        this.function = function;
        this.agentId = agentId;
        this.typeOfTask = type;
    }

    public String getAgentId() {
        return this.agentId;
    }

    public String getTypeOfTask() {
        return this.typeOfTask;
    }

    @Override
    public Behavior<Command> apply(Command command) throws Exception {
        return this.function.apply(command);
    }
}
