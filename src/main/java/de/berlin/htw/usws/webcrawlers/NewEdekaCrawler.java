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

public class NewEdekaCrawler {


    private final static String EDEKA_URL = "https://www.edeka24.de/index.php?stoken=55E0C353&force_sid=1k11l7ktpd569b0a166e817c06&lang=0&cl=search&searchparam=";

    public static final String PRODUCT_DIV_CLASS = "product-item";
    public static final String PRODUCT_PRICE_CLASS = "price";


    public static Product getProductForIngredientEDEKA(String ingredientName) throws IOException {

        String searchURL = EDEKA_URL + ingredientName;

        Document doc = Jsoup.connect(searchURL).get();

        ArrayList<Double> pricesList = new ArrayList<Double>();

        Elements ProductsContent = doc.getElementsByClass(PRODUCT_DIV_CLASS);

        for (Element element : ProductsContent) {
            Elements euro = element.getElementsByClass(PRODUCT_PRICE_CLASS);
            String euroString = euro.text();
            euroString= euroString.replace(",", ".");
            euroString= euroString.replace(" €", "");
            pricesList.add(Double.parseDouble(euroString));
        }

        Collections.sort(pricesList);

        return new Product(ingredientName, Supermarket.EDEKA, pricesList.get(0), pricesList.get(pricesList.size() - 1));

    }
}
