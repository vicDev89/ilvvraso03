package de.berlin.htw.usws.services;


import de.berlin.htw.usws.webcrawlers.hellofresh.HelloFreshRecipeCrawler;
import de.berlin.htw.usws.webcrawlers.hellofresh.HelloFreshUnknownUrlsCrawler;
import de.berlin.htw.usws.webcrawlersOld.UnknownIdsCrawlerChefkoch;

import javax.ejb.Stateless;
import java.util.ArrayList;

/**
 * Class holding mock-logic and to be deleted once a DB is connected.
 *
 * @author Lucas Larisch
 * @since 24.10.2018
 */
@Stateless
public class ChefkochCrawlerService {

    /**
     * Used for collecting IDs of recipes.
     */
    private HelloFreshUnknownUrlsCrawler urlsCrawler;

    /**
     * Used for collecting recipes.
     */
    private HelloFreshRecipeCrawler recipeCrawler;

    /**
     * Crawls all unknown IDs and the recipe connected to each ID afterwards.
     * Whereas the IDs are collected from newest to oldest, the recipes are
     * parsed starting with the last added ID (= oldest recipe). This algorithm is
     * based on the logic of {@link UnknownIdsCrawlerChefkoch#crawlRecipePages()}:
     * The last added recipe will be the newest in the database.
     * So in case of an error, only newer IDs will be collected (and the recipes
     * connected to them).
     *
     * @author Lucas Larisch
     * @since 24.10.2018
     */
    public void start() {
        urlsCrawler = new HelloFreshUnknownUrlsCrawler();

        ArrayList<String> unknownUrls = urlsCrawler.crawlRecipePage();
        recipeCrawler = new HelloFreshRecipeCrawler();
        for (int i = unknownUrls.size() - 1; i >= 0; i--) {
            recipeCrawler.scrapRecipe(unknownUrls.get(i));
        }

    }
}
