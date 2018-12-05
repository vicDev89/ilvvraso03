package de.berlin.htw.usws.metamodel;

import de.berlin.htw.usws.model.Ingredient;
import de.berlin.htw.usws.model.IngredientInRecipe;
import de.berlin.htw.usws.model.Product;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Ingredient.class)
public abstract class Ingredient_ extends BaseEntity_ {

	public static volatile SingularAttribute<Ingredient, String> name;
	public static volatile ListAttribute<Ingredient, IngredientInRecipe> ingredientsInRecipe;
	public static volatile ListAttribute<Ingredient, Product> products;

}

