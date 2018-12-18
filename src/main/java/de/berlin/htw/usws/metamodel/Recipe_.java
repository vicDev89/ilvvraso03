package de.berlin.htw.usws.metamodel;

import de.berlin.htw.usws.model.IngredientInRecipe;
import de.berlin.htw.usws.model.Recipe;
import de.berlin.htw.usws.model.enums.DifficultyLevel;
import de.berlin.htw.usws.model.enums.RecipeSite;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Recipe.class)
public abstract class Recipe_ extends BaseEntity_ {

	public static volatile SingularAttribute<Recipe, String> identifier;
	public static volatile SingularAttribute<Recipe, Integer> preparationTimeInMin;
	public static volatile SingularAttribute<Recipe, Double> rate;
	public static volatile SingularAttribute<Recipe, DifficultyLevel> difficultyLevel;
	public static volatile SingularAttribute<Recipe, String> pictureUrl;
	public static volatile SingularAttribute<Recipe, RecipeSite> recipeSite;
	public static volatile SingularAttribute<Recipe, String> title;
	public static volatile ListAttribute<Recipe, IngredientInRecipe> ingredientInRecipes;
	public static volatile SingularAttribute<Recipe, String> url;
	public static volatile SingularAttribute<Recipe, String> preparation;

}

