package de.berlin.htw.usws.repositories;

import de.berlin.htw.usws.model.IngredientInRecipe;
import de.berlin.htw.usws.model.Recipe;
import org.apache.deltaspike.data.api.AbstractFullEntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;
import org.apache.deltaspike.data.api.SingleResultType;

@Repository(forEntity = IngredientInRecipe.class)
public abstract class IngredientsInRecipeRepository extends AbstractFullEntityRepository<IngredientInRecipe, Long> {


    @Query(named = IngredientInRecipe.BY_RECIPE_ID, singleResult = SingleResultType.OPTIONAL)
    public abstract int getNumberIngredients(final Recipe recipe);

}
