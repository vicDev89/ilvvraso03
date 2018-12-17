package de.berlin.htw.usws.webcrawlers;

import de.berlin.htw.usws.model.Product;
import de.berlin.htw.usws.webcrawlers.bringmeister.BringmeisterProductAPI;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

public class BringmeisterProductAPITest {

    BringmeisterProductAPI bringmeisterProductAPI = new BringmeisterProductAPI();

    @Test
    @Ignore
    public void getProduct() {

            List<Product> bananeProducts = bringmeisterProductAPI.getProducts("hänchen");

            for(Product product: bananeProducts) {
                System.out.println(product);
            }

    }
}