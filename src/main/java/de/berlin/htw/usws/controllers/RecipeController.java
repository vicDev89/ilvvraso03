package de.berlin.htw.usws.controllers;

import com.google.gson.GsonBuilder;
import de.berlin.htw.usws.model.Ingredient;
import de.berlin.htw.usws.model.Recipe;
import de.berlin.htw.usws.repositories.RecipeRepository;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/")
public class RecipeController {

    @Inject
    private RecipeRepository recipeRepository;


    @GET
    @Path("/getTest")
    @Consumes("application/json")
    @Produces("application/json")
    public Response getTest() {
        List<Ingredient> ingredients = new ArrayList<>();
        Ingredient ingredient = new Ingredient();
        ingredient.setName("banane");
        ingredients.add(ingredient);
        List<Recipe> recipes = this.recipeRepository.findRecipesContainingIngredients(ingredients);
        return Response.ok().entity(recipes).build();
    }

    @POST
    @Path("/getRecipes")
    @Consumes("application/json")
    @Produces("application/json")
    public Response getRecipes(final String ingredientsJson) {

        final GsonBuilder builder = new GsonBuilder();
        final ArrayList<String> listeIngredients = builder.create().fromJson(ingredientsJson, ArrayList.class);

        ArrayList<Ingredient> ingredients = new ArrayList<>();
        for (String ingredientName : listeIngredients) {
            ingredients.add(new Ingredient(ingredientName));
        }

        List<Recipe> recipes = this.recipeRepository.findRecipesContainingIngredients(ingredients);

        return Response.ok().entity(recipes).build();
    }
}
