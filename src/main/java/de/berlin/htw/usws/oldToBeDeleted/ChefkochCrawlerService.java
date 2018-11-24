//package de.berlin.htw.usws.oldToBeDeleted;
//
//
//import javax.ejb.Stateless;
//import java.io.IOException;
//import java.util.ArrayList;
//
///**
// * Class holding mock-logic and to be deleted once a DB is connected.
// *
// * @author Lucas Larisch
// * @since 24.10.2018
// */
//public class ChefkochCrawlerService {
//
//    /**
//     * Used for collecting IDs of recipes.
//     */
//    private UnknownIdsCrawlerChefkoch unknownIdsCrawler;
//
//    /**
//     * Used for collecting recipes.
//     */
//    private RecipeCrawlerChefkoch recipeCrawler;
//
//    /**
//     * Crawls all unknown IDs and the recipe connected to each ID afterwards.
//     * Whereas the IDs are collected from newest to oldest, the recipes are
//     * parsed starting with the last added ID (= oldest recipe). This algorithm is
//     * based on the logic of {@link UnknownIdsCrawlerChefkoch#crawlRecipePages()}:
//     * The last added recipe will be the newest in the database.
//     * So in case of an error, only newer IDs will be collected (and the recipes
//     * connected to them).
//     *
//     * @author Lucas Larisch
//     * @since 24.10.2018
//     */
//    public void start() {
//        recipeCrawler = new RecipeCrawlerChefkoch();
//
//        try {
//            ArrayList<Long> unknownIds = unknownIdsCrawler.crawlRecipePages();
//            for (int i = unknownIds.size() - 1; i >= 0; i--) {
//                recipeCrawler.scrapRecipe(unknownIds.get(i));
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//}
