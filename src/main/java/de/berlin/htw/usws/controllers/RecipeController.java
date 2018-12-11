package de.berlin.htw.usws.controllers;

import de.berlin.htw.usws.model.Ingredient;
import de.berlin.htw.usws.model.Recipe;
import de.berlin.htw.usws.repositories.IngredientRepository;
import de.berlin.htw.usws.repositories.IngredientsInRecipeRepository;
import de.berlin.htw.usws.repositories.RecipeRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.PredicateUtils;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/")
public class RecipeController {

    @Inject
    private RecipeRepository recipeRepository;

    @Inject
    private IngredientRepository ingredientRepository;

    @Inject
    private IngredientsInRecipeRepository ingredientsInRecipeRepository;

    /**
     * POST-Aufruf, der mit den übergebenen Ingredients alle Rezepte durchsucht.
     *
     * @param ingredientsList
     * @return
     */
    @POST
    @Path("/getRecipes")
    @Consumes("application/json")
    @Produces("application/json")
    public Response getRecipes(final IngredientsList ingredientsList) {
        List<Recipe> recipes = this.recipeRepository.findRecipesContainingIngredients(ingredientsList.getIngredients());
        return Response.ok().entity(recipes).build();
    }

    /**
     * GET-Auruf, um alle Ingredients von der DB zu holen
     * Dazu werden auch alle mögliche Measures von jedem Ingredient ermittelt
     *
     * @return
     */
    @GET
    @Path("/getAllIngredients")
    @Produces("application/json")
    public Response getAllIngredients() {
        List<Ingredient> ingredients = this.ingredientRepository.findAllIngredients();
        List<IngredientFrontend> ingredientsFrontend = new ArrayList<>();
        for(Ingredient ingredient : ingredients) {
            List<String> measures = this.ingredientsInRecipeRepository.getMeasuresByIngredient(ingredient.getName());
            CollectionUtils.filter(measures, PredicateUtils.notNullPredicate());
            ingredientsFrontend.add(new IngredientFrontend(ingredient.getName(), measures));
        }
        return Response.ok().entity(ingredientsFrontend).build();
    }

}
