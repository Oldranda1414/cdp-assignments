package macropart2.eventloop;

import java.util.Queue;
import java.util.LinkedList;

public class EventLoopImpl implements EventLoop {
    
    private final Queue<Runnable> tasks = new LinkedList<>();

    @Override
    public void run() {
        log("Running event loop.");
        while (!tasks.isEmpty()) {
            Runnable task = tasks.poll();
            task.run();
        }
        log("Event loop finished.");
    }

    @Override
    public void addTask(final Runnable task) {
        tasks.add(task);
    }

    private void log(final String message) {
        System.out.println("[EVENTLOOP]: " + message);
    }
}