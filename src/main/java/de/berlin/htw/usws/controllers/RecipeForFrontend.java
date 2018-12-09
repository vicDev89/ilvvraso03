package de.berlin.htw.usws.controllers;

import de.berlin.htw.usws.model.Ingredient;
import de.berlin.htw.usws.model.IngredientInRecipe;
import de.berlin.htw.usws.model.Product;
import de.berlin.htw.usws.model.Recipe;
import de.berlin.htw.usws.model.enums.DifficultyLevel;
import de.berlin.htw.usws.model.enums.RecipeSite;
import lombok.Getter;
import lombok.Setter;


import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class RecipeForFrontend {

    private String title;
    private String preparation;
    private int preparationTimeInMin;
    private Double rate;
    private DifficultyLevel difficultyLevel;
    private String pictureUrl;
    private List<IngredientInRecipe> ingredientInRecipes;
    private String identifier;
    private RecipeSite recipeSite;
    private int ingredientsToBuy;

    public RecipeForFrontend(Recipe recipe, int ingredientsToBuy) {
        this.title = recipe.getTitle();
        this.preparation = recipe.getPreparation();
        this.preparationTimeInMin = recipe.getPreparationTimeInMin();
        this.rate = recipe.getRate();
        this.difficultyLevel = recipe.getDifficultyLevel();
        this.pictureUrl = recipe.getPictureUrl();
        this.ingredientInRecipes = recipe.getIngredientInRecipes();
        this.identifier = recipe.getIdentifier();
        this.recipeSite = recipe.getRecipeSite();
        this.ingredientsToBuy = ingredientsToBuy;
    }


}
