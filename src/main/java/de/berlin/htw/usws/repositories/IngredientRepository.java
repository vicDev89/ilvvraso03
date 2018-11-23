package de.berlin.htw.usws.repositories;

import de.berlin.htw.usws.model.Ingredient;

import org.apache.deltaspike.data.api.AbstractFullEntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;
import org.apache.deltaspike.data.api.SingleResultType;

@Repository(forEntity = Ingredient.class)
public abstract class IngredientRepository  extends AbstractFullEntityRepository<Ingredient, Long> {

    @Query(named = Ingredient.BY_NAME, singleResult = SingleResultType.OPTIONAL)
    public abstract Ingredient findByName(final String name);

}
