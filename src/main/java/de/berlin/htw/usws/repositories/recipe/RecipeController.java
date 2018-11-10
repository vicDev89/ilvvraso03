package de.berlin.htw.usws.repositories.recipe;

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

//    @Inject
//    private RecipeRepository recipeRepository;

    @GET
    @Path("/test")
    @Consumes("application/json")
    public Response test() {

        RecipeService recipeService = new RecipeService();
        Recipe recipe = recipeService.getRecipe();
//        RecipeRepository recipeRepository = BeanProvider.getContextualReference(RecipeRepository.class);
//        Recipe recipe = recipeRepository.findBy(1L);
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

        RecipeRepository recipeRepository = BeanProvider.getContextualReference(RecipeRepository.class);

        List<Recipe> recipes = recipeRepository.findRecipesByIngredients(ingredients); //this.recipeRepository.findRecipesByIngredients(ingredients);

        return Response.ok().entity(recipes).build();
    }
}
