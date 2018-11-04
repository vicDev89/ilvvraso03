package de.berlin.htw.usws.webcrawlers;

import de.berlin.htw.usws.model.Product;
import de.berlin.htw.usws.model.Supermarket;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class BringmeisterCrawler {

    private final static String EDEKA_URL = "https://www.bringmeister.de/catalogsearch/results?q=";

    public static final String PRODUCT_DIV_CLASS = "_1lp36";
    public static final String PRODUCT_PRICE_CLASS = "_1aMgV BGQpt";
    public static final String PRODUCT_PICTURE = "_2PXqC";
    public static final String PRODUCT_NAME = "MTKh1";


    public static Product getProductForIngredientBRINGMEISTER(String ingredientName) throws IOException {

        String searchURL = EDEKA_URL + ingredientName;

        Document doc = Jsoup.connect(searchURL).get();

        ArrayList<Double> pricesList = new ArrayList<Double>();

        Elements ProductsContent = doc.getElementsByClass(PRODUCT_DIV_CLASS);

        for (Element element : ProductsContent) {
            String name = element.getElementsByClass(PRODUCT_NAME).text();

            // Plural-Suche des PRODUCT_NAMES über PONS-API?
            if(!name.matches(String.format(".*\\b" +PRODUCT_NAME + "$")) && !name.matches(String.format(".*\\bÄpfel$"))) {
                continue;
            }
            Elements euro = element.getElementsByClass(PRODUCT_PRICE_CLASS);
            String euroString = euro.text();
            euroString= euroString.replace(" €", "");
            pricesList.add(Double.parseDouble(euroString));
        }

        Collections.sort(pricesList);

        return new Product(ingredientName, Supermarket.BRINGMEISTER, pricesList.get(0), pricesList.get(pricesList.size() - 1));

    }
}
