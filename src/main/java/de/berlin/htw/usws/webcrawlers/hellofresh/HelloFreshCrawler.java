package de.berlin.htw.usws.webcrawlers.hellofresh;

import de.berlin.htw.usws.webcrawlers.generic.Crawler;

/**
 * Abstract class for all Hello Fresh-Crawlers.
 *
 * @author Lucas Larisch
 * @since 12.11.2018
 */
abstract class HelloFreshCrawler extends Crawler {

    /**
     * Base URL of the "Chefkoch" page
     */
    private final String BASE_URL = "https://www.hellofresh.de/";

    /**
     * Calls the super-constructor and sets a base URL.
     */
    public HelloFreshCrawler() {
        super();
        setBaseUrl(BASE_URL);
    }

    /**
     * Takes an ID out of an URL to a recipe and returns it.
     *
     * @param url The URL to a recipe the ID will be taken out of.
     * @return ID of a recipe.
     * @author Lucas Larisch
     * @since 13.11.2018
     */
    protected String getRecipeIdFromUrl(String url) {
        String[] splittedUrl = url.split("\\?")[0].split("-");
        String id = splittedUrl[splittedUrl.length - 1];
        return id;
    }

}
