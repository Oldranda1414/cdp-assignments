package executor;

import actor.Command;
import akka.japi.function.Function;

public class Task implements Function<Command, Void> {
    private Function<Command, Void> function;
    private String agentId;
    private String typeOfTask;

    public Task(Function<Command, Void> function, String agentId, String type){
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
    public Void apply(Command command) throws Exception {
        this.function.apply(command);
        return null;
    }
}
