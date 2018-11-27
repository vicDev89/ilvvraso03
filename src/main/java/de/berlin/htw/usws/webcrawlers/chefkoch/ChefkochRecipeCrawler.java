package de.berlin.htw.usws.webcrawlers.chefkoch;

import de.berlin.htw.usws.model.enums.DifficultyLevel;
import de.berlin.htw.usws.model.Ingredient;
import de.berlin.htw.usws.model.IngredientInRecipe;
import de.berlin.htw.usws.model.Recipe;
import de.berlin.htw.usws.model.enums.RecipeSite;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

/**
 * Class used for scrapping recipes from chefkoch.de.
 *
 * @since 24.10.2018
 * @author Lucas Larisch
 */
public class ChefkochRecipeCrawler extends ChefkochCrawler {

    /** String to append to an URL before the recipe ID. */
    private final String RECIPES_APPEND_BEFORE_ID = "/rezepte/";

    /** String to append to an URL in order to get the ingredients for one portion. */
    private final String ONE_PORTION_APPEND = "?portionen=1";

    /** CSS Query for headlines */
    private final String CSS_QUERY_H1 = "h1";

    /** CSS Query for getting the value of a src-attribute. */
    private final String CSS_QUERY_SRC = "src";

    /** CSS Query for strong tags. */
    private final String CSS_QUERY_STRONG = "strong";

    /** CSS Query for bold tags. */
    private final String CSS_QUERY_B = "b";

    /** CSS Query for the span containing the average rating of a recipe. */
    private final String CSS_QUERY_SPAN_AVG_RATING = "span.rating__average-rating";

    /** CSS Query for the paragraph containing the preparation info. */
    private final String CSS_QUERY_PREPARATION_INFO = "p#preparation-info";

    /** CSS Query for getting an alternative text for an image. Has to be called with String.format() in order to set the alternative text to be searched for. */
    private final String CSS_QUERY_IMAGE_ALT_FORMAT = "img[alt=%s]";

    /** CSS Query for the div containing the instructions for preparation. */
    private final String CSS_QUERY_RECIPE_PREPARATION = "div#rezept-zubereitung";

    /** CSS Query for a cell of the table containing the ingredients. */
    private final String CSS_QUERY_INGREDIENTS_TD = "table.incredients td";

    /** CSS Query for the class representing an amount. */
    private final String CSS_QUERY_CLASS_AMOUNT = "amount";

    /** Regex for an average rating. */
    private final String REGEX_AVG_RATING = "Ø[1-5],[0-9]{2}";

    /** Regex for a text surrounded by the strong tag. */
    private final String REGEX_INSIDE_STRONG_TAG = "<strong>.+?<\\/strong>";

    /** Regex for checking if a string is a (no point) number. */
    private final String REGEX_NUMBER = "[0-9]*";

    /** Key (Chefkoch) for the cooking time. */
    private final String KEY_COOKING_TIME = "Koch-/Backzeit:";

    /** Key (Chefkoch) for the preparation time. */
    private final String KEY_PREPARATION_TIME = "Arbeitszeit:";

    /** Key (Chefkoch) for the resting time. */
    private final String KEY_RESTING_TIME = "Ruhezeit:";

    /** HashMap containing values for converting time to minutes (Key -> Name of the unit, Value -> Unit in minutes). */
    private static final HashMap<String, Integer> TIME_CONVERSION = new HashMap<String, Integer>() {{
        put("Min.",1);
        put("Std.",60);
    }};

    /** HashMap containing the difficulty levels named on Chefkoch as keys and the corresponding {@link DifficultyLevel} as values. */
    private final HashMap<String, DifficultyLevel> DIFFICULTY_LEVELS = new HashMap<String, DifficultyLevel>() {{
        put("simpel", DifficultyLevel.EASY);
        put("normal", DifficultyLevel.MEDIUM);
        put("pfiffig", DifficultyLevel.DIFFICULT);
    }};

    /** Recipe to be finally returned. */
    private Recipe recipe;

    /**
     * Crawls a recipe-page and stores all information in an object that will
     * be returned.
     *
     * @since 02.11.2018
     * @author Lucas Larisch
     * @param recipeId Id of the recipe to be scrapped.
     * @return Recipe scrapped from Chefkoch.de.
     */
    public Recipe scrapRecipe(long recipeId) {
        super.appendToBaseUrl(RECIPES_APPEND_BEFORE_ID + recipeId + ONE_PORTION_APPEND);

        recipe = null;

        try {
            Document recipePage = getUnlimitedDocument();
            recipe = new Recipe();
            recipe.setIdentifier(String.valueOf(recipeId));
            addBasicContentToRecipe(recipePage);
            addPreparationTimesAndDifficultyToRecipe(recipePage);
            addPictureToRecipe(recipePage);
            addIngredientsToRecipe(recipePage);
            recipe.setRecipeSite(RecipeSite.CHEFKOCH);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return recipe;
    }

    /**
     * Crawls the page given in as a param and adds basic content to {@link ChefkochRecipeCrawler#recipe}.
     * That is: title, preparation instructions and the average rating (if existing).
     *
     * @since 02.11.2018
     * @author Lucas Larisch
     * @param recipePage Document of the recipe page.
     */
    private void addBasicContentToRecipe(Document recipePage) {
        String title = recipePage.select(CSS_QUERY_H1).text();
        String preparation = recipePage.select(CSS_QUERY_RECIPE_PREPARATION).text();

        recipe.setTitle(title);
        recipe.setPreparation(preparation);

        String averageRating = recipePage.select(CSS_QUERY_SPAN_AVG_RATING).text();
        if(averageRating.matches(REGEX_AVG_RATING)) {
            recipe.setRate(stringToDouble(averageRating));
        }
    }

    /**
     * Crawls the page given in as a param and adds preparation times and the difficulty
     * to {@link ChefkochRecipeCrawler#recipe}.
     *
     * @since 02.11.2018
     * @author Lucas Larisch
     * @param recipePage Document of the recipe page.
     */
    private void addPreparationTimesAndDifficultyToRecipe(Document recipePage) {
        Element preparationInfo = recipePage.select(CSS_QUERY_PREPARATION_INFO).first();

        Elements allStrongPreparationInfo = preparationInfo.select(CSS_QUERY_STRONG);
        String[] allNotStrongPreparationInfo = preparationInfo.toString().split(REGEX_INSIDE_STRONG_TAG,-2);

        // Only information concerning time:
        int minutes = 0;
        for (int i = 0; i < allStrongPreparationInfo.size()-2; i++) {
            String description = allStrongPreparationInfo.get(i).text();
            String value = allNotStrongPreparationInfo[i+1];
            minutes += getTimeForRecipe(description, value);
        }
        recipe.setPreparationTimeInMin(minutes);

        String unformatedDifficulty = allNotStrongPreparationInfo[allStrongPreparationInfo.size()-1];
        String formatedDifficulty = unformatedDifficulty.replace("/","").trim();
        DifficultyLevel difficultyLevel = DIFFICULTY_LEVELS.get(formatedDifficulty);
        recipe.setDifficultyLevel(difficultyLevel);
    }

    /**
     * Crawls the page given in as a param and adds an URL (picture) to {@link ChefkochRecipeCrawler#recipe}
     * if it exists.
     *
     * @since 02.11.2018
     * @author Lucas Larisch
     * @param recipePage Document of the recipe page.
     */
    private void addPictureToRecipe(Document recipePage) {
        final String cssQueryPictureUrl = String.format(CSS_QUERY_IMAGE_ALT_FORMAT, recipe.getTitle());
        String pictureUrl = recipePage.select(cssQueryPictureUrl).attr(CSS_QUERY_SRC);

        if (!pictureUrl.isEmpty()) {
            recipe.setPictureUrl(pictureUrl);
        }
    }

    /**
     * Crawls the page given in as a param and adds the ingredients to {@link ChefkochRecipeCrawler#recipe}.
     *
     * @since 02.11.2018
     * @author Lucas Larisch
     * @param recipePage Document of the recipe page.
     */
    private void addIngredientsToRecipe(Document recipePage) {
        Elements ingredientsTable = recipePage.select(CSS_QUERY_INGREDIENTS_TD);
        List<IngredientInRecipe> ingredientInRecipeList = null;

        for (int i = 0; i < ingredientsTable.size(); i++) {
            if (ingredientsTable.get(i).className().equals(CSS_QUERY_CLASS_AMOUNT)) {
                // Since descriptions are written in a <b>-tag:
                if(ingredientsTable.get(i+1).select(CSS_QUERY_B).isEmpty()) {
                    Element amountElement = ingredientsTable.get(i);
                    Element ingredientElement = ingredientsTable.get(i+1);

                    if(ingredientInRecipeList == null) {
                        ingredientInRecipeList = new ArrayList<IngredientInRecipe>();
                    }

                    ingredientInRecipeList.add(createIngredientInRecipe(amountElement, ingredientElement));
                }
            }
        }
        recipe.setIngredientInRecipes(ingredientInRecipeList);
    }

    /**
     * Converts time to minutes and adds it to {@link ChefkochRecipeCrawler#recipe}.
     * The converted value will be set for what is specified in the description.
     *
     * @since 02.11.2018
     * @author Lucas Larisch
     * @param description Description of what takes the specified time.
     * @param value Time to be added.
     */
    private int getTimeForRecipe(String description, String value) {
        if(value != null) {
            value = value.replace("ca.","");
            value = value.replace("/", "");
            value = value.trim();
            String[] valueArray = value.split(" ");
            int minutes = 0;
            for (int i = 0; i<valueArray.length-1; i+=2) {
                int valueNumber = Integer.parseInt(valueArray[i]);
//                int convValue = TIME_CONVERSION.get(valueArray[i+1]);
                minutes += valueNumber * 1;
            }
            return minutes;
        } else {
            return 0;
        }


//        if (description.equals(KEY_COOKING_TIME)) {
//            recipe.setCookingTimeInMin(minutes);
//        } else if (description.equals(KEY_PREPARATION_TIME)) {
//            recipe.setPreparationTimeInMin(minutes);
//        } else if (description.equals(KEY_RESTING_TIME)) {
//            recipe.setRestingTimeInMin(minutes);
//        } else {
//            System.err.println("Description '"+description+"' is not known");
//        }

    }

    /**
     * Returns an ingredient used in the recipe as object.
     *
     * @since 02.11.2018
     * @author Lucas Larisch
     * @param amountElement Element representing the amount of the ingredient needed for the recipe.
     * @param ingredientElement Element representing the ingredient itself.
     * @return Scrapped ingredient with converted values.
     */
    private IngredientInRecipe createIngredientInRecipe(Element amountElement, Element ingredientElement) {
        IngredientInRecipe ingredientInRecipe = new IngredientInRecipe();

        Ingredient ingredient = new Ingredient();
        ingredient.setName(ingredientElement.text());
        ingredientInRecipe.setIngredient(ingredient);

        // All (HTML) white spaces need to be replaced
        String amountText = amountElement.text().replace("\u00a0"," ");
        String[] amountArray = amountText.split(" ", 2);

        if (amountArray.length > 0 && !amountText.trim().isEmpty()) {
            String amountToCheck = amountArray[0].replaceAll(",","");
            if (amountToCheck.matches(REGEX_NUMBER) && !amountToCheck.isEmpty()) {
                ingredientInRecipe.setQuantity(stringToDouble(amountArray[0]));
            } else {
                System.err.println("'"+amountArray[0]+"' leads to problems. (no amount/double)");
            }
        }
        if (amountArray.length > 1 ) {
            ingredientInRecipe.setMeasure(amountArray[1]);
        }

        ingredientInRecipe.setRecipe(recipe);

        return ingredientInRecipe;
    }

    /**
     * Converts a string to a double and returns it.
     *
     * @since 02.11.2018
     * @author Lucas Larisch
     * @param string String to be retuned.
     * @return value of the string as double.
     */
    private double stringToDouble(String string) {
        // Only for avg-rates:
        string = string.replace("Ø","");
        string = string.replace(",",".");
        return Double.parseDouble(string);
    }
}
