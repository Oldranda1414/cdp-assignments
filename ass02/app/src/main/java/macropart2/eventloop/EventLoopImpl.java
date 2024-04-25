package macropart2.eventloop;

import java.util.Queue;
import java.util.LinkedList;

public class EventLoopImpl implements RunnableEventLoop {
    
    private final Queue<Runnable> tasks = new LinkedList<>();
    private boolean isStopped = true;
    private boolean finished = false;

    @Override
    public void run() {
        new Thread(() -> {
            while (!tasks.isEmpty()) {
                while (isStopped) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                tasks.poll().run();
            }
            this.finished = true;
        }).start();
        this.isStopped = false;
    }

    @Override
    public void enqueueTask(final Runnable task) {
        this.tasks.add(task);
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

    @Override
    public boolean isFinished() {
        return this.finished;
    }
}