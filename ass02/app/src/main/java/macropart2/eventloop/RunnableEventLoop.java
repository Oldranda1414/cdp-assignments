package macropart2.eventloop;

/**
 * An event loop that can run on demand.
 */
public interface RunnableEventLoop extends EventLoop {
    
    /**
     * Run the event loop.
     */
    void run();

    /**
     * Stop the event loop.
     */
    void stop();

    /**
     * Check if the event loop is stopped.
     * @return true if the event loop is stopped, false otherwise.
     */
    boolean isStopped();

    /**
     * Resume a stopped event loop.
     */
    void resume();
}
