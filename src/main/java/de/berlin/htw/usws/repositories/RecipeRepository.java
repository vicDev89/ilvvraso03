package de.berlin.htw.usws.repositories;

import de.berlin.htw.usws.model.Recipe;
import org.apache.deltaspike.data.api.*;

/**
 * Recipe-Repository mit Funktionen, die für alle Services aufrufbar sind
 */
@Repository(forEntity = Recipe.class)
public abstract class RecipeRepository extends AbstractFullEntityRepository<Recipe, Long> {

    /**
     * Search for a recipe by ID
     * @param id
     * @return
     */
    @Query(named = Recipe.BY_ID, singleResult = SingleResultType.OPTIONAL)
    //@EntityGraph(paths = {"ingredients"})
    public abstract Recipe findBy(final Long id);

    /**
     * Remove a recipe
     * @param id
     */
    public void deleteById(final Long id) {
        this.remove(this.findBy(id));
    }
}
