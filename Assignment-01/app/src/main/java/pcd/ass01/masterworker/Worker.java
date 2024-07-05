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
            while (true) {
                Task task = this.bagOfTasks.get(this.getName());
                task.run();
                if (simulationOver.get()) break;
            }
            
        } catch (InterruptedException e) {
        }
    }
}
