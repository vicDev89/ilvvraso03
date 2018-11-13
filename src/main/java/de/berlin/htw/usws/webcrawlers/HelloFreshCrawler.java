package de.berlin.htw.usws.webcrawlers;

/**
 * Abstract class for all Hello Fresh-Crawlers.
 *
 * @since 12.11.2018
 * @author Lucas Larisch
 */
abstract class HelloFreshCrawler extends Crawler {

    // TODO: Remove Driver from HelloFreshCrawler (put to Unknown Id Crawler instead) if not used in recipe crawler

    /** Base URL of the "Chefkoch" page */
    private final String BASE_URL  = "https://www.hellofresh.de/";

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
     * @since 13.11.2018
     * @author Lucas Larisch
     * @param url The URL to a recipe the ID will be taken out of.
     * @return ID of a recipe.
     */
    protected String getRecipeIdFromUrl(String url) {
        String[] splittedUrl = url.split("\\?")[0].split("-");
        String id = splittedUrl[splittedUrl.length-1];
        return id;
    }

}
