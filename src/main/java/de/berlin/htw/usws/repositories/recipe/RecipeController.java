package de.berlin.htw.usws.repositories.recipe;

import com.google.gson.GsonBuilder;
import de.berlin.htw.usws.model.Ingredient;
import de.berlin.htw.usws.model.Recipe;

import javax.inject.Inject;


import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/")
public class RecipeController {

//    @Inject
//    private RecipeRepository recipeRepository;

    @POST
    @Path("/getRecipes")
    @Consumes("application/json")
    @Produces("application/json")
    public Response getRecipes(final String ingredientsJson) {

        // TODO Convert JSON with ingredients to ArrayList
        final GsonBuilder builder = new GsonBuilder();
        final ArrayList<String> listeIngredients = builder.create().fromJson(ingredientsJson, ArrayList.class);

        ArrayList<Ingredient> ingredients = new ArrayList<>();
        for(String ingredientName : listeIngredients) {
            ingredients.add(new Ingredient(ingredientName));
        }

//        List<Recipe> recipes = this.recipeRepository.findRecipesContainingIngredients(ingredients);

        return Response.ok().entity(null).build();
    }
}
