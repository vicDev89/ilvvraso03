package de.berlin.htw.usws.services;

import de.berlin.htw.usws.model.Recipe;
import de.berlin.htw.usws.webcrawlers.hellofresh.HelloFreshRecipeCrawler;
import de.berlin.htw.usws.webcrawlers.hellofresh.HelloFreshUnknownUrlsCrawlerOld;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class HellofreshCrawlerService {

    @Inject
    private HelloFreshUnknownUrlsCrawlerOld urlCrawler;

    private HelloFreshRecipeCrawler recipeCrawler;

    public List<Recipe> start() {

        List<Recipe> recipes = new ArrayList<>();

        ArrayList<String> unknownUrls = this.urlCrawler.crawlRecipePage();
        recipeCrawler = new HelloFreshRecipeCrawler();
        for (int i = unknownUrls.size() - 1; i >= 0; i--) {
            recipes.add(recipeCrawler.scrapRecipe(unknownUrls.get(i)));
        }

        return recipes;
    }
}
