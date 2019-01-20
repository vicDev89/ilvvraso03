package de.berlin.htw.usws.webcrawlers.hellofresh;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.berlin.htw.usws.model.enums.RecipeSite;
import de.berlin.htw.usws.repositories.RecipeRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Class used for scrapping URLs of recipes from hellofresh.de that have an ID
 * that is not stored in the database yet.
 *
 * @author Ido Klimovsky
 * @since 27.11.2018
 */
@Stateless
public class HelloFreshUnknownUrlsCrawler extends HelloFreshCrawler {

    private static final String HELLO_FRESH_RECIPES_PAGE_URL = "https://www.hellofresh.de/recipes/search/?order=-date";
    private static final int LIMIT = 10;

    @Inject
    private RecipeRepository recipeRepository;

    public List<String> getUrlsForNewRecipes() {

        List<String> result = new ArrayList<>();

        int scrapedUrlsAmount = LIMIT;
        int counter = 0;

        int startOffset = countHelloFreshRecipesInDB();
        String token = getAuthTokenFromHelloFresh();

        while(scrapedUrlsAmount == LIMIT){

            HttpURLConnection con = null;
            StringBuffer content = null;
            HelloFreshRecipesResponse helloFreshRecipesResponse = null;

            try {
                con = getHttpURLConnection(startOffset, counter, token);
                content = getStringBuffer(con);
                con.disconnect();
                helloFreshRecipesResponse = getHelloFreshRecipesResponse(content);

            } catch (IOException e) {
                e.printStackTrace();
            }

            if(helloFreshRecipesResponse != null){
                List<String> urlsFromResponse = getUrlsFromResponse(helloFreshRecipesResponse);
                scrapedUrlsAmount = urlsFromResponse.size();
                result.addAll(urlsFromResponse);
                counter ++;
            } else {
                scrapedUrlsAmount = 0;
            }
        }
        System.out.println("new "+result.size()+ " recipes have been scraped");

        return result;
    }

    private StringBuffer getStringBuffer(HttpURLConnection con) throws IOException {
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }

        in.close();
        return content;
    }

    private HttpURLConnection getHttpURLConnection(int startOffset, int counter, String token) throws IOException {
        URL url = new URL("https://gw.hellofresh.com/api/recipes/search?offset="+(startOffset + counter * LIMIT)+ "&limit=" + LIMIT + "&order=date&locale=de-DE&country=de");
        System.out.println("Recipes from :" + (startOffset + counter * LIMIT) + " until: " + (startOffset +LIMIT + counter * LIMIT) +" are now crawled");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty("Authorization", "Bearer " + token);
        con.setRequestMethod("GET");
        return con;
    }

    private List<String> getUrlsFromResponse(HelloFreshRecipesResponse helloFreshRecipesResponse) {
        List<String> result = new ArrayList<>();
        for (HelloFreshRecipe item : helloFreshRecipesResponse.getItems()) {
            result.add(item.getWebsiteUrl());
        }
        return result;
    }

    private HelloFreshRecipesResponse getHelloFreshRecipesResponse(StringBuffer content) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper.readValue(String.valueOf(content), HelloFreshRecipesResponse.class);
    }

    public String getAuthTokenFromHelloFresh() {
        String token = null;
        try {
            Document doc = Jsoup.connect(HELLO_FRESH_RECIPES_PAGE_URL).get();
            Elements scriptElements = doc.getElementsByTag("script");

            for (Element element :scriptElements ){
                for (DataNode node : element.dataNodes()) {
                    String nodeData = node.getWholeData();
                    if(nodeData.contains("Bearer")){
                        int start = nodeData.lastIndexOf("accessToken:");
                        int end = nodeData.lastIndexOf(",expiresIn:");
                        String tokenWithTag = nodeData.substring(start, end);
                        token = tokenWithTag.substring(13, tokenWithTag.length() - 1);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return token;
    }

    private int countHelloFreshRecipesInDB() {
        return recipeRepository.countRecipesBySite(RecipeSite.HELLOFRESH);
    }

}
