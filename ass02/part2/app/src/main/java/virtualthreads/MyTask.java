package virtualthreads;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import utils.JSoupHandler;
import utils.SimpleSemaphore;
import wordcounter.WordCounterListener;

public class MyTask implements Runnable{
    private String url;
    private String word;
    private int depth;
    private Map<String, Integer> map;
    private boolean isLogger;
    private List<WordCounterListener> listeners;
    private int count = 0;
    private List<Thread> childThreads = new ArrayList<>();
    private SimpleSemaphore sem;

    public MyTask(String url, String word, int depth, Map<String, Integer> map, List<WordCounterListener> listeners, SimpleSemaphore sem, boolean isLogger){
        this.url = url;
        this.word = word;
        this.depth = depth;
        this.map = map;
        this.listeners = listeners;
        this.sem = sem;
        this.isLogger = isLogger;
        //log("created thread for depth" + this.depth);
    }

    @Override
    public void run() {
        this.checkForCondition();
        if(this.map.containsKey(this.url)) return;
        try{
            //log("searching for word occurences");
            this.searchWord();
            //log("searching for links");
            this.searchLinks();
        }
        catch (Exception e){
            //log("skipping: " + url);
        }
    }

    private void searchWord(){
        //log("searching in " + this.url);
        this.count = JSoupHandler.findWordOccurrences(this.url, this.word);
        this.checkForCondition();
        this.updateMap();
    }

    private void searchLinks(){
        var newDepth = depth - 1;
        //log("creating threads with new depth of: " + newDepth);
        if(newDepth == 0) return;
        var links = JSoupHandler.getLinksFromUrl(this.url);
        //log("links obtained" + links);
        for(var link : links){
            this.checkForCondition();
            //log("creating a thread looking into: " + link);
            this.childThreads.add(Thread.ofVirtual().start(new MyTask(link, word, newDepth, this.map, listeners, this.sem, this.isLogger)));
        }
        for(var thread : this.childThreads){
            try{
                thread.join();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void updateMap(){
        this.checkForCondition();
        this.map.put(this.url, this.count);
        this.listeners.forEach(l -> l.onNewWordCounted(this.url, this.count));
    }

    private void checkForCondition(){
        log("checking for pause: " + this.sem.isRed());
        try{
            this.sem.waitForGreen();
        }
        catch (Exception e){
            e.printStackTrace();
        } 
    }
    
    private void log(String msg) {
        if(this.isLogger){
		    System.out.println("[ thread looking at " + this.url + " ] " + msg);
        }
	}
    
}
