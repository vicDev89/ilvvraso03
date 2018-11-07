package de.berlin.htw.usws.webcrawlers;

import de.berlin.htw.usws.model.IngredientInRecipe;
import de.berlin.htw.usws.model.Recipe;
import org.junit.Ignore;
import org.junit.Test;
import testutils.FakerProducer;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChefkochCrawlersTest extends PersistTestBase{

    @Test
    @Ignore
    public void crawlOnerecipeAndPersist() {
        RecipeCrawlerChefkoch recipeCrawler = new RecipeCrawlerChefkoch();
        Recipe recipe = recipeCrawler.scrapRecipe(3292121488810516L);

        EntityManager entityManager = getEntityManager();
        entityManager.getTransaction().begin();

        entityManager.persist(recipe);

        entityManager.getTransaction().commit();

        entityManager.clear();

    }


    @Test
    @Ignore
    public void crawlRecipes() throws IOException {
        UnknownIdsCrawlerChefkoch idCrawler = new UnknownIdsCrawlerChefkoch();
        RecipeCrawlerChefkoch recipeCrawler = new RecipeCrawlerChefkoch();

        ArrayList<Long> idList = new ArrayList<Long>();
        idList.add(3292121488810516L);
        idList.add(3586591539033463L);
        idList.add(601821159946812L);
        idList.add(1413701246265716L);
//      ArrayList<Long> idList = idCrawler.crawlRecipePages();

         for (long id : idList) {
             Recipe recipe = recipeCrawler.scrapRecipe(id);

             System.out.println(recipe.getTitle());
             System.out.println(recipe.getId());
             System.out.println("Picture: "+recipe.getPictureUrl());
             System.out.println("Rate: "+recipe.getRate());
             System.out.println("Difficulty: "+recipe.getDifficultyLevel());
             System.out.println("Prep. time: "+recipe.getPreparationTimeInMin());
             System.out.println("Rest. time: "+recipe.getRestingTimeInMin());
             System.out.println("Cook. time: "+recipe.getCookingTimeInMin());
             System.out.println("Preparation (char length): "+recipe.getPreparation().length());
             System.out.println("Ingredients: ");

             List<IngredientInRecipe> ingredients = recipe.getIngredientInRecipes();

             for (IngredientInRecipe i : ingredients) {
                 System.out.println(" - " + i.getId());
                 System.out.println("   " + i.getIngredient().getName());
                 System.out.println("   Measure: " + i.getMeasure());
                 System.out.println("   Quantity: " + i.getQuantity());
             }

             System.out.println();

         }

    }
}
