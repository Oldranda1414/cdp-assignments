import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import macropart2.eventloop.*;

public class EventLoopTest {
    
    private RunnableEventLoop eventLoop;

    @BeforeEach
    public void beforeEach() {
        eventLoop = new EventLoopImpl();
    }

    @Test
    public void testStopAtBegin() {
        assertTrue(eventLoop.isStopped());
    }

    @Test
    public void testIsNotStoppedWhenRunning() {
        eventLoop.enqueueTask(() -> heavyTask());
        eventLoop.run();
        assertFalse(eventLoop.isStopped());
    }

    @Test
    public void testIsStoppedWhenStopped() {
        eventLoop.enqueueTask(() -> heavyTask());
        eventLoop.run();
        eventLoop.stop();
        assertTrue(eventLoop.isStopped());
    }

    @Test
    public void testIsRunningWhenResumed() {
        eventLoop.enqueueTask(() -> heavyTask());
        eventLoop.run();
        eventLoop.stop();
        eventLoop.resume();
        assertFalse(eventLoop.isStopped());
    }

    // @Test
    // public void testIsFinishedWhenAllTasksAreDone() {
    //     eventLoop.enqueueTask(() -> heavyTask());
    //     eventLoop.run();
    //     var start = System.currentTimeMillis();
    //     while (!eventLoop.isFinished()) {
    //         if (System.currentTimeMillis() - start > 4000) {
    //             fail("Event loop did not finish a 3 second task after 4 seconds");
    //         }
    //     }
    //     assertTrue(eventLoop.isFinished());
    // }

    private void heavyTask() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
