package macropart2.eventloop;

import java.util.Queue;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;

public class EventLoopImpl implements RunnableEventLoop {
    
    private final Queue<Runnable> tasks = new LinkedList<>();
    private AtomicBoolean isStopped = new AtomicBoolean(true);
    private AtomicBoolean finished = new AtomicBoolean(false);
    private boolean started = false;

    @Override
    public void run() {
        if (this.started) throw new IllegalStateException("Event loop is already running.");
        this.started = true;
        this.isStopped.set(false);
        this.finished.set(false);
        new Thread(() -> {
            while (!this.tasks.isEmpty()) {
                while (this.isStopped.get()) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                this.tasks.poll().run();
            }
            System.out.println("event loop finished");
            this.finished.set(true);
        }).start();
    }

    @Override
    public void enqueueTask(final Runnable task) {
        this.tasks.add(task);
    }

    @Override
    public void stop() {
        if (!this.started) throw new IllegalStateException("Event loop is not running.");
        if (this.finished.get()) throw new IllegalStateException("Event loop is already finished.");
        if (this.isStopped.get()) throw new IllegalStateException("Event loop is already stopped.");
        this.isStopped.set(true);
    }

    @Override
    public void resume() {
        if (!this.started) throw new IllegalStateException("Event loop is not running.");
        if (this.finished.get()) throw new IllegalStateException("Event loop is already finished.");
        if (!this.isStopped.get()) throw new IllegalStateException("Event loop is not stopped.");
        this.isStopped.set(false);
    }

    @Override
    public boolean isStopped() {
        return this.isFinished() || this.isStopped.get();
    }

    @Override
    public boolean isFinished() {
        return this.finished.get();
    }
}