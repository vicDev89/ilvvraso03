package de.berlin.htw.usws.webcrawlers.chefkoch;

import de.berlin.htw.usws.webcrawlers.generic.Crawler;

/**
 * Abstract class for all Chefkoch-Crawlers.
 *
 * @since 26.10.2018
 * @author Lucas Larisch
 */
abstract class ChefkochCrawler extends Crawler {

    /** Base URL of the "Chefkoch" page */
    private final String BASE_URL  = "https://www.chefkoch.de";

    /**
     * Calls the super-constructor and sets a base URL.
     */
    public ChefkochCrawler() {
        super();
        setBaseUrl(BASE_URL);
    }

}