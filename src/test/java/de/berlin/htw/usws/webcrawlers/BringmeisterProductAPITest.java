package de.berlin.htw.usws.webcrawlers;

import de.berlin.htw.usws.model.Product;
import de.berlin.htw.usws.webcrawlers.bringmeister.BringmeisterProductAPI;
import org.junit.Test;

import java.util.List;

public class BringmeisterProductAPITest {

    BringmeisterProductAPI bringmeisterProductAPI = new BringmeisterProductAPI();

    @Test
    public void getProduct() {

            List<Product> bananeProducts = bringmeisterProductAPI.getProducts("banane");

            List<Product> erdbeerenProducts = bringmeisterProductAPI.getProducts("Erdbeeren");

            for(Product product: bananeProducts) {
                System.out.println(product);
            }

            for(Product product: erdbeerenProducts) {
                System.out.println(product);
            }
    }
}