package de.berlin.htw.usws.webCrawlers;

import de.berlin.htw.usws.model.DifficultyLevel;
import de.berlin.htw.usws.model.Ingredient;
import de.berlin.htw.usws.model.IngredientsInRecipe;
import de.berlin.htw.usws.model.Recipe;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

/**
 * Class used for scrapping recipes.
 *
 * @since 24.10.2018
 * @author Lucas Larisch
 */
public class RecipeCrawlerChefkoch extends ChefkochCrawler {

    /** String to append to an URL before the recipe ID. */
    private final String RECIPES_APPEND_BEFORE_ID = "/rezepte/";

    /** String to append to an URL in order to get the ingredients for one portion. */
    private final String ONE_PORTION_APPEND = "?portionen=1";

    /** CSS Query for Headlines */
    private final String CSS_QUERY_H1 = "h1";

    /** CSS Query for sources. */
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
    private final String CSS_QUERY_RECIPE_PRERARATION = "div#rezept-zubereitung";
    private final String CSS_QUERY_INGREDIENTS_TD = "table.incredients td";
    private final String CSS_QUERY_CLASS_AMOUNT = "amount";

    private final String REGEX_AVG_RATING = "Ø[1-5],[0-9]{2}";
    private final String REGEX_STRONG_TAGS = "<strong>.+?<\\/strong>";

    // TODO Same as in ID-Crawler
    /** Regex for checking if a string is a (no point) number. */
    private final String REGEX_NUMBER = "[0-9]*";

    private final String KEY_COOKING_TIME = "Koch-/Backzeit:";
    private final String KEY_PREPARATION_TIME = "Arbeitszeit:";
    private final String KEY_RESTING_TIME = "Ruhezeit:";

    private static final HashMap<String, Integer> TIME_CONVERSION = new HashMap<String, Integer>() {{
        put("Min.",1);
        put("Std.",60);
    }};

    private final HashMap<String, DifficultyLevel> DIFFICULTY_LEVELS = new HashMap<String, DifficultyLevel>() {{
        put("simpel", DifficultyLevel.EASY);
        put("normal", DifficultyLevel.MEDIUM);
        put("pfiffig", DifficultyLevel.DIFFICULT);
    }};

    private Recipe recipe;

    public Recipe scrapRecipe(long recipeId) {
        super.appendToBaseUrl(RECIPES_APPEND_BEFORE_ID + recipeId + ONE_PORTION_APPEND);

        recipe = null;

        try {
            Document recipePage = getUnlimitedDocument();
            recipe = new Recipe();
            recipe.setId(recipeId);
            addBasicContentToRecipe(recipePage);
            addPreparationTimesAndDifficultyToRecipe(recipePage);
            addPictureToRecipe(recipePage);
            addIngredientsToRecipe(recipePage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return recipe;
    }

    private void addBasicContentToRecipe(Document recipePage) {
        String title = recipePage.select(CSS_QUERY_H1).text();
        String preparation = recipePage.select(CSS_QUERY_RECIPE_PRERARATION).text();

        recipe.setTitle(title);
        recipe.setPreparation(preparation);

        String averageRating = recipePage.select(CSS_QUERY_SPAN_AVG_RATING).text();
        if(averageRating.matches(REGEX_AVG_RATING)) {
            double ratingAsNr = ratingStringToDouble(averageRating);
            recipe.setRate(ratingAsNr);
        }
    }

    private void addPreparationTimesAndDifficultyToRecipe(Document recipePage) {
        Element preparationInfo = recipePage.select(CSS_QUERY_PREPARATION_INFO).first();

        Elements allStrongPreparationInfo = preparationInfo.select(CSS_QUERY_STRONG);
        String[] allNotStrongPreparationInfo = preparationInfo.toString().split(REGEX_STRONG_TAGS,-2);

        // Only information concerning time:
        for (int i = 0; i < allStrongPreparationInfo.size()-2; i++) {
            String description = allStrongPreparationInfo.get(i).text();
            String value = allNotStrongPreparationInfo[i+1];
            addTimeToRecipe(description, value);
        }

        String unformatedDifficulty = allNotStrongPreparationInfo[allStrongPreparationInfo.size()-1];
        String formatedDifficulty = unformatedDifficulty.replace("/","").trim();
        DifficultyLevel difficultyLevel = DIFFICULTY_LEVELS.get(formatedDifficulty);
        recipe.setDifficultyLevel(difficultyLevel);
    }

    private void addPictureToRecipe(Document recipePage) {
        final String cssQueryPictureUrl = String.format(CSS_QUERY_IMAGE_ALT_FORMAT, recipe.getTitle());
        String pictureUrl = recipePage.select(cssQueryPictureUrl).attr(CSS_QUERY_SRC);

        if (!pictureUrl.isEmpty()) {
            recipe.setPictureUrl(pictureUrl);
        }
    }

    private void addIngredientsToRecipe(Document recipePage) {
        Elements ingredientsTable = recipePage.select(CSS_QUERY_INGREDIENTS_TD);
        List<IngredientsInRecipe> ingredientsInRecipeList = null;

        for (int i = 0; i < ingredientsTable.size(); i++) {
            if (ingredientsTable.get(i).className().equals(CSS_QUERY_CLASS_AMOUNT)) {
                // Since descriptions are written in a <b>-tag:
                if(ingredientsTable.get(i+1).select(CSS_QUERY_B).isEmpty()) {
                    Element amountElement = ingredientsTable.get(i);
                    Element ingredientElement = ingredientsTable.get(i+1);

                    if(ingredientsInRecipeList == null) {
                        ingredientsInRecipeList = new ArrayList<IngredientsInRecipe>();
                    }

                    ingredientsInRecipeList.add(createIngredientInRecipe(amountElement, ingredientElement));
                }
            }
        }
        recipe.setIngredientsInRecipes(ingredientsInRecipeList);
    }

    private void addTimeToRecipe(String description, String value) {
        value = value.replace("ca.","");
        value = value.replace("/", "");
        value = value.trim();
        String[] valueArray = value.split(" ");
        int minutes = 0;
        for (int i = 0; i<valueArray.length-1; i+=2) {
            int valueNumber = Integer.parseInt(valueArray[i]);
            int convValue = TIME_CONVERSION.get(valueArray[i+1]);
            minutes += valueNumber * convValue;
        }

        if (description.equals(KEY_COOKING_TIME)) {
            recipe.setCookingTimeInMin(minutes);
        } else if (description.equals(KEY_PREPARATION_TIME)) {
            recipe.setPreparationTimeInMin(minutes);
        } else if (description.equals(KEY_RESTING_TIME)) {
            recipe.setRestingTimeInMin(minutes);
        } else {
            System.err.println("Description '"+description+"' is not known");
        }

    }

    private double ratingStringToDouble(String averageRating) {
        averageRating = averageRating.replace("Ø","");
        averageRating = averageRating.replace(",",".");
        return Double.parseDouble(averageRating);
    }

    private IngredientsInRecipe createIngredientInRecipe(Element amountElement, Element ingredientElement) {
        IngredientsInRecipe ingredientInRecipe = new IngredientsInRecipe();

        Ingredient ingredient = new Ingredient();
        ingredient.setName(ingredientElement.text());
        ingredientInRecipe.setIngredient(ingredient);

        // All (HTML) white spaces need to be replaced
        String amountText = amountElement.text().replace("\u00a0"," ");
        String[] amountArray = amountText.split(" ", 2);

        if (amountArray.length > 0 && !amountText.trim().isEmpty()) {
            String amountToCheck = amountArray[0].replaceAll(",","");
            if (amountToCheck.matches(REGEX_NUMBER) && !amountToCheck.isEmpty()) {
                ingredientInRecipe.setQuantity(Double.parseDouble(amountArray[0].replaceAll(",", ".")));
            } else {
                // TODO Discuss this error
                System.err.println("'"+amountArray[0]+"' leads to problems. (no amount/double)");
            }
        }
        if (amountArray.length > 1 ) {
            ingredientInRecipe.setMeasure(amountArray[1]);
        }

        ingredientInRecipe.setRecipe(recipe);

        return ingredientInRecipe;
    }
}
