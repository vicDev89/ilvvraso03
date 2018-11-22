package de.berlin.htw.usws.controllers;

import com.google.gson.GsonBuilder;
import de.berlin.htw.usws.model.Ingredient;
import de.berlin.htw.usws.model.Recipe;
import de.berlin.htw.usws.services.RecipeService;
import org.apache.deltaspike.core.api.provider.BeanProvider;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/")
public class RecipeController {

    @Inject
    private RecipeService recipeService;

    @GET
    @Path("/test")
    @Consumes("application/json")
    public Response test() {
        Recipe recipe = this.recipeService.getRecipeTest();
        return Response.ok(recipe).build();
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

        List<Recipe> recipes = this.recipeService.findRecipesByIngredients(ingredients);

        return Response.ok().entity(recipes).build();
    }
}
