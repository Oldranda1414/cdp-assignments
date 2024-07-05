package pcd.ass01.masterworker;

public class Task implements Runnable{
    private Runnable runnable;
    private String agentId;
    private String typeOfTask;

    public Task(Runnable runnable, String agentId, String type){
        this.runnable = runnable;
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
    public void run() {
        this.runnable.run();
    }
}
