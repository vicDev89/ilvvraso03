package de.berlin.htw.usws.webcrawlers;

import de.berlin.htw.usws.model.Product;
import de.berlin.htw.usws.webcrawlers.rewe.ReweCrawler;
import org.junit.Test;

import java.io.IOException;

public class ReweCrawlerTest {

    private ReweCrawler reweCrawler = new ReweCrawler();

    @Test
    public void testGetProductForIngredientREWE() throws IOException {

        Product banane = reweCrawler.getProductForIngredientREWE("Banane");

        System.out.print(banane.toString());
    }
}
