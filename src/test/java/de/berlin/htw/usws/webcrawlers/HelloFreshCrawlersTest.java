package de.berlin.htw.usws.webcrawlers;

import de.berlin.htw.usws.webcrawlers.hellofresh.HelloFreshRecipeCrawler;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;

public class HelloFreshCrawlersTest {

    @Test
    @Ignore
    public void crawl() {
        // ArrayList<String> urls = new HelloFreshUnknownUrlsCrawler().crawlRecipePage();

        ArrayList<String> urls = new ArrayList<>();
        urls.add("https://www.hellofresh.de/recipes/wunderbar-saftiges-putengulasch-568cd65cf8b25ee91f8b4567?locale=de-DE");
        urls.add("https://www.hellofresh.de/recipes/cacciatore-italienisches-hahnchenragout-5b4de31130006c45744a6022?locale=de-DE");
        urls.add("https://www.hellofresh.de/recipes/himmel-und-erde-5be99f04ae08b522d8532fa2?locale=de-DE");

        HelloFreshRecipeCrawler helloFreshRecipeCrawler = new HelloFreshRecipeCrawler();

        for (String recipeUrl : urls) {
            helloFreshRecipeCrawler.scrapRecipe(recipeUrl);
        }

    }
}