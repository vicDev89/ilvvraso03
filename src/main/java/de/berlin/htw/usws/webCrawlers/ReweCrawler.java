package de.berlin.htw.usws.webCrawlers;

import de.berlin.htw.usws.model.Product;
import de.berlin.htw.usws.model.Supermarket;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class ReweCrawler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReweCrawler.class);
    public static final String PRODUCT_TILE_CLASS = "search-service-ProductTileContent";
    public static final String PRODUCT_EURO_CLASS = "search-service-ProductPriceInteger";
    public static final String PRODUCT_CENT_CLASS = "search-service-ProductPriceDecimal";

    private final String REWE_URL = "https://shop.rewe.de/productList?search=";

    //TODO: Filter irrelevant products
    //TODO: access multiple pages
    public Product getProductForIngredientREWE(String ingredientName) throws IOException {

        String searchURL = REWE_URL + ingredientName;

        Document doc = Jsoup.connect(searchURL).get();

        ArrayList<Double> pricesList = new ArrayList<Double>();

        Elements ProductsContent = doc.getElementsByClass(PRODUCT_TILE_CLASS);

        for (Element element : ProductsContent) {
            Elements euro = element.getElementsByClass(PRODUCT_EURO_CLASS);
            String euroString = euro.first().text();

            Elements cent = element.getElementsByClass(PRODUCT_CENT_CLASS);
            String centString = cent.first().text();

            pricesList.add(Double.parseDouble(euroString + "." + centString));
        }

        Collections.sort(pricesList);

        return new Product(ingredientName,Supermarket.REWE, pricesList.get(0), pricesList.get(pricesList.size()-1) );

    }


}
