package de.berlin.htw.usws.services;

import de.berlin.htw.usws.model.Ingredient;
import de.berlin.htw.usws.model.Recipe;
import de.berlin.htw.usws.repositories.RecipeRepository;
import de.berlin.htw.usws.webcrawlers.hellofresh.HelloFreshUnknownUrlsCrawler;

import javax.ejb.Stateless;
import javax.inject.Inject;

import java.util.List;

@Stateless
public class RecipeService {

    @Inject
    private RecipeRepository recipeRepository;

    public Recipe getRecipeTest() {

        Recipe recipe = this.recipeRepository.findBy(1L);
        if(recipe == null) {
            recipe = new Recipe();
        }
        return recipe;
    }

    public List<Recipe> findRecipesByIngredients(List<Ingredient> ingredients) {
        return this.recipeRepository.findRecipesByIngredients(ingredients);
    }

}
