package de.berlin.htw.usws.webcrawlers.foodboom;

import de.berlin.htw.usws.model.Ingredient;
import de.berlin.htw.usws.model.IngredientInRecipe;
import de.berlin.htw.usws.model.Recipe;
import de.berlin.htw.usws.model.enums.DifficultyLevel;
import de.berlin.htw.usws.model.enums.RecipeSite;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Class for crawling information of a FoodBoom-recipe.
 *
 * @author Lucas Larisch
 * @since 04.12.2018
 */
public class FoodBoomRecipeCrawler extends FoodBoomCrawler {

    /**
     * CSS Query for headlines.
     */
    private final String CSS_QUERY_H1 = "h1";

    /**
     * CSS Query for getting the recipe image.
     */
    private final String CSS_QUERY_RECIPE_IMAGE = "img.card-image__img";

    /**
     * CSS Query for getting the value of a src-attribute.
     */
    private final String CSS_QUERY_ATTRIBUTE_SRC = "src";

    /**
     * CSS Query for tables in the recipe page.
     */
    private final String CSS_QUERY_TABLE_IN_PAGE = "table.table.table--with-line.node.node--type-recipe.node--view-mode-full";

    /**
     * CSS Query for the ingredient table.
     */
    private final String CSS_QUERY_TABLE_INGREDIENTS = "table.table.table--no-spacing.table--checkboxes";

    /**
     * CSS Query for getting an table body.
     */
    private final String CSS_QUERY_TBODY = "tbody";

    /**
     * CSS Query for getting table rows.
     */
    private final String CSS_QUERY_TR = "tr";

    /**
     * CSS Query for getting table cells.
     */
    private final String CSS_QUERY_TD = "td";

    /**
     * CSS Query for getting divs.
     */
    private final String CSS_QUERY_DIV = "div";

    /**
     * CSS Query for getting spans.
     */
    private final String CSS_QUERY_SPAN = "span";

    /**
     * CSS Query for getting an ng-init attribute.
     */
    private final String CSS_QUERY_ATTRIBUTE_NG_INIT = "ng-init";

    /**
     * CSS Query for getting divs containing preparation instructions.
     */
    private final String CSS_QUERY_P_INSTRUCTION = "div.box.box-with-counter.box--large p";

    /**
     * Map containing a string standing for a difficulty as named on hellofresh.de as key and its respective {@link DifficultyLevel}.
     */
    private final HashMap<String, DifficultyLevel> DIFFICULTY_LEVEL_CONVERSION = new HashMap<String, DifficultyLevel>() {{
        put("Leicht", DifficultyLevel.EASY);
        put("Mittel", DifficultyLevel.MEDIUM);
        put("Schwer", DifficultyLevel.DIFFICULT);
    }};

    /**
     * String to append to an url of a recipe page in order to get the ingredients calculated to one portion.
     */
    private final String TO_APPEND_ONE_PORTION = "?portions=1";

    /**
     * To be set as measure if no measure is stated.
     */
    private final String MEASURE_IF_NULL = "Stück";

    /**
     * Recipe to be finally returned.
     */
    private Recipe recipe;

    /**
     * Returns a {@link Recipe} scrapped from foodboom.de and converted to one portion.
     *
     * @param relativeUrl The relative url to the recipe page.
     * @return A recipe scrapped from foodboom.de (one portion).
     * @author Lucas Larisch
     * @since 04.12.2018
     */
    public Recipe scrapRecipe(String relativeUrl) {
        recipe = null;
        try {
            appendToBaseUrl(relativeUrl + TO_APPEND_ONE_PORTION);
            Document recipePage = getUnlimitedDocument();
            recipe = new Recipe();
            recipe.setUrl(relativeUrl);
            recipe.setIdentifier(getRecipeIdFromRelativeUrl(relativeUrl));
            recipe.setRecipeSite(RecipeSite.FOODBOOM);
            scrapAllRecipeInformation(recipePage);
            System.out.println("Recipe successfully scrapped: " + recipe.getTitle());
        } catch (IOException e) {
            System.out.println("Recipe error: " + getUrl());
            e.printStackTrace();
        }
        return recipe;
    }

    /**
     * Calls all methods that scrap information and set it for {@link FoodBoomRecipeCrawler#recipe}.
     *
     * @param recipePage Recipe page.
     * @author Lucas Larisch
     * @since 04.12.2018
     */
    private void scrapAllRecipeInformation(Document recipePage) {
        scrapTitle(recipePage);
        scrapPictureUrl(recipePage);
        scrapPreparationInstruction(recipePage);
        scrapDifficultyAndDuration(recipePage);
        scrapIngredients(recipePage);
    }

    /**
     * Scraps the title of the recipe and sets it for {@link FoodBoomRecipeCrawler#recipe}.
     *
     * @param recipePage Recipe page.
     * @author Lucas Larisch
     * @since 04.12.2018
     */
    private void scrapTitle(Document recipePage) {
        Elements elements = recipePage.select(CSS_QUERY_H1);
        if (elements.size() > 0) {
            Element title = elements.get(0);
            recipe.setTitle(title.text());
        }
    }

    /**
     * Scraps the URL for the picture of the recipe and sets it for {@link FoodBoomRecipeCrawler#recipe}.
     *
     * @param recipePage Recipe page.
     * @author Lucas Larisch
     * @since 04.12.2018
     */
    private void scrapPictureUrl(Document recipePage) {
        Elements elements = recipePage.select(CSS_QUERY_RECIPE_IMAGE);
        if (elements.size() > 0) {
            Element pictureUrl = elements.get(0);
            recipe.setPictureUrl(pictureUrl.attr(CSS_QUERY_ATTRIBUTE_SRC));
        }
    }

    /**
     * Scraps both difficulty (normed) and the duration for preparing the recipe
     * and sets it for {@link FoodBoomRecipeCrawler#recipe}.
     *
     * @param recipePage Recipe page.
     * @author Lucas Larisch
     * @since 04.12.2018
     */
    private void scrapDifficultyAndDuration(Document recipePage){
        Elements tables = recipePage.select(CSS_QUERY_TABLE_IN_PAGE);
        if(tables.size() > 0) {
            Elements tBodies = tables.get(0).select(CSS_QUERY_TBODY);
            if (tBodies.size() > 1) {
                Elements tRows = tBodies.get(1).select(CSS_QUERY_TR);
                if (tRows.size() > 1) {
                    // Dealing with the (total) duration tr:
                    Elements tdDuration = tRows.get(0).select(CSS_QUERY_TD);
                    if (tdDuration.size() > 1) {
                        // Always minutes:
                        String durationNrOnly = tdDuration.get(1).text().split(" ")[0];
                        try{
                            recipe.setPreparationTimeInMin(Integer.parseInt(durationNrOnly));
                        } catch(NumberFormatException e) {
                            System.err.println("Error when parsing duration");
                        }

                    }
                    // Dealing with the difficulty tr:
                    Elements tdDifficulty = tRows.get(1).select(CSS_QUERY_TD);
                    if (tdDifficulty.size() > 1) {
                        DifficultyLevel difficulty = DIFFICULTY_LEVEL_CONVERSION.get(tdDifficulty.get(1).text());
                        recipe.setDifficultyLevel(difficulty);
                    }
                }
            }
        }
    }

    /**
     * Scraps all ingredients of the recipe and sets them for {@link FoodBoomRecipeCrawler#recipe}.
     * {@link FoodBoomRecipeCrawler#MEASURE_IF_NULL} will be set as measure if no measure, but a
     * quantity is stated. In case of none given, the ingredient will be saved without quantity/measure.
     * All ingredients will be saved in plural.
     *
     * @param recipePage Recipe page.
     * @author Lucas Larisch
     * @since 04.12.2018
     */
    private void scrapIngredients(Document recipePage) {
        List<IngredientInRecipe> ingredientsInRecipe = new ArrayList<IngredientInRecipe>();
        Elements ingredientTables = recipePage.select(CSS_QUERY_TABLE_INGREDIENTS);
        if(ingredientTables.size() > 0) {
            Elements rowsOfIngredientTable = ingredientTables.select(CSS_QUERY_TR);
            if (rowsOfIngredientTable.size() > 0) {
                for (Element row : rowsOfIngredientTable) {
                    // Fix to avoid getting title row like "Für die Streusel"
                    if(!row.hasClass("table__group")) {
                        IngredientInRecipe ingredientInRecipe = new IngredientInRecipe();
                        ingredientInRecipe.setRecipe(recipe);
                        Elements cellsOfIngredientRow = row.select(CSS_QUERY_TD);
                        if (cellsOfIngredientRow.size() == 2) {
                            // Handling the ingredient name:
                            Elements sgPlIngredientNames = cellsOfIngredientRow.last().select(CSS_QUERY_DIV);
                            if (sgPlIngredientNames.size() > 0) {
                                String ingredientName = sgPlIngredientNames.last().text();
                                ingredientInRecipe.setIngredient(new Ingredient(ingredientName));
                            }
                            // Handling quantity and measure:
                            Elements quantityInformation = cellsOfIngredientRow.first().select(CSS_QUERY_DIV);
                            if (quantityInformation.size() > 0) {
                                String ngInitAttribute = quantityInformation.first().attr(CSS_QUERY_ATTRIBUTE_NG_INIT);
                                if (!ngInitAttribute.isEmpty()){
                                    double quantity = readQuantityOfAttribute(ngInitAttribute);
                                    if (quantity > 0) {
                                        String measure = readMeasureOfIngredient(quantityInformation.first());
                                        ingredientInRecipe.setQuantity(quantity);
                                        if (measure != null && !measure.isEmpty()) {
                                            ingredientInRecipe.setMeasure(measure);
                                        } else {
                                            ingredientInRecipe.setMeasure(MEASURE_IF_NULL);
                                        }
                                    }
                                } else {
                                    System.err.println("An error occurred while trying to scrap an ingredient for recipe "+recipe.getTitle()+".");
                                }
                            }
                        }
                        ingredientsInRecipe.add(ingredientInRecipe);
                    }
                }
                recipe.setIngredientInRecipes(ingredientsInRecipe);
            }
        }
    }

    /**
     * Reads a quantity set in the document via ng-init.
     *
     * @param ngInitAttribute Attribute containing ng-init.
     * @return Quantity taken out of the attribute.
     * @author Lucas Larisch
     * @since 04.12.2018
     */
    private double readQuantityOfAttribute(String ngInitAttribute) {
        double quantity = 0;
        String firstOperation = ngInitAttribute.split(";")[0];
        if (firstOperation.contains("=")) {
            String operationValue = firstOperation.split("=")[1];
            quantity = Double.parseDouble(operationValue);
        }
        return quantity;
    }

    /**
     * Scraps a measure for an ingredient and returns it (always singular).
     *
     * @param quantityInformation Element containing information concerning quantity and measure.
     * @return The measure of an ingredient (always singular).
     * @author Lucas Larisch
     * @since 04.12.2018
     */
    private String readMeasureOfIngredient(Element quantityInformation) {
        String measure = null;
        Elements informationSpans = quantityInformation.select(CSS_QUERY_SPAN);
        if (informationSpans.size()>1) {
            measure = informationSpans.last().text();
        }
        return measure;
    }

    /**
     * Scraps all preparation instructions, puts them in one string and
     * which is finally set for {@link FoodBoomRecipeCrawler#recipe}.
     *
     * @param recipePage Recipe page.
     * @author Lucas Larisch
     * @since 04.12.2018
     */
    private void scrapPreparationInstruction(Document recipePage) {
        Elements instructions = recipePage.select(CSS_QUERY_P_INSTRUCTION);
        String instructionsString = "";
        if (instructions.size() > 0) {
            for (Element instruction : instructions) {
                if(!instructionsString.isEmpty() && instructionsString.charAt(instructionsString.length()-1) != ' ' && !instruction.text().isEmpty()) {
                    instructionsString += " ";
                }
                instructionsString += instruction.text();
            }
            recipe.setPreparation(instructionsString);
        }
    }

}
