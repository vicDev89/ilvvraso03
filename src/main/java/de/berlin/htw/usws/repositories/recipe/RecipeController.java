package de.berlin.htw.usws.repositories.recipe;

import com.google.gson.GsonBuilder;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

@Path("/")
public class RecipeController {

    @Inject
    private RecipeRepository recipeRepository;

    @POST
    @Path("/getRecipes")
    @Consumes("application/json")
    @Produces("application/json")
    public Response getRecipes(final String ingredients) {

        // TODO Convert JSON with ingredients to ArrayList
        final GsonBuilder builder = new GsonBuilder();
        final ArrayList<String> listeIngredients = builder.create().fromJson(ingredients, ArrayList.class);

        this.recipeRepository.findBy(1L);

        return Response.ok().build();
    }
}
