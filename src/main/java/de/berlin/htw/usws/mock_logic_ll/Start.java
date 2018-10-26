package de.berlin.htw.usws.mock_logic_ll;

import de.berlin.htw.usws.webCrawlers.RecipeCrawlerChefkoch;

public class Start {

    public static void main(String[] args) {
      //  new MockLogic().start();
        new RecipeCrawlerChefkoch().scrapRecipe("3593891540449959");
    }

}
