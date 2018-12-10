package de.berlin.htw.usws.controllers;

import de.berlin.htw.usws.model.Ingredient;
import de.berlin.htw.usws.model.Recipe;
import de.berlin.htw.usws.repositories.IngredientRepository;
import de.berlin.htw.usws.repositories.RecipeRepository;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
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
        return Response.ok().entity(recipes).build();
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
