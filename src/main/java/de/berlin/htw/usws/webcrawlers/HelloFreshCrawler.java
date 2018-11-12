package de.berlin.htw.usws.webcrawlers;

/**
 * Abstract class for all Hello Fresh-Crawlers.
 *
 * @since 12.11.2018
 * @author Lucas Larisch
 */
abstract class HelloFreshCrawler extends Crawler {

    /** Base URL of the "Chefkoch" page */
    private final String BASE_URL  = "https://www.hellofresh.de/";

    /**
     * Calls the super-constructor and sets a base URL.
     */
    public HelloFreshCrawler() {
        super();
        setBaseUrl(BASE_URL);
    }

}
