package de.berlin.htw.usws.webcrawlers.foodboom;

import de.berlin.htw.usws.webcrawlers.generic.Crawler;

/**
 * Class extending each FoodBoom-Crawler.
 *
 * @author Lucas Larisch
 * @since 04.12.2018
 */
public abstract class FoodBoomCrawler extends Crawler {

    /**
     * Base URL of the "FoodBoom" page.
     */
    private final String BASE_URL = "https://www.foodboom.de/";

    public FoodBoomCrawler() {
        super();
        setBaseUrl(BASE_URL);
    }

    /**
     * Returns an identifier (FoodBoom) read from a relative URL.
     *
     * @param url Relate URL to the recipe.
     * @return Identifier read from the relative URL.
     * @author Lucas Larisch
     * @since 04.12.2018
     */
    protected String getRecipeIdFromRelativeUrl(String url){
        final String TO_REPLACE_FOR_ID = "/rezept/";
        return url.replace(TO_REPLACE_FOR_ID, "");
    }

}
