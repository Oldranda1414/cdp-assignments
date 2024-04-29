package macropart2.reactiveprogramming;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FirstScraperImpl implements Scraper, Listened {
    private final String initialUrl;
    private final int maxDepth;
    private final Set<Listener> listeners;

    public FirstScraperImpl(final String initialUrl, final int maxDepth) {
        this.initialUrl = initialUrl;
        this.maxDepth = maxDepth;
        this.listeners = new HashSet<>();
    }

    @Override
    public void scrape() {
        scrapeAux(this.initialUrl, 0)
            .subscribe(this::log);  
    
            for (int i = 0; i < 30; i++) {
                try {
                System.out.println("Main thread working...");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Results getResults() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getResults'");
    }
    
    private Observable<Object> scrapeAux(String url, int depth) {
        return Observable
        .fromAction(() -> {
                System.out.println(Thread.currentThread().getName());
                List<String> links = getLinks("https://www.google.com");
                if (depth <= maxDepth) {
                    links.forEach(link -> scrapeAux(link, depth + 1).subscribe(this::log));
                }
            })
            .subscribeOn(Schedulers.io());
    }
    
    private String getWebpage(String url) {
        System.out.println("Loading page... url: " + url);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Returning page (url: " + url + ")");
        return url;
    }


    private List<String> getLinks(String url) {
        String webpage = this.getWebpage(url);
        return List.of(
            webpage + "/one",
            webpage + "/two",
            webpage + "/three",
            webpage + "/four"
        );
    }

    private void log(Object s) {
        System.out.print("[" + Thread.currentThread().getName() + "] ");
        System.out.println(s.toString());
    }

    @Override
    public void registerListener(Listener listener) {
        this.listeners.add(listener);
    }

    @Override
    public void unregisterListener(Listener listener) {
        if (this.listeners.contains(listener)) {
            this.listeners.remove(listener);
        }
    }
}
