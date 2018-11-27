package de.berlin.htw.usws.webcrawlers.rewe;

import de.berlin.htw.usws.model.Product;
import de.berlin.htw.usws.model.enums.Supermarket;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Stateless
public class ReweCrawler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReweCrawler.class);
    private static final String PRODUCT_TILE_CLASS = "search-service-ProductTileContent";
    private static final String PRODUCT_EURO_CLASS = "search-service-ProductPriceInteger";
    private static final String PRODUCT_CENT_CLASS = "search-service-ProductPriceDecimal";
    private static final String PRODUCT_NAME_CLASS = "search-service-productTitle";

    private final String REWE_URL = "https://shop.rewe.de/productList?search=";

    private final Integer NUMBER_OF_SCRAPPED_PRODUCTS = 3;


    public List<Product> getProductForIngredientREWE(String ingredientName) throws IOException {

        List<Product> products = new ArrayList<>();

        String searchURL = REWE_URL + ingredientName;

        Document doc = Jsoup.connect(searchURL).get();

        Elements ProductsContent = doc.getElementsByClass(PRODUCT_TILE_CLASS);

        if (!ProductsContent.isEmpty()) {
            int counter = 0;

            for (Element element : ProductsContent) {
                if (counter < NUMBER_OF_SCRAPPED_PRODUCTS) {

                    String productName = element.getElementsByClass(PRODUCT_NAME_CLASS).text();

                    Elements euro = element.getElementsByClass(PRODUCT_EURO_CLASS);
                    String euroString = euro.first().text();

                    Elements cent = element.getElementsByClass(PRODUCT_CENT_CLASS);
                    String centString = cent.first().text();

                    double price = Double.parseDouble(euroString + "." + centString);
                    products.add(new Product(productName, Supermarket.REWE, price));

                    counter++;
                }
            }

            return products;

        } else {
            return null;
        }

    }

}
