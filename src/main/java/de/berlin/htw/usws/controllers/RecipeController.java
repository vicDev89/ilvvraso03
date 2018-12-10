package de.berlin.htw.usws.controllers;

import de.berlin.htw.usws.model.Ingredient;
import de.berlin.htw.usws.model.Recipe;
import de.berlin.htw.usws.repositories.IngredientRepository;
import de.berlin.htw.usws.repositories.RecipeRepository;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Path("/")
public class RecipeController {

    @Inject
    private RecipeRepository recipeRepository;

    @Inject
    private IngredientRepository ingredientRepository;

    /**
     * POST-Aufruf, der mit den übergebenen Ingredients alle Rezepte durchsucht. Die Ergebnisliste wird nach der Anzahl
     * von fehlenden Zutanten aufsteigend sortiert
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

        List<RecipeForFrontend> recipesForFronted = new ArrayList<>();
        for (Recipe recipe : recipes) {
            int ingredientsToBuy = recipe.getIngredientInRecipes().size() - ingredientsList.getIngredients().size();
            recipesForFronted.add(new RecipeForFrontend(recipe, ingredientsToBuy));
        }

        Collections.sort(recipesForFronted, Comparator.comparingInt(RecipeForFrontend::getIngredientsToBuy));

        return Response.ok().entity(recipesForFronted).build();
    }

    /**
     * GET-Auruf, um alle Ingredients von der DB zu holen
     *
     * @return
     */
    @GET
    @Path("/getAllIngredients")
    @Produces("application/json")
    public Response getAllIngredients() {
        List<Ingredient> ingredients = this.ingredientRepository.findAllIngredients();
        for (Ingredient ingredient : ingredients) {
            ingredient.setProducts(null);
        }
        return Response.ok().entity(ingredients).build();
    }

}
