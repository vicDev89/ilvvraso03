package de.berlin.htw.usws.webcrawlers;

import de.berlin.htw.usws.model.Product;
import de.berlin.htw.usws.webcrawlers.bringmeister.BringmeisterProductAPI;
import org.junit.Test;

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