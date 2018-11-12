package de.berlin.htw.usws.webcrawlers;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HelloFreshUnknownIdsCrawler extends HelloFreshCrawler {

    /** CSS query for the total number of recipes. */
    private final String CSS_QUERY_TOTAL_NUMBER = "div.fela-1abvmfr div span:not([data-translation-id])";

    /** CSS query for an anchor containing the link to a recipe. */
    private final String CSS_QUERY_RECIPE_A = "a.fela-ksgbu1";

    /** CSS query for an href-attribute. */
    private final String CSS_QUERY_ATTRIBUTE_HREF = "href";

    /** CSS query for a div to be clicked on in order to close a popup. */
    private final String CSS_QUERY_CLOSE_DIV = "div.dy-lb-close";

    /** CSS query for the button to be clicked on in order to show more recipes. */
    private final String CSS_QUERY_BUTTON_SHOW_MORE = "button[data-kind=\"green\"]";

    /** Name of the web driver to be used for scrapping the URLs. */
    private final String WEB_DRIVER_NAME = "webdriver.chrome.driver";

    /** Path to the web driver to be used for scrapping the URLs. */
    private final String WEB_DRIVER_PATH = "target/classes/ChromeDriver/chromedriver.exe";

    /** Relative URL holding the newest recipes. */
    private final String RECIPES_URL = "recipes/search/?order=-date";

    /** Pause after clicking on "MEHR ANZEIGEN". */
    private final int SLEEP_INTERVAL = 6;

    /** List all unknown IDs are to be added to. */
    private ArrayList<String> allRecipeUrls = new ArrayList<>();

    /**
     * Opens a (Chrome) Web Driver in order to scrap all URLs to recipes of
     * Hello Fresh and adds them to {@link HelloFreshUnknownIdsCrawler#allRecipeUrls}
     * (List that will be returned). This method will run as long as there are (new)
     * recipes and as long as an URL to be added does not belong to a recipe already
     * stored in the database.
     *
     * @since 12.11.2018
     * @author Lucas Larisch
     * @return List of all URLs representing recipes not yet stored.
     *         ({@link UnknownIdsCrawlerChefkoch#allRecipeIds})
     */
    public ArrayList<String> crawlRecipePage() {
        appendToBaseUrl(RECIPES_URL);
        System.setProperty(WEB_DRIVER_NAME, WEB_DRIVER_PATH);
        WebDriver driver = new ChromeDriver();
        driver.get(getUrl());

        int countRecipes = 0;

        while(countRecipes < totalRecipes(driver)) {
            try {
                countRecipes = addUrlsToList(driver, countRecipes);
                if (countRecipes == -1) {
                    break;
                }
                driver.findElement(By.cssSelector(CSS_QUERY_BUTTON_SHOW_MORE)).click();
            } catch(WebDriverException e) {
                closePopUpIfExisting(driver);
            } finally {
                sleep();
            }
        }
        driver.close();
        return allRecipeUrls;
    }

    /**
     * Scraps the total number of recipes and returns the value as an integer.
     *
     * @since 12.11.2018
     * @author Lucas Larisch
     * @param driver WebDriver to be used for scrapping information.
     * @return Total number of recipes.
     */
    private int totalRecipes(WebDriver driver) {
        WebElement totalRecipesSpan = driver.findElement(By.cssSelector(CSS_QUERY_TOTAL_NUMBER));
        return Integer.parseInt(totalRecipesSpan.getText().trim());
    }

    /**
     * Adds URLs to {@link HelloFreshUnknownIdsCrawler#allRecipeUrls} as long as the
     * recipe they belong to is not stored in the database yet.
     *
     * @since 12.11.2018
     * @author Lucas Larisch
     * @param driver WebDriver to be used for scrapping information.
     * @param nrOfKnownUrls Number of URLs that have already been scrapped before
     *                      calling this method. (used for performance and logging)
     * @return Size of {@link HelloFreshUnknownIdsCrawler#allRecipeUrls} after having
     *         added new URLs.
     */
    private int addUrlsToList(WebDriver driver, int nrOfKnownUrls) {
        List<WebElement> recipeLinks = driver.findElements(By.cssSelector(CSS_QUERY_RECIPE_A));

        for (int i = nrOfKnownUrls; i < recipeLinks.size(); i++) {
            String url = recipeLinks.get(i).getAttribute(CSS_QUERY_ATTRIBUTE_HREF);
            String[] splittedUrl = url.split("\\?")[0].split("-");
            String id = splittedUrl[splittedUrl.length-1];
            // TODO: if !Id in DB:
            if(!false) {
                allRecipeUrls.add(url);
            } else {
                System.out.println(allRecipeUrls.size()-nrOfKnownUrls+" URLs have been added (total: "+allRecipeUrls.size()+").");
                return -1;
            }
        }
        System.out.println(allRecipeUrls.size()-nrOfKnownUrls+" URLs have been added (total: "+allRecipeUrls.size()+").");
        return allRecipeUrls.size();
    }

    /**
     * Pauses the current execution/thread for as many seconds as specified
     * in {@link HelloFreshUnknownIdsCrawler#SLEEP_INTERVAL}.
     *
     * @since 12.11.2018
     * @author Lucas Larisch
     */
    private void sleep(){
        try {
            TimeUnit.SECONDS.sleep(SLEEP_INTERVAL);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks whether a pop up has appeared. If so, it is closed.
     *
     * @since 12.11.2018
     * @author Lucas Larisch
     * @param driver WebDriver to be used for scrapping information.
     */
    private void closePopUpIfExisting(WebDriver driver) {
        if (driver.findElements(By.cssSelector(CSS_QUERY_CLOSE_DIV)).size() != 0) {
            driver.findElement(By.cssSelector(CSS_QUERY_CLOSE_DIV)).click();
        }
    }

}
