package pcd.ass01.masterworker;

import java.util.concurrent.atomic.AtomicBoolean;

import pcd.ass01.utils.Buffer;

public class Worker extends Thread {
    private Buffer<Task> bagOfTasks;
    private AtomicBoolean simulationOver;

    public Worker(
        Buffer<Task> bagOfTasks,
        AtomicBoolean simulationOver
    ) {
        this.bagOfTasks = bagOfTasks;
        this.simulationOver = simulationOver;
    }

    @Override
    public void run() {
        try {
            while (!simulationOver.get()) {
                var task = this.bagOfTasks.get(this.getName());
                log("running a " + task.getTypeOfTask() + " task for agent " + task.getAgentId());
                task.run();
            }

            log("worker killed");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void log(String message){
        synchronized(System.out){
            System.out.println("[" + this.getName() + "]: " + message);
        }
    }
}
