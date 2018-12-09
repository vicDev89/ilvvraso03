package de.berlin.htw.usws.webcrawlers;

import de.berlin.htw.usws.model.IngredientInRecipe;
import de.berlin.htw.usws.model.Recipe;
import de.berlin.htw.usws.webcrawlers.foodboom.FoodBoomRecipeCrawler;
import de.berlin.htw.usws.webcrawlers.foodboom.FoodBoomUnknownUrlsCrawler;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;

public class FoodBoomTest {

    @Ignore
    @Test
    public void testScrapRecipe() {
        FoodBoomUnknownUrlsCrawler foodBoomUnknownUrlsCrawler= new FoodBoomUnknownUrlsCrawler();
        foodBoomUnknownUrlsCrawler.getUrlsForNewRecipes();
        /*

        FoodBoomRecipeCrawler foodBoomRecipeCrawler = new FoodBoomRecipeCrawler();

        ArrayList<Recipe> al = new ArrayList();
        al.add(foodBoomRecipeCrawler.scrapRecipe("/rezept/schneller-tomatensalat"));
        al.add(foodBoomRecipeCrawler.scrapRecipe("/rezept/kaesespaetzle"));

        for(Recipe r : al) {
            System.out.println("Titel:\t\t\t" + r.getTitle());
            System.out.println("Bild:\t\t\t" + r.getPictureUrl());
            System.out.println("Identifier:\t\t" + r.getIdentifier());
            System.out.println("Difficulty:\t\t" + r.getDifficultyLevel());
            System.out.println("Bewertung:\t\t" + r.getRate());
            System.out.println("Zeit:\t\t\t" + r.getPreparationTimeInMin());
            System.out.println("Seite:\t\t\t" + r.getRecipeSite());
            System.out.println("Vorbereitung:\t" + r.getPreparation());
            System.out.println("Zutaten:\t\t");
            for (IngredientInRecipe i : r.getIngredientInRecipes()) {
                System.out.println("\t\t\t\t- " + i.getIngredient().getName());
                System.out.println("\t\t\t\t  " + i.getQuantity() + " " + i.getMeasure());
            }
        }
        */
    }

}
