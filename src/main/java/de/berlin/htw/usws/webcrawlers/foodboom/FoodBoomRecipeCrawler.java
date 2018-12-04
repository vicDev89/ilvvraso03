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

public class FoodBoomRecipeCrawler extends FoodBoomCrawler {

    // TODO Comment all

    /**
     * CSS Query for headlines.
     */
    private final String CSS_QUERY_H1 = "h1";

    /**
     * CSS Query for recipe image.
     */
    private final String CSS_QUERY_RECIPE_IMAGE = "img.card-image__img";

    /**
     * CSS Query for getting the value of a src-attribute.
     */
    private final String CSS_QUERY_ATTRIBUTE_SRC = "src";

    private final String CSS_QUERY_TABLE_IN_PAGE = "table.table.table--with-line.node.node--type-recipe.node--view-mode-full";

    private final String CSS_QUERY_TABLE_INGREDIENTS = "table.table.table--no-spacing.table--checkboxes";

    private final String CSS_QUERY_TBODY = "tbody";

    private final String CSS_QUERY_TR = "tr";

    private final String CSS_QUERY_TD = "td";

    private final String CSS_QUERY_DIV = "div";

    private final String CSS_QUERY_SPAN = "span";

    private final String CSS_QUERY_ATTRIBUTE_NG_INIT = "ng-init";

    private final String CSS_QUERY_P_INSTRUCTION = "div.box.box-with-counter.box--large p";

    /**
     * Map containing a string standing for a difficulty as named on hellofresh.de as key and its respective {@link DifficultyLevel}.
     */
    private final HashMap<String, DifficultyLevel> DIFFICULTY_LEVEL_CONVERSION = new HashMap<String, DifficultyLevel>() {{
        put("Leicht", DifficultyLevel.EASY);
        put("Mittel", DifficultyLevel.MEDIUM);
        put("Schwer", DifficultyLevel.DIFFICULT);
    }};

    private String TO_APPEND_ONE_PORTION = "?portions=1";

    private Recipe recipe;

    public Recipe scrapRecipe(String url) {
        recipe = null;
        try {
            setUrl(url + TO_APPEND_ONE_PORTION);
            Document recipePage = getUnlimitedDocument();
            recipe = new Recipe();
            recipe.setIdentifier(getRecipeIdFromUrl(url));
            recipe.setRecipeSite(RecipeSite.FOODBOOM);
            scrapAllRecipeInformation(recipePage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print("Recipe successfully scrapped: " + recipe.getTitle());
        return recipe;
    }

    private void scrapAllRecipeInformation(Document recipePage) {
        scrapTitle(recipePage);
        scrapPictureUrl(recipePage);
        scrapPreparationInstruction(recipePage);
        scrapDifficultyAndDuration(recipePage);
        scrapIngredients(recipePage);
    }

    private void scrapTitle(Document recipePage) {
        Elements elements = recipePage.select(CSS_QUERY_H1);
        if (elements.size() > 0) {
            Element title = elements.get(0);
            recipe.setTitle(title.text());
        }
    }

    private void scrapPictureUrl(Document recipePage) {
        Elements elements = recipePage.select(CSS_QUERY_RECIPE_IMAGE);
        if (elements.size() > 0) {
            Element pictureUrl = elements.get(0);
            recipe.setPictureUrl(pictureUrl.attr(CSS_QUERY_ATTRIBUTE_SRC));
        }
    }

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
                        recipe.setPreparationTimeInMin(Integer.parseInt(durationNrOnly));
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

    // TODO: Make smaller?
    private void scrapIngredients(Document recipePage) {
        List<IngredientInRecipe> ingredientsInRecipe = new ArrayList<IngredientInRecipe>();
        Elements ingredientTables = recipePage.select(CSS_QUERY_TABLE_INGREDIENTS);
        if(ingredientTables.size() > 0) {
            Elements rowsOfIngredientTable = ingredientTables.select(CSS_QUERY_TR);
            if (rowsOfIngredientTable.size() > 0) {
                for (Element row : rowsOfIngredientTable) {
                    IngredientInRecipe ingredientInRecipe = new IngredientInRecipe();
                    ingredientInRecipe.setRecipe(recipe);
                    Elements cellsOfIngredientRow = row.select(CSS_QUERY_TD);
                    if (cellsOfIngredientRow.size() == 2) {
                        // Handling the ingredient name:
                        Elements sgPlIngredientNames = cellsOfIngredientRow.last().select(CSS_QUERY_DIV);
                        if (sgPlIngredientNames.size() > 0) {
                            // TODO: Discuss: Always sg!
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
                                    if (measure != null && !measure.isEmpty()) {
                                        ingredientInRecipe.setQuantity(quantity);
                                        ingredientInRecipe.setMeasure(measure);
                                    } else {
                                        System.err.println("ERROR: '" + ingredientInRecipe.getIngredient().getName() + "' has a quantity of " + quantity + " but no measure. Neither quantity nor measure have been saved.");
                                    }
                                } else {
                                    System.err.println("'" + ingredientInRecipe.getIngredient().getName() + "' will be saved as ingredient without quantity and measure.");
                                }
                            } else {
                                System.err.println("An error occurred while trying to scrap an ingredient for recipe "+recipe.getTitle()+".");
                            }
                        }
                    }
                    ingredientsInRecipe.add(ingredientInRecipe);
                }
                recipe.setIngredientInRecipes(ingredientsInRecipe);
            }
        }
    }

    private double readQuantityOfAttribute(String ngInitAttribute) {
        double quantity = 0;
        String firstOperation = ngInitAttribute.split(";")[0];
        if (firstOperation.contains("=")) {
            String operationValue = firstOperation.split("=")[1];
            // TODO: Discuss assumption: better an error in parsing than throwing one if not matching a regex
            quantity = Double.parseDouble(operationValue);
        }
        return quantity;
    }

    private String readMeasureOfIngredient(Element quantityInformation) {
        String measure = null;
        // TODO: Discus: Always sg!
        Elements informationSpans = quantityInformation.select(CSS_QUERY_SPAN);
        if (informationSpans.size()>1) {
            measure = informationSpans.last().text();
        }
        return measure;
    }

    private void scrapPreparationInstruction(Document recipePage) {
        Elements instructions = recipePage.select(CSS_QUERY_P_INSTRUCTION);
        String instructionsString = "";
        if (instructions.size() > 0) {
            for (Element instruction : instructions) {
                instructionsString += instruction.text();
            }
            recipe.setPreparation(instructionsString);
        }
    }

}
