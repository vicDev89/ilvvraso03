package de.berlin.htw.usws.mock_logic_ll;


import de.berlin.htw.usws.webCrawlers.IdCrawlerChefkoch;
import de.berlin.htw.usws.webCrawlers.RecipeCrawlerChefkoch;

import java.io.IOException;
import java.util.LinkedList;

/**
 * Class holding mock-logic and to be deleted once a DB is connected.
 *
 * @since 24.10.2018
 * @author Lucas Larisch
 */
public class MockLogic {

    /** Used for collecting IDs of recipes. */
    private IdCrawlerChefkoch idCrawler;

    /** Used for collecting recipes. */
    private RecipeCrawlerChefkoch recipeCrawler;

    /**
     * Crawls all unknown IDs and the recipe connected to each ID afterwards.
     * Whereas the IDs are collected from newest to oldest, the recipes are
     * parsed starting with the last added ID (= oldest recipe). This algorithm is
     * based on the logic of {@link IdCrawlerChefkoch#crawlRecipePages()}:
     * The last added recipe will be the newest in the database.
     * So in case of an error, only newer IDs will be collected (and the recipes
     * connected to them).
     *
     * @since 24.10.2018
     * @author Lucas Larisch
     */
    public void start() {
        idCrawler = new IdCrawlerChefkoch();

        try {
            LinkedList<String> unknownIds = idCrawler.crawlRecipePages();
            recipeCrawler = new RecipeCrawlerChefkoch();
            for (int i = unknownIds.size()-1; i >= 0; i--) {
                recipeCrawler.scrapRecipe(unknownIds.get(i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
