package macropart2.eventloop;

public class Main {
    
    public static void main(String[] args) {
        RunnableEventLoop eventLoop = new EventLoopImpl();
        var link = "https://www.google.com";
        var word = "hello";
        var depth = 5;
        eventLoop.enqueueTask(() -> getWordOccurrences(link, word, depth, eventLoop));
        eventLoop.run();
    }

    private static void getWordOccurrences(final String link, final String word, final int depth, final EventLoop eventLoop) {

    }
}
