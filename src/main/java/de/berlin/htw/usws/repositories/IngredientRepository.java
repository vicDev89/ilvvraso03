package de.berlin.htw.usws.repositories;

import de.berlin.htw.usws.model.Ingredient;
import org.apache.deltaspike.data.api.*;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository(forEntity = Ingredient.class)
public abstract class IngredientRepository extends AbstractFullEntityRepository<Ingredient, Long> {

    @Query(named = Ingredient.BY_NAME, singleResult = SingleResultType.OPTIONAL)
    public abstract Ingredient findByName(final String name);

    @EntityGraph("withProducts")
    @Query(named = Ingredient.BY_ALL, singleResult = SingleResultType.OPTIONAL)
    public abstract List<Ingredient> findAllIngredients();
}
