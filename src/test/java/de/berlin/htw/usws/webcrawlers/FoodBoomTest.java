package de.berlin.htw.usws.webcrawlers;

import de.berlin.htw.usws.model.Recipe;
import de.berlin.htw.usws.webcrawlers.foodboom.FoodBoomRecipeCrawler;
import de.berlin.htw.usws.webcrawlers.foodboom.FoodBoomUnknownUrlsCrawler;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class FoodBoomTest {

    @Ignore
    @Test
    public void testScrapRecipe() {

        FoodBoomUnknownUrlsCrawler foodBoomUnknownUrlsCrawler = new FoodBoomUnknownUrlsCrawler();
        List<String> urls = foodBoomUnknownUrlsCrawler.getUrlsForNewRecipes();
        // List<String> urls = new ArrayList<>();
        // urls.add("/rezept/erdbeer-avocado-salat-mit-haehnchenbruststreifen");

        FoodBoomRecipeCrawler foodBoomRecipeCrawler = new FoodBoomRecipeCrawler();
        ArrayList<Recipe> recipes = new ArrayList<Recipe>();
        for (String url : urls) {
            recipes.add(foodBoomRecipeCrawler.scrapRecipe(url));
        }

        /*
        for (Recipe r : al) {
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
