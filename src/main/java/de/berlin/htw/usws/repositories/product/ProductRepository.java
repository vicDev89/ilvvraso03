package de.berlin.htw.usws.repositories.product;

import de.berlin.htw.usws.model.Product;
import org.apache.deltaspike.data.api.AbstractFullEntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;
import org.apache.deltaspike.data.api.SingleResultType;

/**
 * Product-Repository mit Funktionen, die für alle Services aufrufbar sind
 */
@Repository(forEntity = Product.class)
public abstract class ProductRepository extends AbstractFullEntityRepository<Product, Long> {

    /**
     * Search for a product by ID
     * @param id
     * @return
     */
    @Query(named = Product.BY_ID, singleResult = SingleResultType.OPTIONAL)
    public abstract Product findBy(final Long id);

    /**
     * Remove a product
     * @param id
     */
    public void deleteById(final Long id) {
        this.remove(this.findBy(id));
    }
}
