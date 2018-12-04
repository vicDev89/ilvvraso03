package de.berlin.htw.usws.webcrawlers;

import de.berlin.htw.usws.webcrawlers.foodboom.FoodBoomRecipeCrawler;
import org.junit.Ignore;
import org.junit.Test;

public class FoodBoomTest {

    @Ignore
    @Test
    public void testScrapRecipe() {
        new FoodBoomRecipeCrawler().scrapRecipe("https://www.foodboom.de/rezept/kaesespaetzle");
    }

}
