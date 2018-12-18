package de.berlin.htw.usws.webcrawlers.foodboom;

import de.berlin.htw.usws.repositories.RecipeRepository;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for crawling foodboom.de unknown URLs to recipes.
 *
 * @author Lucas Larisch
 * @since 09.12.2018
 */
@Stateless
public class FoodBoomUnknownUrlsCrawler extends FoodBoomCrawler {

    @Inject
    private RecipeRepository recipeRepository;

    /**
     * String to be appended to the base URL in order to go to the pages listing recipes.
     */
    private final String TO_APPEND_TO_BASE_URL = "rezepte?page=";

    /**
     * Number of the page listing recipes to be scrapped.
     */
    private int pageCount = 1;

    /**
     * CSS Query for getting all anchors concerning recipes.
     */
    private final String CSS_QUERY_RECIPE_ANCHOR = "a.card.card--hover.node.node--type-recipe.node--view-mode-teaser";

    /** CSS Query for the anchor for getting to the last page. */
    private final String CSS_QUERY_LAST_PAGE_ANCHOR = "li.pager__item.pager__item--last a";

    /**
     * CSS Query for getting a href-attribute.
     */
    private final String CSS_QUERY_ATTRIBUTE_HREF = "href";

    /**
     * Crawls all listed recipes and returns all unknown URLs.
     *
     * @return All unknown URLs.
     * @author Lucas Larisch
     * @since 09.12.2018
     */
    public List<String> getUrlsForNewRecipes() {
        ArrayList<String> urls = null;
        try {
            int lastPageNr = getLastPageNr();
            urls = crawlPagesForUrls(lastPageNr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return urls;
    }

    /**
     * Calls the first recipe page in order to scrap the number of the last page listing recipes and return it.
     *
     * @return Number of the last page.
     * @author Lucas Larisch
     * @since 17.12.2018
     */
    private int getLastPageNr() {
        int lastPage = 0;
        try {
            Document document = setNextPage();
            pageCount = 1;
            Elements lastPageAnchor = document.select(CSS_QUERY_LAST_PAGE_ANCHOR);
            if (lastPageAnchor.size() > 0){
                String lastPageUrl = lastPageAnchor.first().attr(CSS_QUERY_ATTRIBUTE_HREF);
                if (lastPageUrl.charAt(0)=='/') {
                    lastPageUrl = lastPageUrl.replaceFirst("/", "");
                }
                try {
                    lastPage = Integer.parseInt(lastPageUrl.replace(TO_APPEND_TO_BASE_URL, ""));
                    System.out.println(lastPage + " has been set as last page number.");
                }catch (NumberFormatException e) {
                    System.err.println("Number of the last page could not be determined due to issues concerning the number-conversion.");
                }
            }
        } catch (IOException e) {
            System.err.println("Number of the last page could not be determined due to issues concerning the connection.");
            e.printStackTrace();
        } catch(Exception e) {
            System.err.println(e.getMessage());
        }
        return lastPage;
    }

    /**
     * Crawls all pages listing recipes and returns a list containing each
     * URL to an unknown recipe.
     *
     * @param lastPageNr Number of the last page listing recipes.
     * @return All unknown URLs to recipes.
     * @throws IOException
     * @author Lucas Larisch
     * @since 09.12.2018
     */
    private ArrayList<String> crawlPagesForUrls(int lastPageNr) throws Exception {
        ArrayList<String> recipeUrls = new ArrayList<String>();
        while(pageCount <= lastPageNr) {
            Document document = setNextPage();
            if (document != null) {
                Elements recipeAnchors = document.select(CSS_QUERY_RECIPE_ANCHOR);
                for (Element anchor : recipeAnchors) {
                    String url = anchor.attr(CSS_QUERY_ATTRIBUTE_HREF);
                    if (isRecipeUnknown(url)) {
                        recipeUrls.add(url);
                        System.out.println("New Foodboom-Recipe url added: [FOODBOOM] " + url + ". Total: " + recipeUrls.size());
                    }
                }
            }
        }
        return recipeUrls;
    }

    /**
     * Checks whether a recipe is already known or not and returns the result.
     *
     * @param relativeUrl Relative url to the recipe.
     * @return true if the recipe is unknown, false if not.
     * @author Lucas Larisch
     * @since 09.12.2018
     */
    private boolean isRecipeUnknown(String relativeUrl) {
        return this.recipeRepository.findByUrl(relativeUrl) == null;
    }

    /**
     * Calls the next page depending on {@link FoodBoomUnknownUrlsCrawler#pageCount} and returns it.s
     *
     * @return Called next page (depending on {@link FoodBoomUnknownUrlsCrawler#pageCount}) listing recipes.
     * @throws IOException
     * @author Lucas Larisch
     * @since 17.12.2018
     */
    private Document setNextPage() throws Exception {
        appendToBaseUrl(TO_APPEND_TO_BASE_URL +  pageCount++);
        Document document = null;
        try {
            document = getUnlimitedDocument();
        } catch(SocketTimeoutException e) {
            System.err.println("FoodBoom recipes page Nr. "+(pageCount-1)+ " could not be called. (Read time out)");
        } catch (Exception e) {
            System.err.println("FoodBoom recipes page Nr. \"+(pageCount-1)+ \" could not be called. " + e.getMessage());
        }
        return document;
    }
}
