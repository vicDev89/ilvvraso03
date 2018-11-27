package de.berlin.htw.usws.webcrawlers.bringmeister;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.berlin.htw.usws.model.Product;
import de.berlin.htw.usws.model.enums.Supermarket;

import javax.ejb.Stateless;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Stateless
public class BringmeisterProductAPI {

    private static final String ZIP_CODE = "13355";

    private final Integer NUMBER_OF_SCRAPPED_PRODUCTS = 3;

    public List<Product> getProducts(String productName) {
        List<Product> products = new ArrayList<>();

        BringmeisterProductPage bringmeisterProductPage = null;

        int counter = 0;

        try {
            bringmeisterProductPage = getBringmeisterProductPageForProduct(productName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (bringmeisterProductPage != null) {
            for (BringmeisterProduct product : bringmeisterProductPage.getProducts()) {
                if (counter < NUMBER_OF_SCRAPPED_PRODUCTS) {
                    products.add(new Product(product.getName(), Supermarket.EDEKA, product.getPrice()));
                    counter++;
                }
            }
        }

        return products;

    }

    private BringmeisterProductPage getBringmeisterProductPageForProduct(String productName) throws IOException {
        productName = productName.replaceAll(" ", "%20");
        URL url = new URL("https://www.bringmeister.de/api/products?limit=60&offset=0&q=" + productName + "&sorting=default&zipcode=" + ZIP_CODE);
        System.out.println("Bringmeister url: " + url);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }

        in.close();
        con.disconnect();

        return convertJSONtoProductPageObject(content);
    }

    private BringmeisterProductPage convertJSONtoProductPageObject(StringBuffer content) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return (BringmeisterProductPage) objectMapper.readValue(String.valueOf(content), BringmeisterProductPage.class);
    }
}
