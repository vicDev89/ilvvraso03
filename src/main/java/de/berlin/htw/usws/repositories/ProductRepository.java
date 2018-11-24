package de.berlin.htw.usws.repositories;

import de.berlin.htw.usws.model.Product;
import de.berlin.htw.usws.model.enums.Supermarket;
import org.apache.deltaspike.data.api.AbstractFullEntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;
import org.apache.deltaspike.data.api.SingleResultType;

@Repository(forEntity = Product.class)
public abstract class ProductRepository extends AbstractFullEntityRepository<Product, Long> {

    @Query(named = Product.BY_NAME_AND_SUPERMARKET, singleResult = SingleResultType.OPTIONAL)
    public abstract Product findByNameAndSupermarket(final String name, final Supermarket supermarket);
}
