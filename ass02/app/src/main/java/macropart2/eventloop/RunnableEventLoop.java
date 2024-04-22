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
     * Resume a stopped event loop.
     */
    void resume();
}
