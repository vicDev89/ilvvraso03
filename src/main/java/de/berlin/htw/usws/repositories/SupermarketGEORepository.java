package de.berlin.htw.usws.repositories;

import de.berlin.htw.usws.model.*;
import de.berlin.htw.usws.model.enums.Supermarket;
import org.apache.deltaspike.data.api.AbstractFullEntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;
import org.apache.deltaspike.data.api.SingleResultType;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository(forEntity = SupermarketGEO.class)
public abstract class SupermarketGEORepository extends AbstractFullEntityRepository<SupermarketGEO, Long> {

    @Query(named = SupermarketGEO.BY_MARKTID_SUPERMARKET, singleResult = SingleResultType.OPTIONAL)
    public abstract SupermarketGEO findByMarktIDAndSupermarket(final String marktID, final Supermarket supermarket);


    public List<SupermarketGEO> findSupermarketGEOExakt(final SupermarketGEO supermarketGEO) {

        final CriteriaBuilder builder = this.entityManager().getCriteriaBuilder();
        final CriteriaQuery<SupermarketGEO> cQuery = builder.createQuery(SupermarketGEO.class);
        final Root<SupermarketGEO> rootSupermarketGEO = cQuery.from(SupermarketGEO.class);
        final List<Predicate> predicates = new ArrayList<>();

        predicates.add(builder.equal(rootSupermarketGEO.get(SupermarketGEO_.marketID), supermarketGEO.getMarketID()));
        predicates.add(builder.equal(rootSupermarketGEO.get(SupermarketGEO_.supermarket), supermarketGEO.getSupermarket()));
        predicates.add(builder.equal(rootSupermarketGEO.get(SupermarketGEO_.supermarketName), supermarketGEO.getSupermarketName()));
        predicates.add(builder.equal(rootSupermarketGEO.get(SupermarketGEO_.lat), supermarketGEO.getLat()));
        predicates.add(builder.equal(rootSupermarketGEO.get(SupermarketGEO_.lng), supermarketGEO.getLng()));
        predicates.add(builder.equal(rootSupermarketGEO.get(SupermarketGEO_.street), supermarketGEO.getStreet()));
        if(supermarketGEO.getHousenumber() != null) {
            predicates.add(builder.equal(rootSupermarketGEO.get(SupermarketGEO_.housenumber), supermarketGEO.getHousenumber()));
        }
        predicates.add(builder.equal(rootSupermarketGEO.get(SupermarketGEO_.city), supermarketGEO.getCity()));
        predicates.add(builder.equal(rootSupermarketGEO.get(SupermarketGEO_.zip), supermarketGEO.getZip()));
        predicates.add(builder.equal(rootSupermarketGEO.get(SupermarketGEO_.phonenumber), supermarketGEO.getPhonenumber()));


        cQuery.select(rootSupermarketGEO).where(predicates.toArray(new Predicate[]{}));

        List<SupermarketGEO> list = this.entityManager().createQuery(cQuery).getResultList();

        return list;
    }

}
