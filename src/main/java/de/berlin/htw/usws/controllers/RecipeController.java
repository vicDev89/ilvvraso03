package de.berlin.htw.usws.controllers;

import com.google.gson.Gson;
import de.berlin.htw.usws.model.Ingredient;
import de.berlin.htw.usws.model.IngredientInRecipe;
import de.berlin.htw.usws.model.Product;
import de.berlin.htw.usws.model.Recipe;
import de.berlin.htw.usws.repositories.IngredientRepository;
import de.berlin.htw.usws.repositories.ProductRepository;
import de.berlin.htw.usws.repositories.RecipeRepository;
import de.berlin.htw.usws.webcrawlers.bringmeister.BringmeisterProductAPI;
import de.berlin.htw.usws.webcrawlers.chefkoch.ChefkochRecipeCrawler;
import de.berlin.htw.usws.webcrawlers.rewe.ReweCrawler;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Path("/")
public class RecipeController {

    @Inject
    private RecipeRepository recipeRepository;

    /**
     * POST-Aufruf, der mit den übergebenen Ingredients alle Rezepte durchsucht. Die Ergebnisliste wird nach der Anzahl
     * von fehlenden Zutanten aufsteigend sortiert
     *
     * @param ingredientsList
     * @return
     */
    @POST
    @Path("/getRecipes")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
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

}
