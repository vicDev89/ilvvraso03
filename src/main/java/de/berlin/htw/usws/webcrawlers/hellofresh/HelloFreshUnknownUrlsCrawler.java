package de.berlin.htw.usws.webcrawlers.hellofresh;

import de.berlin.htw.usws.repositories.RecipeRepository;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Class used for scrapping URLs of recipes from hellofresh.de that have an ID
 * that is not stored in the database yet.
 *
 * @author Lucas Larisch
 * @since 12.11.2018
 */
@Stateless
public class HelloFreshUnknownUrlsCrawler extends HelloFreshCrawler {

    @Inject
    private RecipeRepository recipeRepository;

    /**
     * Name of the web driver to be used for scrapping the URLs.
     */
    private final String WEB_DRIVER_NAME = "webdriver.chrome.driver";

    /**
     * Path to the web driver to be used for scrapping the URLs.
     */
    private final String WEB_DRIVER_PATH = "C:\\Users\\DelCampo\\Desktop\\wildfly-14.0.1.Final\\bin\\ChromeDriver\\chromedriver.exe";

    /**
     * CSS query for the total number of recipes.
     */
    private final String CSS_QUERY_TOTAL_NUMBER = "div.fela-1abvmfr div span:not([data-translation-id])";

    /**
     * CSS query for an anchor containing the link to a recipe.
     */
    private final String CSS_QUERY_RECIPE_A = "a.fela-ksgbu1";

    /**
     * CSS query for an href-attribute.
     */
    private final String CSS_QUERY_ATTRIBUTE_HREF = "href";

    /**
     * CSS query for the button to be clicked on in order to show more recipes.
     */
    private final String CSS_QUERY_BUTTON_SHOW_MORE = "button[data-kind=\"green\"]";

    /**
     * CSS query for a div to be clicked on in order to close a popup.
     */
    private final String CSS_QUERY_CLOSE_DIV = "div.dy-lb-close";

    /**
     * Relative URL holding the newest recipes.
     */
    private final String RECIPES_URL = "recipes/search/?order=-date";

    /**
     * Pause after clicking on "MEHR ANZEIGEN".
     */
    private final int SLEEP_INTERVAL = 4;

    /**
     * List all unknown IDs are to be added to.
     */
    private ArrayList<String> allRecipeUrls = new ArrayList<>();

    private String lastFirstUrl = "";

    /**
     * Opens a (Chrome) Web Driver in order to scrap all URLs to recipes of
     * Hello Fresh and adds them to {@link HelloFreshUnknownUrlsCrawler#allRecipeUrls}
     * (List that will be returned). This method will run as long as there are (new)
     * recipes and as long as an URL to be added does not belong to a recipe already
     * stored in the database.
     *
     * @return List of all URLs representing recipes not yet stored.
     * @author Lucas Larisch
     * @since 12.11.2018
     */
    public ArrayList<String> crawlRecipePage() {
        appendToBaseUrl(RECIPES_URL);

        WebDriver driver = initHeadlessChromeDriver();
        driver.get(getUrl());
        int countRecipes = 0;
        int totalRecipes = totalRecipes(driver);
        System.out.println("Total recipes: " + totalRecipes);
        while (countRecipes < totalRecipes) {
            if (allRecipeUrls.size() == 108) {
                break;
            }
            try {
                addUrlsToList(driver, countRecipes);

                countRecipes += 6;

                if (countRecipes == -1) {
                    break;
                }
                driver.findElement(By.cssSelector(CSS_QUERY_BUTTON_SHOW_MORE)).click();
            } catch (WebDriverException e) {
                closePopUpIfExisting(driver);
            } finally {
                sleep();
            }
        }
        driver.close();
        return allRecipeUrls;
    }

    /**
     * Returns a headless Chrome Web Driver.
     *
     * @return Headless Chrome Web Driver.
     * @author Lucas Larisch
     * @since 13.11.2018
     */
    protected WebDriver initHeadlessChromeDriver() {
        System.setProperty(WEB_DRIVER_NAME, WEB_DRIVER_PATH);
        ChromeOptions chromeOptions = new ChromeOptions();
        //    chromeOptions.setBinary("C:\\Users\\DelCampo\\Desktop\\wildfly-14.0.1.Final\\bin\\ChromeDriver\\chromedriver.exe");
        chromeOptions.addArguments("--headless");
        chromeOptions.addArguments("--disable-gpu");
        chromeOptions.addArguments("--disable-extensions");
        // Because of error "DevToolsActivePort file doesn't exist"
//        chromeOptions.addArguments("--no-sandbox");
//        chromeOptions.addArguments("--disable-dev-shm-usage");
        return new ChromeDriver(chromeOptions);
    }

    /**
     * Scraps the total number of recipes and returns the value as an integer.
     *
     * @param driver WebDriver to be used for scrapping information.
     * @return Total number of recipes.
     * @author Lucas Larisch
     * @since 12.11.2018
     */
    private int totalRecipes(WebDriver driver) {

        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        WebElement totalRecipesSpan = driver.findElement(By.cssSelector(CSS_QUERY_TOTAL_NUMBER));
        return Integer.parseInt(totalRecipesSpan.getText().trim());
    }

    /**
     * Adds URLs to {@link HelloFreshUnknownUrlsCrawler#allRecipeUrls} as long as the
     * recipe they belong to is not stored in the database yet.
     *
     * @param driver        WebDriver to be used for scrapping information.
     * @param nrOfKnownUrls Number of URLs that have already been scrapped before
     *                      calling this method. (used for performance and logging)
     * @return Size of {@link HelloFreshUnknownUrlsCrawler#allRecipeUrls} after having
     * added new URLs.
     * @author Lucas Larisch
     * @since 12.11.2018
     */
    private int addUrlsToList(WebDriver driver, int nrOfKnownUrls) {

        List<WebElement> recipeLinks = driver.findElements(By.cssSelector(CSS_QUERY_RECIPE_A));

        for (int i = nrOfKnownUrls; i < recipeLinks.size(); i++) {
            String url = recipeLinks.get(i).getAttribute(CSS_QUERY_ATTRIBUTE_HREF);
            if (url.equals(lastFirstUrl)) {
                System.out.println("Recipes haven't been updated");
                return allRecipeUrls.size();
            }
            if (i == nrOfKnownUrls) {
                lastFirstUrl = url;
            }
            System.out.println("Recipe url: " + url);
            // TODO Check ob das Rezept schon in der DB ist
            String identifier = getRecipeIdFromUrl(url);
            if (this.recipeRepository.findByIdentifier(identifier) == null) {
                allRecipeUrls.add(url);
            } else {
                System.out.println(allRecipeUrls.size() - nrOfKnownUrls + " URLs have been added (total: " + allRecipeUrls.size() + "). A known ID has been found.");
                return -1;
            }
        }
        System.out.println("6 URLs have been added (total: " + allRecipeUrls.size() + ").");
        return allRecipeUrls.size();
    }

    /**
     * Pauses the current execution/thread for as many seconds as specified
     * in {@link HelloFreshUnknownUrlsCrawler#SLEEP_INTERVAL}.
     *
     * @author Lucas Larisch
     * @since 12.11.2018
     */
    private void sleep() {
        try {
            TimeUnit.SECONDS.sleep(SLEEP_INTERVAL);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks whether a pop up has appeared. If so, it is closed.
     * If not, an error message is printed out.
     *
     * @param driver WebDriver to be used for scrapping information.
     * @author Lucas Larisch
     * @since 12.11.2018
     */
    private void closePopUpIfExisting(WebDriver driver) {
        if (driver.findElements(By.cssSelector(CSS_QUERY_CLOSE_DIV)).size() != 0) {
            driver.findElement(By.cssSelector(CSS_QUERY_CLOSE_DIV)).click();
        } else {
            System.err.println("An error occurred that could not be solved by closing an appearing pop up.");
        }
    }

}
