package de.berlin.htw.usws.starter;

import de.berlin.htw.usws.model.Product;
import de.berlin.htw.usws.webCrawlers.EdekaCrawler;

import java.util.List;

public class starter {

    public void main(String[] args) {
        List<Product> edekaProducts = EdekaCrawler.getAllProducts();
    }
}
