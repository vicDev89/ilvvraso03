package de.berlin.htw.usws.controllers;

import com.google.gson.GsonBuilder;
import de.berlin.htw.usws.model.Ingredient;
import de.berlin.htw.usws.model.IngredientInRecipe;
import de.berlin.htw.usws.model.Product;
import de.berlin.htw.usws.model.Recipe;
import de.berlin.htw.usws.repositories.IngredientRepository;
import de.berlin.htw.usws.repositories.ProductRepository;
import de.berlin.htw.usws.repositories.RecipeRepository;
import de.berlin.htw.usws.services.RecipeService;
import de.berlin.htw.usws.webcrawlers.bringmeister.BringmeisterProductAPI;
import de.berlin.htw.usws.webcrawlers.hellofresh.HelloFreshRecipeCrawler;
import de.berlin.htw.usws.webcrawlers.rewe.ReweCrawler;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.apache.deltaspike.jpa.api.transaction.Transactional;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Path("/")
public class RecipeController {

    @Inject
    private RecipeService recipeService;

    @Inject
    private IngredientRepository ingredientRepository;

    @Inject
    private RecipeRepository recipeRepository;

    @Inject
    private BringmeisterProductAPI bringmeisterProductAPI;

    @Inject
    private ReweCrawler reweCrawler;

    @Inject
    private ProductRepository productRepository;

    @GET
    @Path("/test")
    @Consumes("application/json")
    public Response test() {
    //    Recipe recipe = this.recipeService.getRecipeTest();

        String url = "https://www.hellofresh.de/recipes/himmel-und-erde-5be99f04ae08b522d8532fa2?locale=de-DE";

        HelloFreshRecipeCrawler helloFreshRecipeCrawler = new HelloFreshRecipeCrawler();


        Recipe recipe = helloFreshRecipeCrawler.scrapRecipe(url);

        for(IngredientInRecipe ingredientInRecipe : recipe.getIngredientInRecipes()) {
            this.ingredientRepository.save(ingredientInRecipe.getIngredient());
        }

        for(IngredientInRecipe ingredientInRecipe : recipe.getIngredientInRecipes()) {
            ingredientInRecipe.setIngredient(this.ingredientRepository.findByName(ingredientInRecipe.getIngredient().getName()));
        }

        this.recipeRepository.save(recipe);


        List<Ingredient> ingredients = this.ingredientRepository.findAll();

        for(Ingredient ingredient:ingredients) {
            Product productBringmeister = this.bringmeisterProductAPI.getProduct(ingredient.getName());
            if(productBringmeister !=null) {
                productBringmeister.setIngredient(ingredient);
                this.productRepository.save(productBringmeister);
            }
            Product productRewe = null;
            try {
                productRewe = this.reweCrawler.getProductForIngredientREWE(ingredient.getName());
                if(productRewe!=null) {
                    productRewe.setIngredient(ingredient);
                    this.productRepository.save(productRewe);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



        return Response.ok(null).build();
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
