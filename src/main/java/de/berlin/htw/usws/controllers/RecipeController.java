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
     * POST-Aufruf, der mit den übergebenen Ingredients alle Rezepte holt.
     *
     * @param ingredientsList
     * @return
     */
    @POST
    @Path("/getRecipes")
    @Consumes("application/json")
    @Produces("application/json")
    public Response getRecipes(final IngredientsList ingredientsList) {
        List<Recipe> recipes = this.recipeRepository.findRecipesContainingIngredientsAll(ingredientsList.getIngredients());
        return Response.ok().entity(recipes).build();
    }

    /**
     * POST-Aufruf, der mit den übergebenen Ingredients alle Rezepte ab 10 holt.
     *
     * @param ingredientsList
     * @return
     */
    @POST
    @Path("/getRecipesRest")
    @Consumes("application/json")
    @Produces("application/json")
    public Response getRecipesRest(final IngredientsList ingredientsList) {
        List<Recipe> recipes = this.recipeRepository.findRecipesContainingIngredientsRest(ingredientsList.getIngredients());
        return Response.ok().entity(recipes).build();
    }

    /**
     * POST-Aufruf, der mit den übergebenen Ingredients die ersten 10 Rezepte holt.
     *
     * @param ingredientsList
     * @return
     */
    @POST
    @Path("/getRecipesMax")
    @Consumes("application/json")
    @Produces("application/json")
    public Response getRecipesMax(final IngredientsList ingredientsList) {
        List<Recipe> recipes = this.recipeRepository.findRecipesContainingIngredientsMax(ingredientsList.getIngredients());
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


    /**
     * GET-Aufruf, um die Measures von einem Ingredient zu holen
     *
     * @param ingredientName
     * @return
     */
    @GET
    @Path("/getMeasures/{ingredientName}")
    @Produces("application/json")
    public Response getMeasures(@PathParam("ingredientName") final String ingredientName) {
        List<String> measures = this.ingredientsInRecipeRepository.getMeasuresByIngredient(ingredientName);
        CollectionUtils.filter(measures, PredicateUtils.notNullPredicate());
        return Response.ok().entity(measures).build();
    }

}
