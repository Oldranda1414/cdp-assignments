package macropart2.virtualthreads;

import java.util.Map;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import macropart2.virtualthreads.utils.RWTreeMonitor;

public class MyTask implements Runnable{
    private String url;
    private String word;
    private int depth;
    private Map<String, Integer> map;

    public MyTask(String url, String word, int depth, RWTreeMonitor<String, Integer> map){
        this.url = url;
        this.word = word;
        this.depth = depth;
        this.map = map;
    }

    @Override
    public void run() {
        
    }
    
}
