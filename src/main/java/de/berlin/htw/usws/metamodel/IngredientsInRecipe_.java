package de.berlin.htw.usws.metamodel;

import de.berlin.htw.usws.model.Ingredient;
import de.berlin.htw.usws.model.IngredientInRecipe;
import de.berlin.htw.usws.model.Recipe;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(IngredientInRecipe.class)
public abstract class IngredientsInRecipe_ extends BaseEntity_ {

	public static volatile SingularAttribute<IngredientInRecipe, Ingredient> ingredient;
	public static volatile SingularAttribute<IngredientInRecipe, Double> quantity;
	public static volatile SingularAttribute<IngredientInRecipe, String> measure;
	public static volatile SingularAttribute<IngredientInRecipe, Recipe> recipe;
	public static volatile SingularAttribute<IngredientInRecipe, Long> id;

}

