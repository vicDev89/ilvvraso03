package de.berlin.htw.usws.scheduler;

import com.google.common.base.Stopwatch;
import de.berlin.htw.usws.model.Ingredient;
import de.berlin.htw.usws.model.IngredientInRecipe;
import de.berlin.htw.usws.model.Product;
import de.berlin.htw.usws.model.Recipe;
import de.berlin.htw.usws.repositories.IngredientRepository;
import de.berlin.htw.usws.repositories.ProductRepository;
import de.berlin.htw.usws.repositories.RecipeRepository;
import de.berlin.htw.usws.services.HellofreshCrawlerService;
import de.berlin.htw.usws.webcrawlers.bringmeister.BringmeisterProductAPI;
import de.berlin.htw.usws.webcrawlers.rewe.ReweCrawler;
import lombok.extern.slf4j.Slf4j;
import org.apache.deltaspike.scheduler.api.Scheduled;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import javax.inject.Inject;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

// Every day at midnight - 12am
@Scheduled(cronExpression = "0 0 0 * * ?")
@Slf4j
public class RecipeScheduler implements org.quartz.Job{

    @Inject
    private HellofreshCrawlerService hellofreshCrawlerService;

    @Inject
    private RecipeRepository recipeRepository;

    @Inject
    private IngredientRepository ingredientRepository;

    @Inject
    private BringmeisterProductAPI bringmeisterProductAPI;

    @Inject
    private ReweCrawler reweCrawler;

    @Inject
    private ProductRepository productRepository;


    private List<Recipe> recipes = new ArrayList<>();

    private List<Ingredient> newIngredients = new ArrayList<>();


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Stopwatch sw = Stopwatch.createStarted();
        System.out.println("#### RecipeScheduler started at: " + LocalDateTime.now() + " ####");

        // TODO check how it works with unknown urls
        recipes = this.hellofreshCrawlerService.start();
        // Persist first all ingredients
        persistIngredients();

        // Set DB ingredients to recipes
        saveIngredientsOnRecipes();

        // Persist all recipes
        persistAllRecipes();

        log.info("Hellofresh Recipe Crawler ended. Duration: " + sw.elapsed(TimeUnit.SECONDS) + " seconds.");

        // Look for products for the new ingredients
        crawlProducts();

    }

    private void crawlProducts() {

        for(Ingredient ingredient : newIngredients) {
            Product productBringmeister = this.bringmeisterProductAPI.getProduct(ingredient.getName());
           Product productRewe = null;
            try {
                productRewe = this.reweCrawler.getProductForIngredientREWE(ingredient.getName());
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
            if(productBringmeister!=null) {
                this.productRepository.save(productBringmeister);
            }

            if(productRewe!=null) {
                this.productRepository.save(productRewe);
            }
        }
    }

    private void persistAllRecipes() {
        for(Recipe recipe : recipes) {
            this.recipeRepository.save(recipe);
        }
    }

    private void saveIngredientsOnRecipes () {
        for(Recipe recipe : recipes) {
            for(IngredientInRecipe ingredientInRecipe : recipe.getIngredientInRecipes()) {
                // Find ingredient by name
                Ingredient ingredient = this.ingredientRepository.findByName(ingredientInRecipe.getIngredient().getName());
                ingredientInRecipe.setIngredient(ingredient);
            }
        }
    }

    private void persistIngredients() {

        for(Recipe recipe : recipes) {
            for(IngredientInRecipe ingredientInRecipe : recipe.getIngredientInRecipes()) {
                Ingredient ingredient = ingredientInRecipe.getIngredient();
                if(this.ingredientRepository.findByName(ingredient.getName()) == null) {
                    newIngredients.add(ingredient);
                    this.ingredientRepository.save(ingredient);
                }
            }
        }

    }
}
