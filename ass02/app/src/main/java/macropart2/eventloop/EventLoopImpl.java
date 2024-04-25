package macropart2.eventloop;

import java.util.Queue;
import java.util.LinkedList;

public class EventLoopImpl implements RunnableEventLoop {
    
    private final Queue<Runnable> tasks = new LinkedList<>();
    private boolean isStopped = false;

    @Override
    public void run() {
        log("Running event loop.");
        while (!tasks.isEmpty()) {
            while (isStopped) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Runnable task = tasks.poll();
            task.run();
        }
        log("Event loop finished.");
    }

    @Override
    public void enqueueTask(final Runnable task) {
        tasks.add(task);
    }

    private void log(final String message) {
        System.out.println("[EVENTLOOP]: " + message);
    }

    @Override
    public void stop() {
        if (this.isStopped) throw new IllegalStateException("Event loop is already stopped.");
        this.isStopped = true;
    }

    @Override
    public void resume() {
        if (!this.isStopped) throw new IllegalStateException("Event loop is not stopped.");
        this.isStopped = false;
    }

    @Override
    public boolean isStopped() {
        return this.isStopped;
    }
}