package de.berlin.htw.usws.webCrawlers;

import de.berlin.htw.usws.model.Recipe;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

public class ChefkochCrawlersTest {

    @Test
    public void crawlRecipes() throws IOException {
        UnknownIdsCrawlerChefkoch idCrawler = new UnknownIdsCrawlerChefkoch();
        RecipeCrawlerChefkoch recipeCrawler = new RecipeCrawlerChefkoch();

        ArrayList<Long> idList = idCrawler.crawlRecipePages();
        for (long id : idList) {
            Recipe recipe = recipeCrawler.scrapRecipe(id);
        }
    }
}
