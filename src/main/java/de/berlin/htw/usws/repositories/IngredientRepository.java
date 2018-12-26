package de.berlin.htw.usws.repositories;

import de.berlin.htw.usws.model.Ingredient;
import org.apache.deltaspike.data.api.*;

import java.util.List;

@Repository(forEntity = Ingredient.class)
public abstract class IngredientRepository extends AbstractFullEntityRepository<Ingredient, Long> {

    @Query(named = Ingredient.BY_NAME, singleResult = SingleResultType.OPTIONAL)
    public abstract Ingredient findByName(final String name);

    @EntityGraph("withProducts")
    @Query(named = Ingredient.BY_ALL, singleResult = SingleResultType.OPTIONAL)
    public abstract List<Ingredient> findAllIngredients();

}
