package de.berlin.htw.usws.metamodel;

import de.berlin.htw.usws.model.Ingredient;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Ingredient.class)
public abstract class Ingredient_ extends BaseEntity_ {

	public static volatile SingularAttribute<Ingredient, String> name;
	public static volatile SingularAttribute<Ingredient, Long> id;

}

