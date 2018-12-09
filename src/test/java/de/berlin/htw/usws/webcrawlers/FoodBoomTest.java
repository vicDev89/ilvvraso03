package de.berlin.htw.usws.webcrawlers;

import de.berlin.htw.usws.model.IngredientInRecipe;
import de.berlin.htw.usws.model.Recipe;
import de.berlin.htw.usws.webcrawlers.foodboom.FoodBoomRecipeCrawler;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;

public class FoodBoomTest {

    @Ignore
    @Test
    public void testScrapRecipe() {
        FoodBoomRecipeCrawler foodBoomRecipeCrawler = new FoodBoomRecipeCrawler();

        ArrayList<Recipe> al = new ArrayList();

        al.add(foodBoomRecipeCrawler.scrapRecipe("https://www.foodboom.de/rezept/schneller-tomatensalat"));
        al.add(foodBoomRecipeCrawler.scrapRecipe("https://www.foodboom.de/rezept/kaesespaetzle"));

        for(Recipe r : al) {
            System.out.println(r.getTitle());
            System.out.println(r.getPictureUrl());
            System.out.println(r.getIdentifier());
            System.out.println(r.getDifficultyLevel());
            System.out.println(r.getRate());
            System.out.println(r.getPreparationTimeInMin());
            System.out.println(r.getRecipeSite());
            System.out.println(r.getPreparation());
            for (IngredientInRecipe i : r.getIngredientInRecipes()) {
                System.out.println(" - " + i.getIngredient().getName());
                System.out.println("   " + i.getQuantity() + " " + i.getMeasure());
                System.out.println("   " + i.getRecipe().getTitle());
            }
        }
    }

}
