package eventloop;

import java.util.Queue;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;

import utils.MyCondition;
import utils.SimpleSemaphore;

public class EventLoopImpl implements RunnableEventLoop {
    
    private final Queue<Runnable> tasks = new LinkedList<>();
    private AtomicBoolean finished = new AtomicBoolean(false);
    private boolean started = false;
    private final SimpleSemaphore sem;
    private final MyCondition cond;

    public EventLoopImpl(final SimpleSemaphore sem, final MyCondition cond) {
        this.sem = sem;
        this.cond = cond;
    }

    @Override
    public void run() {
        if (this.started) throw new IllegalStateException("Event loop is already running.");
        this.started = true;
        this.finished.set(false);
        new Thread(() -> {
            while (!this.tasks.isEmpty()) {
                try {
                    this.sem.waitForGreen();
                } catch (InterruptedException e) {}
                this.tasks.poll().run();
            }
            System.out.println("event loop finished");
            this.cond.signalAll();
        }).start();
    }

    @Override
    public void enqueueTask(final Runnable task) {
        this.tasks.add(task);
    }
}