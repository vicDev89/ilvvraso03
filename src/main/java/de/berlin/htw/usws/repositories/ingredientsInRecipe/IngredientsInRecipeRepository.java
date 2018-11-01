package de.berlin.htw.usws.repositories.ingredientsInRecipe;

import de.berlin.htw.usws.model.IngredientsInRecipe;
import de.berlin.htw.usws.model.Recipe;
import org.apache.deltaspike.data.api.AbstractFullEntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.SingleResultType;

public abstract class IngredientsInRecipeRepository extends AbstractFullEntityRepository<IngredientsInRecipe, Long>{


    @Query(named = IngredientsInRecipe.BY_RECIPE_ID, singleResult = SingleResultType.OPTIONAL)
    public abstract int getNumberIngredients(final Recipe recipe);

}
