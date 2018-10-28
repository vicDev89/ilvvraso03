package de.berlin.htw.usws.webCrawlers;

import de.berlin.htw.usws.model.Product;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;

public class BringmeisterCrawlerTest {

    @Test
    @Ignore
    public void testEmptytest() throws IOException {

        String name = "apfel";

        Product product =  BringmeisterCrawler.getProductForIngredientBRINGMEISTER(name);

        System.out.println(product.getSupermarket().name() + ". Product: " + product.getName() + ". Price_min: " + product.getPriceMin() + ". Price_max: " + product.getPriceMax());
    }
}
