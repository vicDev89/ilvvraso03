package de.berlin.htw.usws.services;

import de.berlin.htw.usws.model.Product;
import de.berlin.htw.usws.repositories.ProductRepository;

import javax.inject.Inject;

public class ProductService {

    @Inject
    private ProductRepository productRepository;

    public void persistProduct(Product product) {
        this.productRepository.save(product);
    }
}
