package de.berlin.htw.usws.webcrawlers.foodboom;

import de.berlin.htw.usws.repositories.RecipeRepository;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.IOException;
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
    private final String TO_APPEND_TO_BASE_URL = "rezepte";

    /**
     * CSS Query for getting all anchors concerning recipes.
     */
    private final String CSS_QUERY_RECIPE_ANCHOR = "a.card.card--hover.node.node--type-recipe.node--view-mode-teaser";

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
        appendToBaseUrl(TO_APPEND_TO_BASE_URL);
        ArrayList<String> urls = null;
        try {
            urls = crawlPagesForUrls();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return urls;
    }

    /**
     * Crawls all pages listing recipes and returns a list containing each
     * URL to an unknown recipe.
     *
     * @return All unknown URLs to recipes.
     * @throws IOException
     * @author Lucas Larisch
     * @since 09.12.2018
     */
    private ArrayList<String> crawlPagesForUrls() throws IOException {
        boolean hasNextPage = false;
        ArrayList<String> recipeUrls = new ArrayList<String>();
        do {
            Document document = getUnlimitedDocument();
            Elements recipeAnchors = document.select(CSS_QUERY_RECIPE_ANCHOR);
            for (Element anchor : recipeAnchors) {
                String url = anchor.attr(CSS_QUERY_ATTRIBUTE_HREF);
                if (isRecipeUnknown(url)) {
                    recipeUrls.add(url);
                    System.out.println("New Foodboom-Recipe url added: "+ getUrl() +". Gesamte Anzahl: " + recipeUrls.size());
                }
            }
            hasNextPage = setNextPage(document);
        } while(hasNextPage);
        return recipeUrls;
    }

    /**
     * Checks whether a recipe is already known or not and returns the result.
     *
     * @param relativeUrl
     * @return true if the recipe is unknown, false if not.
     * @author Lucas Larisch
     * @since 09.12.2018
     */
    private boolean isRecipeUnknown(String relativeUrl) {
        String identifier = getRecipeIdFromRelativeUrl(relativeUrl);
        if(this.recipeRepository.findByIdentifier(identifier) == null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Sets {@link de.berlin.htw.usws.webcrawlers.generic.Crawler#url} if there is
     * a next page listing recipes.
     *
     * @param document Page listing recipes.
     * @return true if there is a next page (that has been set), false if not.
     * @author Lucas Larisch
     * @since 09.12.2018
     */
    private boolean setNextPage(Document document) {
        Elements anchorsToNextPage = document.select("a[rel=next]");
        if (anchorsToNextPage.size() > 0) {
            appendToBaseUrl(anchorsToNextPage.first().attr(CSS_QUERY_ATTRIBUTE_HREF));
            return true;
        } else {
            return false;
        }
    }
}
