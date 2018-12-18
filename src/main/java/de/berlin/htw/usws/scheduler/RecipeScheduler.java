package de.berlin.htw.usws.scheduler;


import com.google.common.base.Stopwatch;
import de.berlin.htw.usws.model.Ingredient;
import de.berlin.htw.usws.model.IngredientInRecipe;
import de.berlin.htw.usws.model.Product;
import de.berlin.htw.usws.model.Recipe;
import de.berlin.htw.usws.repositories.IngredientRepository;
import de.berlin.htw.usws.repositories.ProductRepository;
import de.berlin.htw.usws.repositories.RecipeRepository;
import de.berlin.htw.usws.services.ChefkochCrawlerService;
import de.berlin.htw.usws.services.FoodboomCrawlerService;
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
@Scheduled(cronExpression = "0 40 14 ? * * *")
@Slf4j
public class RecipeScheduler implements org.quartz.Job {

    @Inject
    private HellofreshCrawlerService hellofreshCrawlerService;

    @Inject
    private FoodboomCrawlerService foodboomCrawlerService;

    @Inject
    private BringmeisterProductAPI bringmeisterProductAPI;

    @Inject
    private ReweCrawler reweCrawler;

    @Inject
    private IngredientRepository ingredientRepository;

    @Inject
    private RecipeRepository recipeRepository;

    @Inject
    private ProductRepository productRepository;

    private List<Recipe> recipes = new ArrayList<>();

    private List<Ingredient> newIngredients = new ArrayList<>();

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        Stopwatch swGesamt = Stopwatch.createStarted();
        log.info("#### RecipeScheduler started at: " + LocalDateTime.now() + " ####");

        Stopwatch swFoodboomhRecipeScrapper = Stopwatch.createStarted();
        recipes = this.foodboomCrawlerService.start();
        log.info("#### All Foodboom recipes scrapped. Duration: ####" + swFoodboomhRecipeScrapper.elapsed(TimeUnit.SECONDS) + " seconds.");

        Stopwatch swHellofreshRecipeScrapper = Stopwatch.createStarted();
        recipes.addAll(this.hellofreshCrawlerService.start());
        log.info("#### All Hellofresh recipes scrapped. Duration: ####" + swHellofreshRecipeScrapper.elapsed(TimeUnit.SECONDS) + " seconds.");

        Stopwatch swRecipePersister = Stopwatch.createStarted();
        // Persist first all ingredients
        persistIngredients();
        // Set DB ingredients to recipes
        saveIngredientsOnRecipes();
        // Persist all recipes
        persistAllRecipes();
        log.info("#### All recipes persisted. Duration: ####" + swRecipePersister.elapsed(TimeUnit.SECONDS) + " seconds.");

        Stopwatch swProductScrapperAndPersister = Stopwatch.createStarted();
        // Look for products for the new ingredients
        crawlProducts();
        log.info("#### All products scrapped and persisted. Duration: ####" + swProductScrapperAndPersister.elapsed(TimeUnit.SECONDS) + " seconds.");

        log.info("#### Crawler-Services ended. Duration: ####" + swGesamt.elapsed(TimeUnit.SECONDS) + " seconds.");

    }

    private void persistProducts(List<Product> products, Ingredient ingredient) {
        if (products != null) {
            for (Product product : products) {
                if (product != null && this.productRepository.findByNameAndSupermarket(product.getName(), product.getSupermarket()) == null) {
                    product.setIngredient(ingredient);
                    this.productRepository.save(product);
                }
            }
        }
    }

    private void crawlProducts() {
        for (Ingredient ingredient : newIngredients) {
            List<Product> productBringmeister = this.bringmeisterProductAPI.getProducts(ingredient.getName());
            persistProducts(productBringmeister, ingredient);
            try {
                List<Product> productRewe = this.reweCrawler.getProductForIngredientREWE(ingredient.getName());
                persistProducts(productRewe, ingredient);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    private void persistAllRecipes() {
        for (Recipe recipe : recipes) {
            if (this.recipeRepository.findByTitle(recipe.getTitle()) == null) {
                this.recipeRepository.save(recipe);
            } else {
                log.info("Recipe " + recipe.getRecipeSite() + ": " + recipe.getTitle() + " already exists");
            }

        }
    }

    private void saveIngredientsOnRecipes() {
        for (Recipe recipe : recipes) {
            if (recipe.getIngredientInRecipes() != null) {
                for (IngredientInRecipe ingredientInRecipe : recipe.getIngredientInRecipes()) {
                    // Find ingredient by name
                    ingredientInRecipe.setIngredient(this.ingredientRepository.findByName(ingredientInRecipe.getIngredient().getName()));
                }
            }
        }
    }

    private void persistIngredients() {

        for (Recipe recipe : recipes) {
            if (recipe.getIngredientInRecipes() != null) {
                for (IngredientInRecipe ingredientInRecipe : recipe.getIngredientInRecipes()) {
                    Ingredient ingredient = ingredientInRecipe.getIngredient();
                    if(ingredient!=null) {
                        if (this.ingredientRepository.findByName(ingredient.getName()) == null) {
                            this.ingredientRepository.save(ingredient);
                            newIngredients.add(ingredient);
                        }
                    }
                }
            }
        }
    }
}

