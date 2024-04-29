package macropart2.virtualthreads;

import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import macropart2.WordCounterListener;
import macropart2.virtualthreads.utils.SimpleSemaphore;
import macropart2.virtualthreads.utils.RWTreeMonitor;

public class MyTask implements Runnable{
    private String url;
    private String word;
    private int depth;
    private RWTreeMonitor<String, Integer> map;
    private boolean isLogger;
    private List<WordCounterListener> listeners;
    private int count = 0;
    private List<Thread> childThreads = new ArrayList<>();
    private SimpleSemaphore sem;

    public MyTask(String url, String word, int depth, RWTreeMonitor<String, Integer> map, List<WordCounterListener> listeners, SimpleSemaphore lockConditionPair, boolean isLogger){
        this.url = url;
        this.word = word;
        this.depth = depth;
        this.map = map;
        this.listeners = listeners;
        this.sem = lockConditionPair;
        this.isLogger = isLogger;
    }

    @Override
    public void run() {
        this.checkForCondition();
        if(this.map.containsKey(this.url)) return;
        try{
            Document doc = Jsoup.connect(url).get();
            log("searching for word occurences");
            this.searchWord(doc);
            log("searching for links");
            this.searchLinks(doc);
        }
        catch (Exception e){
            log("skipping: " + url);
        }
    }

    private void searchWord(Document doc){
        log("searching in " + this.url);
        var body = doc.body().text().toLowerCase();
        int index = body.indexOf(word.toLowerCase());
        while (index != -1) {
            this.checkForCondition();
            this.count++;
            index = body.indexOf(word.toLowerCase(), index + 1);
        }
        this.updateMap();
    }

    private void searchLinks(Document doc){
        var links = doc.select("a");
        var newDepth = depth - 1;
        if(newDepth == 0) return;
        for(var link : links){
            this.checkForCondition();
            String href = link.attr("href");
            this.childThreads.add(Thread.ofVirtual().start(new MyTask(href, word, newDepth, this.map, listeners, this.sem, this.isLogger)));
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
        log("pausing");
        try{
            this.sem.waitForGreen();
        }
        catch (Exception e){
            e.printStackTrace();
        } 
    }
    
	@SuppressWarnings("unused")
    private void log(String msg) {
        if(this.isLogger){
		    System.out.println("[ thread looking at " + this.url + " ] " + msg);
        }
	}
    
}
