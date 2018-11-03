package de.berlin.htw.usws.webCrawlers;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.berlin.htw.usws.model.BringmeisterProduct;
import de.berlin.htw.usws.model.BringmeisterProductPage;
import de.berlin.htw.usws.model.Product;
import de.berlin.htw.usws.model.Supermarket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

public class BringmeisterProductAPI {

    private static final String ZIP_CODE = "13355";

    Product getProduct(String productName){
        BringmeisterProductPage bringmeisterProductPage = null;
        ArrayList<Double> pricesList = new ArrayList<Double>();

        try {
            bringmeisterProductPage = getBringmeisterProductPageForProduct(productName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(bringmeisterProductPage != null){
            for (BringmeisterProduct product : bringmeisterProductPage.getProducts()) {
                pricesList.add(product.getPrice());
            }
        }

        Collections.sort(pricesList);

        return new Product(productName, Supermarket.BRINGMEISTER, pricesList.get(0), pricesList.get(pricesList.size()-1));
    }

    private BringmeisterProductPage getBringmeisterProductPageForProduct(String productName) throws IOException {
        URL url = new URL("https://www.bringmeister.de/api/products?limit=60&offset=0&q="+ productName + "&sorting=default&zipcode=" + ZIP_CODE);
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
