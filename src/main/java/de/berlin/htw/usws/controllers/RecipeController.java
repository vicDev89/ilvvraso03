package de.berlin.htw.usws.controllers;

import com.google.gson.GsonBuilder;
import de.berlin.htw.usws.model.Ingredient;
import de.berlin.htw.usws.model.IngredientInRecipe;
import de.berlin.htw.usws.model.Product;
import de.berlin.htw.usws.model.Recipe;
import de.berlin.htw.usws.webcrawlers.chefkoch.ChefkochRecipeCrawler;
import de.berlin.htw.usws.repositories.IngredientRepository;
import de.berlin.htw.usws.repositories.ProductRepository;
import de.berlin.htw.usws.repositories.RecipeRepository;
import de.berlin.htw.usws.services.RecipeService;
import de.berlin.htw.usws.webcrawlers.bringmeister.BringmeisterProductAPI;
import de.berlin.htw.usws.webcrawlers.rewe.ReweCrawler;

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

        List<Ingredient> newIngredients = new ArrayList<>();

//        String url = "https://www.hellofresh.de/recipes/gefullte-hahnchenbrust-mit-mozzarella-5bf2ca8b30006c743304cc42?locale=de-DE";
//
//        HelloFreshRecipeCrawler helloFreshRecipeCrawler = new HelloFreshRecipeCrawler();
//
//        Recipe recipe = helloFreshRecipeCrawler.scrapRecipe(url);

        ChefkochRecipeCrawler recipeCrawler = new ChefkochRecipeCrawler();
        Recipe recipe = recipeCrawler.scrapRecipe(3292121488810516L);

        for (IngredientInRecipe ingredientInRecipe : recipe.getIngredientInRecipes()) {
            Ingredient ingredient = ingredientInRecipe.getIngredient();
            if (this.ingredientRepository.findByName(ingredient.getName()) == null) {
                this.ingredientRepository.save(ingredient);
                newIngredients.add(ingredient);
            }
        }

        for (IngredientInRecipe ingredientInRecipe : recipe.getIngredientInRecipes()) {
            ingredientInRecipe.setIngredient(this.ingredientRepository.findByName(ingredientInRecipe.getIngredient().getName()));
        }

        if (this.recipeRepository.findByTitle(recipe.getTitle()) == null)
            this.recipeRepository.save(recipe);


        for (Ingredient ingredient : newIngredients) {
            List<Product> productBringmeister = this.bringmeisterProductAPI.getProducts(ingredient.getName());
            for(Product product : productBringmeister) {
                if (product != null && this.productRepository.findByNameAndSupermarket(product.getName(), product.getSupermarket()) == null) {
                    product.setIngredient(ingredient);
                    this.productRepository.save(product);
                }
            }

            try {

                List<Product> productRewe = this.reweCrawler.getProductForIngredientREWE(ingredient.getName());
                for(Product product : productRewe) {
                    if (product != null && this.productRepository.findByNameAndSupermarket(product.getName(), product.getSupermarket()) == null) {
                        product.setIngredient(ingredient);
                        this.productRepository.save(product);
                    }
                }
            } catch (IOException e) {
                e.getStackTrace();
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
