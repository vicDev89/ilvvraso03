package de.berlin.htw.usws.webcrawlers.foodboom;

import de.berlin.htw.usws.webcrawlers.generic.Crawler;

public abstract class FoodBoomCrawler extends Crawler {

    /**
     * Base URL of the "FoodBoom" page.
     */
    private final String BASE_URL = "https://www.foodboom.de/";

    public FoodBoomCrawler() {
        super();
        setBaseUrl(BASE_URL);
    }

    public String getRecipeIdFromUrl(String url){
        return url.replace(BASE_URL + "rezept/", "");
    }

}
