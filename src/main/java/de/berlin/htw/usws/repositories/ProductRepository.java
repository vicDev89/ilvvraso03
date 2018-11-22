package de.berlin.htw.usws.repositories;

import de.berlin.htw.usws.model.Product;
import org.apache.deltaspike.data.api.AbstractFullEntityRepository;
import org.apache.deltaspike.data.api.Repository;

@Repository(forEntity = Product.class)
public abstract class ProductRepository extends AbstractFullEntityRepository<Product, Long> {
}
