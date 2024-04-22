package macropart2.eventloop;

public class Main {
    
    public static void main(String[] args) {
        EventLoop eventLoop = new EventLoopImpl();
        var link = "https://www.google.com";
        var word = "hello";
        var depth = 5;
        eventLoop.run();
    }
}
