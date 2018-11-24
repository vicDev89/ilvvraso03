package de.berlin.htw.usws.services;


import de.berlin.htw.usws.model.Recipe;
import de.berlin.htw.usws.webcrawlers.chefkoch.ChefkochRecipeCrawler;
import de.berlin.htw.usws.webcrawlers.chefkoch.ChefkochUnknownIdsCrawler;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    @Inject
    private ChefkochUnknownIdsCrawler unknownIdsCrawler;

    /**
     * Used for collecting recipes.
     */
    private ChefkochRecipeCrawler recipeCrawler;

    /**
     * Crawls all unknown IDs and the recipe connected to each ID afterwards.
     * Whereas the IDs are collected from newest to oldest, the recipes are
     * parsed starting with the last added ID (= oldest recipe). This algorithm is
     * based on the logic of {@link ChefkochUnknownIdsCrawler#crawlRecipePages()}:
     * The last added recipe will be the newest in the database.
     * So in case of an error, only newer IDs will be collected (and the recipes
     * connected to them).
     *
     * @author Lucas Larisch
     * @since 24.10.2018
     */
    public List<Recipe> start() {

        List<Recipe> recipes = new ArrayList<>();

        recipeCrawler = new ChefkochRecipeCrawler();

        try {
            ArrayList<Long> unknownIds = this.unknownIdsCrawler.crawlRecipePages();
            for (int i = unknownIds.size() - 1; i >= 0; i--) {
                recipes.add(recipeCrawler.scrapRecipe(unknownIds.get(i)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return recipes;
    }
}
