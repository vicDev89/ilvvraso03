package de.berlin.htw.usws.webcrawlers;


import de.berlin.htw.usws.model.Ingredient;
import de.berlin.htw.usws.model.IngredientInRecipe;
import de.berlin.htw.usws.model.Product;
import de.berlin.htw.usws.model.Recipe;
import de.berlin.htw.usws.webcrawlers.bringmeister.BringmeisterProductAPI;
import de.berlin.htw.usws.webcrawlers.rewe.ReweCrawler;
import de.berlin.htw.usws.webcrawlersOld.RecipeCrawlerChefkoch;
import de.berlin.htw.usws.webcrawlersOld.UnknownIdsCrawlerChefkoch;
import org.junit.Ignore;
import org.junit.Test;


import javax.persistence.EntityManager;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;


public class ChefkochCrawlersTest extends PersistTestBase {

    @Test
    @Ignore
    public void crawlOnerecipeAndPersist()  {
        EntityManager entityManager = getEntityManager();

        RecipeCrawlerChefkoch recipeCrawler = new RecipeCrawlerChefkoch();
        Recipe recipe = recipeCrawler.scrapRecipe(3292121488810516L);

        List<Ingredient> listeIngredients = new ArrayList<>();


        for (IngredientInRecipe ingredientInRecipe : recipe.getIngredientInRecipes()) {
            listeIngredients.add(ingredientInRecipe.getIngredient());
        }

        for (Ingredient ingredient : listeIngredients) {

            List<Object> results =  entityManager.createQuery("SELECT i FROM Ingredient i where i.name=:name").setParameter("name", ingredient.getName()).getResultList();

            if(results.isEmpty()) {
                entityManager.getTransaction().begin();

                entityManager.persist(ingredient);

                entityManager.getTransaction().commit();

                entityManager.clear();
            }
        }

        for(IngredientInRecipe ingredientInRecipe : recipe.getIngredientInRecipes()) {

            List<Object> results =  entityManager.createQuery("SELECT i FROM Ingredient i where i.name=:name").setParameter("name", ingredientInRecipe.getIngredient().getName()).getResultList();

            ingredientInRecipe.setIngredient((Ingredient) results.get(0));
        }

        entityManager.getTransaction().begin();

        entityManager.persist(recipe);

        entityManager.getTransaction().commit();

        entityManager.clear();

        ReweCrawler reweCrawler = new ReweCrawler();
        BringmeisterProductAPI edekaCrawler = new BringmeisterProductAPI();



        List<Object> results =  entityManager.createQuery("SELECT i FROM Ingredient i").getResultList();
        for(Object o : results) {
            Ingredient i = (Ingredient)o;
            try {
                Product productRewe = reweCrawler.getProductForIngredientREWE(i.getName());
                Product productEdeka = edekaCrawler.getProduct(i.getName());
                entityManager.getTransaction().begin();
                if(productRewe != null) {
                    productRewe.setIngredient(i);
                    entityManager.persist(productRewe);
                }
                if(productEdeka != null) {
                    productEdeka.setIngredient(i);
                    entityManager.persist(productEdeka);
                }
                entityManager.getTransaction().commit();
                entityManager.clear();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

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
    //  ArrayList<Long> idList = idCrawler.crawlRecipePages();

        for (long id : idList) {
            Recipe recipe = recipeCrawler.scrapRecipe(id);

            System.out.println(recipe.getTitle());
            System.out.println(recipe.getId());
            System.out.println("Picture: " + recipe.getPictureUrl());
            System.out.println("Rate: " + recipe.getRate());
            System.out.println("Difficulty: " + recipe.getDifficultyLevel());
            System.out.println("Prep. time: " + recipe.getPreparationTimeInMin());
            System.out.println("Rest. time: " + recipe.getRestingTimeInMin());
            System.out.println("Cook. time: " + recipe.getCookingTimeInMin());
            System.out.println("Preparation (char length): " + recipe.getPreparation().length());
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
