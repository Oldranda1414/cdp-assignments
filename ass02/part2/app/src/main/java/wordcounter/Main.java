package wordcounter;

import view.GUI;
import eventloop.WordCounterWithEventLoop;
import virtualthreads.WordCounterWithVirtualThreads;
import reactive.WordCounterWithReactive;

public class Main {

    public static void main(String[] args) {
        //var wordCounter = new WordCounterWithEventLoop();
        var wordCounter = new WordCounterWithReactive();
        // var wordCounter = new WordCounterWithVirtualThreads(true);
        new GUI(wordCounter).display();
    }

}
