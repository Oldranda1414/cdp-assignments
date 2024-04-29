package macropart2.reactiveprogramming;

import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) {
        AtomicInteger counter = new AtomicInteger(0);

        Listener view = new CLIView();
        Scraper s = new SecondScraperImpl(
           "http://localhost:8080",
           6,
           "page",
           counter
        );
        s.registerListener(view);

        s.scrape();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        while (counter.get() > 0) {
            // try {
            //     Thread.sleep(1000);
            // } catch (InterruptedException e) {
            //     e.printStackTrace();
            // }
            // System.out.println("Main thread waiting...");
            // Main thread waits for other threads to finish
        }

        System.out.println("Scraping finished");
    }
}
