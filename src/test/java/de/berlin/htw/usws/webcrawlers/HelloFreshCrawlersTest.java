package de.berlin.htw.usws.webcrawlers;

import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;

public class HelloFreshCrawlersTest {

    @Test
    @Ignore
    public void crawl() {
        ArrayList<String> urls = new HelloFreshUnknownIdsCrawler().crawlRecipePage();
    }
}