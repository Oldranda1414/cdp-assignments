package pcd.ass01.simengineconcurrent;

public class Task {
    private Runnable runnable;
    private String agentId;
    private String typeOfTask;

    public Task(Runnable runnable, String agentId, String type){
        this.runnable = runnable;
        this.agentId = agentId;
        this.typeOfTask = type;
    }

    public Runnable getRunnable() {
        return this.runnable;
    }

    public String getAgentId() {
        return this.agentId;
    }

    public String getTypeOfTask() {
        return this.typeOfTask;
    }
}
