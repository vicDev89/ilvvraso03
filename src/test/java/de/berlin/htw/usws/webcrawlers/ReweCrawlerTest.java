package de.berlin.htw.usws.webcrawlers;

import de.berlin.htw.usws.model.Product;
import de.berlin.htw.usws.webcrawlers.rewe.ReweCrawler;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class ReweCrawlerTest {

    private ReweCrawler reweCrawler = new ReweCrawler();

    @Test
    public void testGetProductForIngredientREWE() throws IOException {

        List<Product> bananeProducts = reweCrawler.getProductForIngredientREWE("Banane");

        for(Product product : bananeProducts) {
            System.out.println(product.toString());
        }
    }
}
