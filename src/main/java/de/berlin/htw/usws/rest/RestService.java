package de.berlin.htw.usws.rest;

import com.google.gson.GsonBuilder;
import de.berlin.htw.usws.repositories.RecipeRepository;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

@Path("/gui")
public class RestService {

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



        return Response.ok().build();
    }
}
