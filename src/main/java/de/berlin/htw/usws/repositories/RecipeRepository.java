package de.berlin.htw.usws.repositories;

import de.berlin.htw.usws.metamodel.Ingredient_;
import de.berlin.htw.usws.metamodel.IngredientsInRecipe_;
import de.berlin.htw.usws.metamodel.Recipe_;
import de.berlin.htw.usws.model.Ingredient;
import de.berlin.htw.usws.model.IngredientInRecipe;
import de.berlin.htw.usws.model.Recipe;
import de.berlin.htw.usws.model.enums.RecipeSite;
import org.apache.deltaspike.data.api.AbstractFullEntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;
import org.apache.deltaspike.data.api.SingleResultType;
import org.apache.deltaspike.jpa.api.transaction.Transactional;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Recipe-Repository mit Funktionen, die für alle Services aufrufbar sind
 */
@Repository(forEntity = Recipe.class)
@Transactional
public abstract class RecipeRepository extends AbstractFullEntityRepository<Recipe, Long> {

//    @Inject
//    private IngredientsInRecipeRepository ingredientsInRecipeRepository;


    @Query(named = Recipe.BY_IDENTIFIER, singleResult = SingleResultType.OPTIONAL)
    public abstract Recipe findByIdentifier(final String identifier);

    @Query(named = Recipe.BY_TITLE, singleResult = SingleResultType.OPTIONAL)
    public abstract Recipe findByTitle(final String title);

    @Query(named = Recipe.BY_COUNT, singleResult = SingleResultType.OPTIONAL)
    public abstract int countRecipesBySite(final RecipeSite recipeSite);


    /**
     * Find recipes that ONLY contain  given ingredients
     *
     * @param ingredients
     * @return
     */
    public List<Recipe> findRecipesByIngredients(final List<Ingredient> ingredients) {

        // Hole alle Rezepte, die die Zutaten enthalten
        final List<Recipe> recipes = this.findRecipesContainingIngredients(ingredients);

        HashMap<Recipe, Integer> mapNumberIngredientsInRecipe = new HashMap<>();

        // Create Hashmap with recipes and its number of ingredients
        for (Recipe recipe : recipes) {
            //      mapNumberIngredientsInRecipe.put(recipe, ingredientsInRecipeRepository.getNumberIngredients(recipe));
        }

        // Wenn Anzahl Zutaten nicht mit der übergebenen Zutaten-Anzahl übereinstimmt, Recipe weg
        for (Recipe recipe : mapNumberIngredientsInRecipe.keySet()) {
            if (mapNumberIngredientsInRecipe.get(recipe) != ingredients.size()) {
                recipes.remove(recipe);
            }
        }
        return recipes;
    }

    /**
     * Find recipes that contain given ingredients
     *
     * @param ingredients
     * @return
     */
    public List<Recipe> findRecipesContainingIngredients(final List<Ingredient> ingredients) {

        final CriteriaBuilder builder = this.entityManager().getCriteriaBuilder();
        final CriteriaQuery<Recipe> cQuery = builder.createQuery(Recipe.class);
        // Root Recipe da wir Rezepte holen
        final Root<Recipe> rootRecipe = cQuery.from(Recipe.class);
        // Join mit IngredientInRecipe
        final Join<Recipe, IngredientInRecipe> joinIngredientsInRecipe = rootRecipe.join(Recipe_.ingredientsInRecipes);
        // Join mit Ingredients
        final Join<IngredientInRecipe, Ingredient> joinIngredient = joinIngredientsInRecipe.join(IngredientsInRecipe_.ingredient);
        // Liste von predicates vorbereiten
        final List<Predicate> predicates = new ArrayList<>();

        // Add precidate pro Ingredient übergeben
        for (Ingredient ingredient : ingredients) {
            predicates.add(builder.equal(joinIngredient.get(Ingredient_.name), ingredient.getName()));
        }

        // Create query
        cQuery.select(rootRecipe).where(predicates.toArray(new Predicate[]{}));

        return this.entityManager().createQuery(cQuery).getResultList();
    }

}
