package de.berlin.htw.usws.webCrawlers;

import de.berlin.htw.usws.model.Product;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class BringmeisterProductAPITest {

    BringmeisterProductAPI bringmeisterProductAPI = new BringmeisterProductAPI();

    @Test
    public void getProduct() {

            Product test = bringmeisterProductAPI.getProduct("banane");

            Product test2 = bringmeisterProductAPI.getProduct("Erdbeeren");

            System.out.println(test.toString());
            System.out.println(test2.toString());
    }
}