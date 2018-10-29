package de.berlin.htw.usws.webCrawlers;

import de.berlin.htw.usws.model.Recipe;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

public class ChefkochCrawlersTest {

    @Test
    @Ignore
    public void crawlRecipes() throws IOException {
        UnknownIdsCrawlerChefkoch idCrawler = new UnknownIdsCrawlerChefkoch();
        RecipeCrawlerChefkoch recipeCrawler = new RecipeCrawlerChefkoch();

        ArrayList<Long> idList = new ArrayList<Long>();
        idList.add(3292121488810516L);
        idList.add(3586591539033463L);
//      ArrayList<Long> idList = idCrawler.crawlRecipePages();

         for (long id : idList) {
            Recipe recipe = recipeCrawler.scrapRecipe(id);
        }
    }
}
