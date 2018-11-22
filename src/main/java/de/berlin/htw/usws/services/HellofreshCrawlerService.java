package de.berlin.htw.usws.services;

import de.berlin.htw.usws.webcrawlersOld.RecipeCrawlerChefkoch;
import de.berlin.htw.usws.webcrawlersOld.UnknownIdsCrawlerChefkoch;

import javax.ejb.Stateless;
import java.io.IOException;
import java.util.ArrayList;

@Stateless
public class HellofreshCrawlerService {

    private UnknownIdsCrawlerChefkoch idsCrawler;

    private RecipeCrawlerChefkoch recipeCrawler;

    public void start() {
        idsCrawler = new UnknownIdsCrawlerChefkoch();

        try {
            ArrayList<Long> unknownIds = idsCrawler.crawlRecipePages();
            recipeCrawler = new RecipeCrawlerChefkoch();
            for (int i = unknownIds.size()-1; i >= 0; i--) {
                recipeCrawler.scrapRecipe(unknownIds.get(i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
