package macropart2.eventloop;

/**
 * A simple event loop that execute enqueued tasks.
 */
public interface EventLoop {

    /**
     * Add a task to the event loop.
     * @param task the task to add
     */
    void enqueueTask(Runnable task);
}
