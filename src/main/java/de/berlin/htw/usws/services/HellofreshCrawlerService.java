package de.berlin.htw.usws.services;

import de.berlin.htw.usws.model.Recipe;
import de.berlin.htw.usws.webcrawlers.hellofresh.HelloFreshRecipeCrawler;
import de.berlin.htw.usws.webcrawlers.hellofresh.HelloFreshUnknownUrlsCrawler;

import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class HellofreshCrawlerService {

    private HelloFreshUnknownUrlsCrawler urlCrawler;

    private HelloFreshRecipeCrawler recipeCrawler;

    public List<Recipe> start() {
        urlCrawler = new HelloFreshUnknownUrlsCrawler();

        List<Recipe> recipes = new ArrayList<>();

        ArrayList<String> unknownUrls = urlCrawler.crawlRecipePage();
        recipeCrawler = new HelloFreshRecipeCrawler();
        for (int i = unknownUrls.size() - 1; i >= 0; i--) {
            recipes.add(recipeCrawler.scrapRecipe(unknownUrls.get(i)));
        }

        return recipes;
    }
}
