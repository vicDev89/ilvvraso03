package de.berlin.htw.usws.metamodel;

import de.berlin.htw.usws.model.Ingredient;
import de.berlin.htw.usws.model.IngredientsInRecipe;
import de.berlin.htw.usws.model.Recipe;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(IngredientsInRecipe.class)
public abstract class IngredientsInRecipe_ extends BaseEntity_ {

	public static volatile SingularAttribute<IngredientsInRecipe, Ingredient> ingredient;
	public static volatile SingularAttribute<IngredientsInRecipe, Double> quantity;
	public static volatile SingularAttribute<IngredientsInRecipe, String> measure;
	public static volatile SingularAttribute<IngredientsInRecipe, Recipe> recipe;
	public static volatile SingularAttribute<IngredientsInRecipe, Long> id;

}

