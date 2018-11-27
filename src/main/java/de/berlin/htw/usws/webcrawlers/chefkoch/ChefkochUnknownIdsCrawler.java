package de.berlin.htw.usws.webcrawlers.chefkoch;

import de.berlin.htw.usws.repositories.RecipeRepository;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Class used for scrapping unknown recipe-IDs from chefkoch.de.
 *
 * @since 24.10.2018
 * @author Lucas Larisch
 */
@Stateless
public class ChefkochUnknownIdsCrawler extends ChefkochCrawler {

    @Inject
    private RecipeRepository recipeRepository;

    /** Regex for (real) recipe IDs. */
    private final String REGEX_RECIPE_ID ="recipe\\-[0-9]*";

    /** Regex for checking if a string is a (no point) number. */
    private final String REGEX_NUMBER = "[0-9]*";

    /** Relative URL holding the newest recipes. */
    private final String RECIPES_URL = "/rs/s0o3/Rezepte.html";

    /** CSS Query for the search list. */
    private final String CSS_QUERY_SEARCH_LIST = "ul.search-list";

    /** CSS Query for listed items. */
    private final String CSS_QUERY_LISTED_ITEM = "li";

    /** CSS Query for the div concerning the pagination. */
    private final String CSS_QUERY_PAGINATION = "div.ck-pagination";

    /** CSS Query for anchors. */
    private final String CSS_QUERY_A = "a";

    /** CSS Query for hrefs */
    private final String CSS_QUERY_HREF = "href";

    /** Prefix for each recipe. (Chefkoch) */
    private final String RECIPE_PREFIX = "recipe-";

    /** List all unknown IDs are to be added to. */
    private ArrayList<Long> allRecipeIds = new ArrayList<Long>();

    /**
     * Parses all pages for recipe IDs and adds the IDs to {@link ChefkochUnknownIdsCrawler#allRecipeIds}
     * (List that will be returned). This method will run as long as there are (new) pages
     * containing recipes and as long as an ID to be added is not stored in the database yet.
     *
     * @since 24.10.2018
     * @author Lucas Larisch
     * @return List of all IDs representing recipes not yet stored.
     *         ({@link ChefkochUnknownIdsCrawler#allRecipeIds})
     * @throws IOException
     */
    public ArrayList<Long> crawlRecipePages() throws IOException {

        super.appendToBaseUrl(RECIPES_URL);

        boolean isLastKnownIdFound = false;
        boolean isLastPage = false;

        int numberPages = 0;
        // Crawls pages as long as the ID is not found and it is not the last page.
        while(!isLastKnownIdFound && !isLastPage){
            if(numberPages != 100) {
                Document document = getUnlimitedDocument();
                if(document != null) {
                    // Crawls recipes
                    Elements recipeIdsList = document.select(CSS_QUERY_SEARCH_LIST).select(CSS_QUERY_LISTED_ITEM);
                    isLastKnownIdFound = scrapRecipesIdsFromList(recipeIdsList);

                    if (!isLastKnownIdFound) {
                        isLastPage = setUrlForNextPage(document);
                    }
                }
                numberPages++;
            } else {
                break;
            }


        }
        return allRecipeIds;
    }

    /**
     * Adds all IDs from an Elements-list to {@link ChefkochUnknownIdsCrawler#allRecipeIds} as long
     * as they represent a recipe and are not stored in the database yet.
     *
     * @since 24.10.2018
     * @author Lucas Larisch
     * @param recipesIdList Elements-list containing all recipes (minimized) of a page.
     * @return true if a listed ID is already stored in the database, false if none of them is.
     */
    private boolean scrapRecipesIdsFromList(Elements recipesIdList) {
        boolean isKnownIdFound = false;
        for (Element e : recipesIdList) {
            String id = e.id();
            if(id.matches(REGEX_RECIPE_ID)) {
                id = id.replace(RECIPE_PREFIX,"");
                long idNumber = Long.parseLong(id);
                if (this.recipeRepository.findByIdentifier(String.valueOf(idNumber)) != null) {
                    isKnownIdFound = true;
                    break;
                } else {
                    allRecipeIds.add(idNumber);
                    System.out.print("New recipe found. Id: " + id + ". Total recipes: " + allRecipeIds.size() + "\n");
                }
            }
        }
        return isKnownIdFound;
    }

    /**
     * Calls the next page containing minimized recipes (if it exists).
     *
     * @since 24.10.2018
     * @author Lucas Larisch
     * @param document Last parsed page/Document containing the navigation buttons.
     * @return false if the URL could be set (there is a new page),
     *         true in case of no more pages.
     */
    private boolean setUrlForNextPage(Document document) {
        Element nextPage = document.select(CSS_QUERY_PAGINATION).select(CSS_QUERY_A).last();
        boolean isLastPage = false;

        // Number -> disabled "weiter" is not an <a> (but a <span>), which is the reason for the last navigation link directing to a page (3).
        if (nextPage.text().matches(REGEX_NUMBER)) {
            isLastPage = true;
        } else {
            super.appendToBaseUrl(nextPage.attr(CSS_QUERY_HREF));
        }

        return isLastPage;
    }

}

