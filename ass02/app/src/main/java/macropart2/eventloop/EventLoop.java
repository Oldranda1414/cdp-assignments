package macropart2.eventloop;

/**
 * A simple event loop that can run tasks.
 */
public interface EventLoop {
    
    /**
     * Run the event loop.
     */
    void run();

    /**
     * Add a task to the event loop.
     * @param task the task to add
     */
    void addTask(Runnable task);
}
