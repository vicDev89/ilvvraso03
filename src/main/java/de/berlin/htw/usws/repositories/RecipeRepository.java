package de.berlin.htw.usws.repositories;

import de.berlin.htw.usws.model.*;
import de.berlin.htw.usws.model.enums.RecipeSite;
import org.apache.deltaspike.data.api.*;
import org.apache.deltaspike.jpa.api.transaction.Transactional;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Recipe-Repository mit Funktionen, die für alle Services aufrufbar sind
 */
@Repository(forEntity = Recipe.class)
@Transactional
public abstract class RecipeRepository extends AbstractFullEntityRepository<Recipe, Long> {

    @Query(named = Recipe.BY_IDENTIFIER, singleResult = SingleResultType.OPTIONAL)
    public abstract Recipe findByIdentifier(final String identifier);

    @Query(named = Recipe.BY_TITLE, singleResult = SingleResultType.OPTIONAL)
    public abstract Recipe findByTitle(final String title);

    @Query(named = Recipe.BY_COUNT, singleResult = SingleResultType.OPTIONAL)
    public abstract int countRecipesBySite(final RecipeSite recipeSite);

    @Query(named = Recipe.BY_URL, singleResult = SingleResultType.OPTIONAL)
    public abstract Recipe findByUrl(final String url);

    public List<Recipe> findRecipesContainingIngredientsAll(final List<String> ingredients) {

        final CriteriaBuilder builder = this.entityManager().getCriteriaBuilder();
        final CriteriaQuery<Recipe> cQuery = builder.createQuery(Recipe.class);
        // Root Recipe da wir Rezepte holen
        final Root<Recipe> rootRecipe = cQuery.from(Recipe.class);
        // Liste von predicates vorbereiten
        final List<Predicate> predicates = new ArrayList<>();

        // Add precidate pro Ingredient übergeben
        for (String ingredient : ingredients) {
            // Join mit IngredientInRecipe
            final Join<Recipe, IngredientInRecipe> joinIngredientsInRecipe = rootRecipe.join(Recipe_.ingredientInRecipes);
            // Join mit Ingredients
            final Join<IngredientInRecipe, Ingredient> joinIngredient = joinIngredientsInRecipe.join(IngredientInRecipe_.ingredient);

            predicates.add(builder.like(joinIngredient.get(Ingredient_.name), "%" + ingredient + "%"));
        }

        // Create query
        cQuery.select(rootRecipe).where(predicates.toArray(new Predicate[]{}));

        return this.entityManager().createQuery(cQuery).getResultList();
    }

    /**
     * Find all recipes that contain given ingredients
     *
     * @param ingredients
     * @return
     */
    public List<Recipe> findRecipesContainingIngredientsRest(final List<String> ingredients) {

        final CriteriaBuilder builder = this.entityManager().getCriteriaBuilder();
        final CriteriaQuery<Recipe> cQuery = builder.createQuery(Recipe.class);
        // Root Recipe da wir Rezepte holen
        final Root<Recipe> rootRecipe = cQuery.from(Recipe.class);
        // Liste von predicates vorbereiten
        final List<Predicate> predicates = new ArrayList<>();

        // Add precidate pro Ingredient übergeben
        for (String ingredient : ingredients) {
            // Join mit IngredientInRecipe
            final Join<Recipe, IngredientInRecipe> joinIngredientsInRecipe = rootRecipe.join(Recipe_.ingredientInRecipes);
            // Join mit Ingredients
            final Join<IngredientInRecipe, Ingredient> joinIngredient = joinIngredientsInRecipe.join(IngredientInRecipe_.ingredient);

            predicates.add(builder.like(joinIngredient.get(Ingredient_.name), "%" + ingredient + "%"));
        }

        // Create query
        cQuery.select(rootRecipe).where(predicates.toArray(new Predicate[]{})).distinct(true);

        return this.entityManager().createQuery(cQuery).setFirstResult(10).getResultList();
    }


    /**
     * Find only the 10 first recipes that contain all the ingredients
     * @param ingredients
     * @return
     */
    public List<Recipe> findRecipesContainingIngredientsMax(final List<String> ingredients) {

        final CriteriaBuilder builder = this.entityManager().getCriteriaBuilder();
        final CriteriaQuery<Recipe> cQuery = builder.createQuery(Recipe.class);
        // Root Recipe da wir Rezepte holen
        final Root<Recipe> rootRecipe = cQuery.from(Recipe.class);
        // Liste von predicates vorbereiten
        final List<Predicate> predicates = new ArrayList<>();

        // Add precidate pro Ingredient übergeben
        for (String ingredient : ingredients) {
            // Join mit IngredientInRecipe
            final Join<Recipe, IngredientInRecipe> joinIngredientsInRecipe = rootRecipe.join(Recipe_.ingredientInRecipes);
            // Join mit Ingredients
            final Join<IngredientInRecipe, Ingredient> joinIngredient = joinIngredientsInRecipe.join(IngredientInRecipe_.ingredient);

            predicates.add(builder.like(joinIngredient.get(Ingredient_.name), "%" + ingredient + "%"));
        }

        // Create query
        cQuery.select(rootRecipe).where(predicates.toArray(new Predicate[]{})).distinct(true);

        return this.entityManager().createQuery(cQuery).setMaxResults(10).getResultList();
    }
}
