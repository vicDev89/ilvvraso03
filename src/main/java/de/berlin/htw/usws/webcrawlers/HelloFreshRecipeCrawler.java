package de.berlin.htw.usws.webcrawlers;

import de.berlin.htw.usws.model.Recipe;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Class used for scrapping recipes from hellofresh.de.
 *
 * @since 13.11.2018
 * @author Lucas Larisch
 */
public class HelloFreshRecipeCrawler extends HelloFreshCrawler {

    // TODO Comment attributes below:

    private final String CSS_QUERY_PORTIONS_DESCRIPTION = "div.fela-r7xe7e p";

    private final String CSS_QUERY_SELECTED_PORTIONS = "div.fela-r7xe7e button[checked]";

    private final String CSS_QUERY_IMAGE = "img.fela-1b1idjb";

    private final String CSS_QUERY_ATTRIBUTE_SRC = "src";

    /** CSS Query for Headlines */
    private final String CSS_QUERY_H1 = "h1";

    private final String CSS_QUERY_DURATION_AND_DIFFICULTY = "div.fela-19qpnoj span:not([bold=\"true\"]).mealkit-a";

    private final String REGEX_PORTIONS_DESCRIPTION = "\\/.*für [0-9]* Personen";

    private final String REGEX_NUMBER_ESCAPE = "\\[0-9]\\*";

    private final String TO_REPLACE_MINUTES = " Minuten";

    private int portions;

    /** Recipe to be finally returned. */
    private Recipe recipe;

    /**
     * TODO Comment
     *
     * @since 13.11.2018
     * @author Lucas Larisch
     * @param url
     * @return
     */
    public Recipe scrapRecipe(String url) {
        recipe = null;

        try {
            setUrl(url);
            Document recipePage = getUnlimitedDocument();
            determinePortions(recipePage);
            recipe = new Recipe();
            // TODO: Recipe ID -> string
            // recipe.setId(getRecipeIdFromUrl(url));
            scrapRecipeInformation(recipePage);
        } catch(IOException e) {
            e.printStackTrace();
        }

        return recipe;
    }

    /**
     * TODO Comment
     *
     * @since 13.11.2018
     * @author Lucas Larisch
     * @param recipePage
     * @throws NumberFormatException
     */
    private void determinePortions(Document recipePage) throws NumberFormatException {
        Elements portionsByDescription = recipePage.select(CSS_QUERY_PORTIONS_DESCRIPTION);
        String portionsString;

        if (portionsByDescription.size() != 0) {
            portionsString = portionsByDescription.get(0).text();
            if (portionsString.matches(REGEX_PORTIONS_DESCRIPTION)) {
                String[] regexWithoutNr = REGEX_PORTIONS_DESCRIPTION.split(REGEX_NUMBER_ESCAPE);
                portionsString = portionsString.replaceAll(regexWithoutNr[0], "").replaceAll(regexWithoutNr[1], "");
            }
        } else {
            Elements portionsBySelection = recipePage.select(CSS_QUERY_SELECTED_PORTIONS);
            portionsString = portionsBySelection.get(0).text();
        }
        portions = Integer.parseInt(portionsString);
    }

    /**
     * TODO Comment
     *
     * @since 13.11.2018
     * @author Lucas Larisch
     * @param recipePage
     * @throws NumberFormatException
     */
    private void scrapRecipeInformation(Document recipePage) throws NumberFormatException {
        Element title = recipePage.select(CSS_QUERY_H1).get(0);
        recipe.setTitle(title.text());

        Element pictureUrl = recipePage.select(CSS_QUERY_IMAGE).get(0);
        recipe.setPictureUrl(pictureUrl.attr(CSS_QUERY_ATTRIBUTE_SRC));

        Elements difficultyAndDuration = recipePage.select(CSS_QUERY_DURATION_AND_DIFFICULTY);
        int durationInMinutes = Integer.parseInt(difficultyAndDuration.get(0).text().replace(TO_REPLACE_MINUTES, ""));
        // TODO Only one duration (total duration) for Hello Fresh Recipes
        recipe.setPreparationTimeInMin(durationInMinutes);
        // TODO Difficulty levels: 1-3.
        String difficulty = difficultyAndDuration.get(1).text();
        // recipe.setDifficultyLevel(difficulty);

        /*
        preparation: string; -> List?
        rate: number; -> * 1.25
        ingredientInRecipes: IngredientInRecipe[];
        */
    }

}
