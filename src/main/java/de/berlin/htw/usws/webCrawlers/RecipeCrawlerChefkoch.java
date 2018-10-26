package de.berlin.htw.usws.webCrawlers;

import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Class used for scrapping recipes.
 *
 * @since 24.10.2018
 * @author Lucas Larisch
 */
public class RecipeCrawlerChefkoch extends ChefkochCrawler {

    private final String RECIPES_APPEND_BEFORE_ID = "/rezepte/";
    private final String ONE_PORTION_APPEND = "?portionen=1";

    public void scrapRecipe(String recipeId) {
        super.appendToBaseUrl(RECIPES_APPEND_BEFORE_ID + recipeId + ONE_PORTION_APPEND);

        try {
            Document recipePage = getUnlimitedDocument();
            System.out.println(recipePage.title());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
