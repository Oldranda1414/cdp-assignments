package macropart2.eventloop;

/**
 * An event loop that can run on demand.
 */
public interface RunnableEventLoop extends EventLoop {
    
    /**
     * Run the event loop.
     */
    void run();
}
