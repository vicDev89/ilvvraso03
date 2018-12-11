package de.berlin.htw.usws.repositories;

import de.berlin.htw.usws.model.Ingredient;
import de.berlin.htw.usws.model.IngredientInRecipe;
import de.berlin.htw.usws.model.Recipe;
import org.apache.deltaspike.data.api.AbstractFullEntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;
import org.apache.deltaspike.data.api.SingleResultType;

import java.util.List;

@Repository(forEntity = IngredientInRecipe.class)
public abstract class IngredientsInRecipeRepository extends AbstractFullEntityRepository<IngredientInRecipe, Long> {


    @Query(named = IngredientInRecipe.MEASURE_BY_INGREDIENT, singleResult = SingleResultType.OPTIONAL)
    public abstract List<String> getMeasuresByIngredient(final String ingredientName);

}
