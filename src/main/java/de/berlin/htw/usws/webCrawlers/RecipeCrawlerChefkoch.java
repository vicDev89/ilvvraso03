package de.berlin.htw.usws.webCrawlers;

import de.berlin.htw.usws.model.Recipe;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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

    private Recipe recipe;

    public Recipe scrapRecipe(Long recipeId) {
        super.appendToBaseUrl(RECIPES_APPEND_BEFORE_ID + recipeId + ONE_PORTION_APPEND);

        recipe = null; // TODO: maintain?

        try {
            Document recipePage = getUnlimitedDocument();
            recipe = new Recipe();
            addBasicContentToRecipe(recipePage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return recipe;
    }

    private Recipe addBasicContentToRecipe(Document recipePage) {
        // ID!
        String title = recipePage.select("h1").text();
        String preparation = recipePage.select("div#rezept-zubereitung").text();

        // TODO: Continue here, prevent from deleting * between first and last strong tag. Should use next strong.
        String preparationInfo = recipePage.select("p#preparation-info").first().toString();
        for (String s : preparationInfo.split("<\\/strong>.*<strong>"))
            System.out.println(s);



        return recipe;
    }

}
