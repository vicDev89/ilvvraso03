package de.berlin.htw.usws.services;

import de.berlin.htw.usws.model.Recipe;
import de.berlin.htw.usws.webcrawlers.hellofresh.HelloFreshRecipeCrawler;
import de.berlin.htw.usws.webcrawlers.hellofresh.HelloFreshUnknownUrlsCrawler;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class HellofreshCrawlerService {

    @Inject
    private HelloFreshUnknownUrlsCrawler urlCrawler;

    private HelloFreshRecipeCrawler recipeCrawler;

    public List<Recipe> start() {

        List<Recipe> recipes = new ArrayList<>();

        List<String> unknownUrls = this.urlCrawler.getUrlsForNewRecipes();
        recipeCrawler = new HelloFreshRecipeCrawler();
        for (int i = unknownUrls.size() - 1; i >= 0; i--) {
            System.out.println("\n#### Recipe url: " + unknownUrls.get(i) + " ####");
            Recipe recipe = recipeCrawler.scrapRecipe(unknownUrls.get(i));
            if(recipe!=null) {
                recipes.add(recipe);
            } else {
                System.err.println("\n#### Recipe url: " + unknownUrls.get(i) + " was null ####");
            }
        }
        return recipes;
    }
}
