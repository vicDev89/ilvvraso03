package de.berlin.htw.usws.webcrawlers;

import de.berlin.htw.usws.model.DifficultyLevel;
import de.berlin.htw.usws.model.Ingredient;
import de.berlin.htw.usws.model.IngredientInRecipe;
import de.berlin.htw.usws.model.Recipe;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class used for scrapping recipes from hellofresh.de.
 *
 * @since 13.11.2018
 * @author Lucas Larisch
 */
public class HelloFreshRecipeCrawler extends HelloFreshCrawler {

    /** CSS Query for the description of portions (does not exist on some pages). */
    private final String CSS_QUERY_PORTIONS_DESCRIPTION = "div.fela-r7xe7e p";

    /** CSS Query for the checked button concerning the portions (does not exist on some pages). */
    private final String CSS_QUERY_SELECTED_PORTIONS = "div.fela-r7xe7e button[checked]";

    /** CSS Query for recipe image. */
    private final String CSS_QUERY_RECIPE_IMAGE = "img.fela-1b1idjb";

    /** CSS Query for getting the value of a src-attribute. */
    private final String CSS_QUERY_ATTRIBUTE_SRC = "src";

    /** CSS Query for headlines. */
    private final String CSS_QUERY_H1 = "h1";

    /** CSS Query for the div containing the duration for preparing the recipe as well as its difficulty. */
    private final String CSS_QUERY_DURATION_AND_DIFFICULTY = "div.fela-19qpnoj span:not([bold=\"true\"]).mealkit-a";

    /** CSS Query for each div containing preparation instructions. */
    private final String CSS_QUERY_PREPARATION_INSTRUCTION = "div.fela-1qzip4i";

    /** CSS Query for the rating. */
    private final String CSS_QUERY_RATING_SPAN = "span.fela-2shuhy";

    /** CSS Query for a div containing ingormation about an ingredient. */
    private final String CSS_QUERY_INGREDIENT = "div.fela-1qz307e";

    /** CSS Query for a paragraph. */
    private final String CSS_QUERY_P = "p";

    /** Regex for a description of portions */
    private final String REGEX_PORTIONS_DESCRIPTION = "\\/.*für [0-9]* Personen";

    /** Regex for the rating of a recipe. */
    private final String REGEX_RATING = "[0-9\\.]* \\/ [0-9]*";

    /** Escaped regex for checking if a value is a (non-floating) number. This escaped regex is used in order to match with a real regex string. (e.g. does regex x contain a checking on numbers) */
    private final String REGEX_INTEGER_ESCAPE = "\\[0-9]\\*";

    /** Regex for an a string containing both quantity and measure. */
    private final String REGEX_QUANTITY_MEASURE = "([0-9]+(\\.[0-9]+)?) [^0-9]+";

    /** Escaped regex for a slash. */
    private final String REGEX_SLASH_ESCAPE = "\\/";

    /** String to be replaced for working with minutes. */
    private final String TO_REPLACE_MINUTES = " Minuten";

    /** String to be replaced in order to work with a rating. */
    private final String TO_REPLACE_RATED_WITH = "Bewertet mit ";

    /** The value a rating is to be normalized to. (Max value of a normalized rating) */
    private final double RATING_NORM = 5;

    /** Map containing a character standing for a floating number as key and its value as double. */
    private final HashMap<Character, Double> AMOUNT_CONVERSION = new HashMap<Character, Double>(){{
        put('¼', 1.0/4.0);
        put('½', 1.0/2.0);
        put('¾', 3.0/4.0);
        put('⅓', 1.0/3.0);
        put('⅔', 2.0/3.0);
        put('⅕', 1.0/5.0);
        put('⅖', 2.0/5.0);
        put('⅗', 3.0/5.0);
        put('⅘', 4.0/5.0);
        put('⅙', 1.0/6.0);
        put('⅚', 5.0/6.0);
        put('⅛', 1.0/8.0);
        put('⅜', 3.0/8.0);
        put('⅝', 5.0/8.0);
        put('⅞', 7.0/8.0);
    }};

    /** Map containing a string standing for a difficulty as named on hellofresh.de as key and its respective {@link DifficultyLevel}. */
    private final HashMap<String, DifficultyLevel> DIFFICULTY_LEVEL_CONVERSION = new HashMap<String, DifficultyLevel>(){{
       put("Stufe 1", DifficultyLevel.EASY);
       put("Stufe 2", DifficultyLevel.MEDIUM);
       put("Stufe 3", DifficultyLevel.DIFFICULT);
    }};

    /** The portions the ingredients are listed for. Used for converting each ingredient to a recipe for one person. */
    private int portions;

    /** Recipe to be finally returned. */
    private Recipe recipe;

    /**
     * Returns a {@link Recipe} scrapped from hellofresh.de and converted to one portion.
     *
     * @since 13.11.2018
     * @author Lucas Larisch
     * @param url The url to the recipe page.
     * @return A recipe scrapped from hellofresh.de and converted to one portion.
     */
    public Recipe scrapRecipe(String url) {
        recipe = null;
        try {
            setUrl(url);
            Document recipePage = getUnlimitedDocument();
            determinePortions(recipePage);
            recipe = new Recipe();
            // TODO: [DB] Recipe ID -> string
            // recipe.setId(getRecipeIdFromUrl(url));
            scrapAllRecipeInformation(recipePage);
        } catch(IOException e) {
            e.printStackTrace();
        }

        return recipe;
    }

    /**
     * Scraps the portions the ingredients are listed for and finally sets
     * {@link HelloFreshRecipeCrawler#portions} to this number.
     *
     * @since 13.11.2018
     * @author Lucas Larisch
     * @param recipePage The recipe page as document.
     * @throws NumberFormatException
     */
    private void determinePortions(Document recipePage) throws NumberFormatException {
        Elements portionsByDescription = recipePage.select(CSS_QUERY_PORTIONS_DESCRIPTION);
        String portionsString;

        if (portionsByDescription.size() != 0) {
            // True, if the amount of portions cannot be selected:
            portionsString = portionsByDescription.get(0).text();
            if (portionsString.matches(REGEX_PORTIONS_DESCRIPTION)) {
                String[] regexWithoutNr = REGEX_PORTIONS_DESCRIPTION.split(REGEX_INTEGER_ESCAPE);
                portionsString = portionsString.replaceAll(regexWithoutNr[0], "").replaceAll(regexWithoutNr[1], "");
            }
        } else {
            // If the amount of portions is selectable, the selected button will be taken as value:
            Elements portionsBySelection = recipePage.select(CSS_QUERY_SELECTED_PORTIONS);
            portionsString = portionsBySelection.get(0).text();
        }
        portions = Integer.parseInt(portionsString);
    }

    /**
     * Scraps all recipe information.
     *
     * @since 13.11.2018
     * @author Lucas Larisch
     * @param recipePage The recipe page as document.
     * @throws NumberFormatException
     */
    private void scrapAllRecipeInformation(Document recipePage) {
        scrapTitle(recipePage);
        scrapPictureUrl(recipePage);
        scrapDifficultyAndDuration(recipePage);
        scrapPreparationInstruction(recipePage);
        scrapRating(recipePage);
        scrapIngredients(recipePage);
    }

    /**
     * Scraps the title of the recipe and sets it for {@link HelloFreshRecipeCrawler#recipe}.
     *
     * @since 15.11.2018
     * @author Lucas Larisch
     * @param recipePage The recipe page as document.
     */
    private void scrapTitle(Document recipePage) {
        Element title = recipePage.select(CSS_QUERY_H1).get(0);
        recipe.setTitle(title.text());
    }

    /**
     * Scraps the url to the image for the recipe and sets it for {@link HelloFreshRecipeCrawler#recipe}.
     *
     * @since 15.11.2018
     * @author Lucas Larisch
     * @param recipePage The recipe page as document.
     */
    private void scrapPictureUrl(Document recipePage) {
        Element pictureUrl = recipePage.select(CSS_QUERY_RECIPE_IMAGE).get(0);
        recipe.setPictureUrl(pictureUrl.attr(CSS_QUERY_ATTRIBUTE_SRC));
    }

    /**
     * Scraps both difficulty and the duration for preparing the meal and sets these
     * values for {@link HelloFreshRecipeCrawler#recipe}.
     *
     * @since 15.11.2018
     * @author Lucas Larisch
     * @param recipePage The recipe page as document.
     */
    private void scrapDifficultyAndDuration(Document recipePage) {
        Elements difficultyAndDuration = recipePage.select(CSS_QUERY_DURATION_AND_DIFFICULTY);
        int durationInMinutes = Integer.parseInt(difficultyAndDuration.get(0).text().replace(TO_REPLACE_MINUTES, ""));
        // TODO: [DB] Only one duration (total duration) for Hello Fresh Recipes
        recipe.setPreparationTimeInMin(durationInMinutes);
        // TODO: [DB] Difficulty levels: 1-3 / Discuss assumption: 1 = EASY, 2 = MEDIUM, 3 = DIFFICULT?
        DifficultyLevel difficulty = DIFFICULTY_LEVEL_CONVERSION.get(difficultyAndDuration.get(1).text());
        recipe.setDifficultyLevel(difficulty);
    }

    /**
     * Scraps the instructions for preparing the meal and sets them for {@link HelloFreshRecipeCrawler#recipe}.
     *
     * @since 15.11.2018
     * @author Lucas Larisch
     * @param recipePage The recipe page as document.
     */
    private void scrapPreparationInstruction(Document recipePage) {
        Elements preparationInstructions = recipePage.select(CSS_QUERY_PREPARATION_INSTRUCTION);
        // TODO: [DB] Array or string?
        // String[] preparation = new String[preparationInstructions.size()];
        String preparation = "";
        for (int i = 0; i < preparationInstructions.size(); i++) {
            // preparation[i] = preparationInstructions.get(i).text();
            // String[] above (comment), string below:
            if (i > 0) {
                preparation += "\n";
            }
            preparation += preparationInstructions.get(i).text();
        }
        recipe.setPreparation(preparation);
    }

    /**
     * Scraps the rating of a recipe, normalizes it relative to
     * {@link HelloFreshRecipeCrawler#RATING_NORM} and sets the
     * normalized value for {@link HelloFreshRecipeCrawler#recipe}.
     *
     * @since 15.11.2018
     * @author Lucas Larisch
     * @param recipePage The recipe page as document.
     */
    private void scrapRating(Document recipePage) {
        Elements ratingSpan = recipePage.select(CSS_QUERY_RATING_SPAN);

        if (ratingSpan.size() > 0) {
            String rating = ratingSpan.get(0).text().replace(TO_REPLACE_RATED_WITH, "");
            if (rating.matches(REGEX_RATING)) {
                String[] splittedRating = rating.split(REGEX_SLASH_ESCAPE);
                double actualRating = Double.parseDouble(splittedRating[0]);
                double maxRating = Double.parseDouble(splittedRating[1]);
                recipe.setRate(actualRating * (RATING_NORM / maxRating));
            } else {
                System.err.println("Rating span is not null, but its text does not match the rating regex.");
            }
        }
    }

    /**
     * Scraps the ingredients needed in order to be able to prepare the meal and sets them as
     * values for {@link HelloFreshRecipeCrawler#recipe}.
     *
     * @since 15.11.2018
     * @author Lucas Larisch
     * @param recipePage The recipe page as document.
     */
    private void scrapIngredients(Document recipePage) {
        ArrayList<IngredientInRecipe> ingredientsInRecipe = new ArrayList<IngredientInRecipe>();
        Elements ingredientElements = recipePage.select(CSS_QUERY_INGREDIENT);

        for (Element ingredient: ingredientElements) {
            Elements ingredientParagraph = ingredient.select(CSS_QUERY_P);
            ingredientsInRecipe.add(scrapIngredientInRecipe(ingredientParagraph));
        }

        recipe.setIngredientInRecipes(ingredientsInRecipe);
    }

    /**
     * Scraps information about a recipe from a paragraph (Type: {@link Element}) given in as param and
     * stores it in an object that will finally be returned.
     * The quantity needed for the amount of portions stored in {@link HelloFreshRecipeCrawler#portions}
     * is normalized to the quantity needed for one portion. In case of information concerning quantity/
     * measure not matching {@link HelloFreshRecipeCrawler#REGEX_QUANTITY_MEASURE}, the ingredient will
     * be regarded as one that can be used according to taste (-> No quantity/measure saved).
     *
     * @since 15.11.2018
     * @author Lucas Larisch
     * @param ingredientParagraphs Paragraph containing information about a recipe.
     * @return Ingredient in recipe with information scapped from hellofresh.de.
     */
    private IngredientInRecipe scrapIngredientInRecipe(Elements ingredientParagraphs) {
        String quantityAndMeasure = selectQuantityAndMeasure(ingredientParagraphs.get(0));
        String ingredientName = ingredientParagraphs.get(1).text().replace("*","");

        IngredientInRecipe ingredientInRecipe = new IngredientInRecipe();
        ingredientInRecipe.setIngredient(new Ingredient(ingredientName));
        ingredientInRecipe.setRecipe(recipe);

        if (quantityAndMeasure.matches(REGEX_QUANTITY_MEASURE)) {
            String[] quantityAndMeasureSplitted = quantityAndMeasure.trim().split(" ", 2);
            double quantity = Double.parseDouble(quantityAndMeasureSplitted[0]);
            String measure = quantityAndMeasureSplitted[1].trim();
            ingredientInRecipe.setQuantity(quantity / portions); // for one person
            ingredientInRecipe.setMeasure(measure);
        } else {
            System.err.println("'" + ingredientName + "' will be saved as ingredient without quantity and measure ('" + quantityAndMeasure + "' does not match regex).");
        }
        return ingredientInRecipe;
    }

    /**
     * Takes the text of a paragraph containing information about quantity and measure
     * of an ingredient and returns it. In case of the first character in this string
     * being a symbol standing for a floating number, this value will be converted to
     * a floating number (see {@link HelloFreshRecipeCrawler#AMOUNT_CONVERSION}).
     *
     * @since 15.11.2018
     * @author Lucas Larisch
     * @param quantityAndMeasureParagraph Paragraph (Type: {@link Element}) containing information
     *                                    concerning amount and measure.
     * @return (Normalized) string concerning amount and measure.
     */
    private String selectQuantityAndMeasure(Element quantityAndMeasureParagraph){
        String quantityAndMeasure = quantityAndMeasureParagraph.text();
        if (quantityAndMeasure.length() > 0 && AMOUNT_CONVERSION.get(quantityAndMeasure.charAt(0)) != null) {
            quantityAndMeasure = quantityAndMeasure.replace(""+quantityAndMeasure.charAt(0), ""+AMOUNT_CONVERSION.get(quantityAndMeasure.charAt(0)));
        }
        return quantityAndMeasure;
    }

}
