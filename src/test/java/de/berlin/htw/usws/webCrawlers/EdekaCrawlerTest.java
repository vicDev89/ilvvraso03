package de.berlin.htw.usws.webCrawlers;



import de.berlin.htw.usws.model.Product;
import org.junit.Test;

import java.io.IOException;


public class EdekaCrawlerTest
{

    @Test

    public void testEmptytest() throws IOException {

        String name = "Tomate";

        Product product =  NewEdekaCrawler.getProductForIngredientEDEKA(name);

        System.out.println(product.getSupermarket().name() + ". Product: " + product.getName() + ". Price_min: " + product.getPriceMin() + ". Price_max: " + product.getPriceMax());
    }

}
