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
    private static final String PRODUCT_TILE_CLASS = "search-service-product";
    private static final String PRODUCT_PRICE_CLASS = "search-service-productPrice";
    private static final String PRODUCT_OFFER_PRICE_CLASS = "search-service-productOfferPrice";
    private static final String PRODUCT_NAME_CLASS = "search-service-productTitle";

    private final String REWE_URL = "https://shop.rewe.de/productList?search=";

    private final Integer NUMBER_OF_SCRAPPED_PRODUCTS = 3;


    public List<Product> getProductForIngredientREWE(String ingredientName) throws IOException {

        List<Product> products = new ArrayList<>();

        String searchURL = REWE_URL + ingredientName;

        System.out.println("Rewe URL: " + searchURL);

        Document doc = Jsoup.connect(searchURL).get();

        Elements ProductsContent = doc.getElementsByClass(PRODUCT_TILE_CLASS);

        if (!ProductsContent.isEmpty()) {
            int counter = 0;

            for (Element element : ProductsContent) {
                if (counter < NUMBER_OF_SCRAPPED_PRODUCTS) {

                    String productName = element.getElementsByClass(PRODUCT_NAME_CLASS).text();

                    String price = element.getElementsByClass(PRODUCT_PRICE_CLASS).text();

                    if(price == "" || price == null) {
                        price = element.getElementsByClass(PRODUCT_OFFER_PRICE_CLASS).text();
                    }

                    price = price.replace("€", "");
                    price = price.replace(",", ".");

                    double priceDouble = 0;
                    try {
                         priceDouble = Double.parseDouble(price);
                    } catch (NumberFormatException e) {
                        System.err.println("Price could not be parsed. URL: " + searchURL);
                    }

                    products.add(new Product(productName, Supermarket.REWE, priceDouble));

                    counter++;
                }
            }

            return products;

        } else {
            return null;
        }

    }

}
