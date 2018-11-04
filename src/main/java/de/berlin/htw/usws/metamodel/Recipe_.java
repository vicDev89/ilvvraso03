package de.berlin.htw.usws.metamodel;

import de.berlin.htw.usws.model.DifficultyLevel;
import de.berlin.htw.usws.model.IngredientInRecipe;
import de.berlin.htw.usws.model.Recipe;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Recipe.class)
public abstract class Recipe_ extends BaseEntity_ {

	public static volatile ListAttribute<Recipe, IngredientInRecipe> ingredientsInRecipes;
	public static volatile SingularAttribute<Recipe, Integer> preparationTimeInMin;
	public static volatile SingularAttribute<Recipe, Double> rate;
	public static volatile SingularAttribute<Recipe, Integer> cookingTimeInMin;
	public static volatile SingularAttribute<Recipe, DifficultyLevel> difficultyLevel;
	public static volatile SingularAttribute<Recipe, String> pictureUrl;
	public static volatile SingularAttribute<Recipe, Long> id;
	public static volatile SingularAttribute<Recipe, String> title;
	public static volatile SingularAttribute<Recipe, Integer> restingTimeInMin;
	public static volatile SingularAttribute<Recipe, String> preparation;

}

