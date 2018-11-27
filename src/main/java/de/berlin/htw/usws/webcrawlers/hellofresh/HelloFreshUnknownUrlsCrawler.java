package de.berlin.htw.usws.webcrawlers.hellofresh;

import de.berlin.htw.usws.model.enums.RecipeSite;
import de.berlin.htw.usws.repositories.RecipeRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Class used for scrapping URLs of recipes from hellofresh.de that have an ID
 * that is not stored in the database yet.
 *
 * @author Lucas Larisch
 * @since 12.11.2018
 */
@Stateless
public class HelloFreshUnknownUrlsCrawler extends HelloFreshCrawler {

    public static final String HELLO_FRESH_RECIPES_PAGE_URL = "https://www.hellofresh.de/recipes/search/?order=-date";
    @Inject
    private RecipeRepository recipeRepository;

    private ArrayList<String> allRecipeUrls = new ArrayList<>();

    public ArrayList<String> crawlRecipesUrls(){

        int numberHelloFreshRecipes = countHelloFreshRecipesInDB();

        getAuthHeaderWithToken();

        return null;
    }

    public Document getAuthHeaderWithToken() {
        Document doc = null;
        try {
            doc = Jsoup.connect(HELLO_FRESH_RECIPES_PAGE_URL).get();
            Elements scriptElements = doc.getElementsByTag("script");

            for (Element element :scriptElements ){
                for (DataNode node : element.dataNodes()) {
                    String nodeData = node.getWholeData();
                    if(nodeData.contains("Bearer")){
                        int start = nodeData.lastIndexOf("accessToken:");
                        int end = nodeData.lastIndexOf(",expiresIn:");
                        String tokenWithTag = nodeData.substring(start, end);
                        System.out.println(tokenWithTag.substring(13,tokenWithTag.length()-1));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doc;
    }

    private int countHelloFreshRecipesInDB() {
        return recipeRepository.countRecipesBySite(RecipeSite.HELLOFRESH);
    }

}
