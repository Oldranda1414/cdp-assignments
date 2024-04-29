package macropart2.reactiveprogramming;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JSoupHandler {
    public static List<String> getLinksFromUrl(String url) {
        List<String> links = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(url).get();
            Elements linkElements = doc.select("a[href]");
            for (Element link : linkElements) {
                String linkUrl = link.attr("abs:href");
                links.add(linkUrl);
            }
        } catch (IOException e) {
            // System.err.println("[LINKS] Errore durante la connessione all'URL: " + e.getMessage());
        }
        return links;
    }

    public static int findWordOccurrences(final String url, final String word) {
        int count = 0;
        try {
            Document doc = Jsoup.connect(url).get();
            String text = doc.body().text();
            var words = List.of(text.split("\\s+"));
            
            for (String w : words) {
                if (w.equalsIgnoreCase(word)) {
                    count++;
                }
            }
        } catch (IOException e) {
            // System.err.println("[OCCUR] Errore durante la connessione all'URL: " + e.getMessage());
        }
        return count;
    }
}
