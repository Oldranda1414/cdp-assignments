package macropart2;

import java.util.Map;

import macropart2.View.GUI;
import macropart2.eventloop.WordCounterWithEventLoop;
import macropart2.virtualthreads.WordCounterWithVirtualThreads;
import macropart2.reactive.WordCounterWithReactive;

public class Main {

    public static void main(String[] args) {
        var wordCounter = new WordCounterWithEventLoop();
        // var wordCounter = new WordCounterWithReactive();
        // var wordCounter = new WordCounterWithVirtualThreads();
        new GUI(wordCounter).display();
    }

}
