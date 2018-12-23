package de.berlin.htw.usws.metamodel;

import de.berlin.htw.usws.model.Protokoll;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Protokoll.class)
public abstract class Protokoll_ extends BaseEntity_ {

	public static volatile SingularAttribute<Protokoll, String> aufrufparameter;
	public static volatile SingularAttribute<Protokoll, Integer> numberGetMeasures;
	public static volatile SingularAttribute<Protokoll, String> erzeuger;
	public static volatile SingularAttribute<Protokoll, Integer> numberGetRecipes;
	public static volatile SingularAttribute<Protokoll, Integer> newIngredientsPersisted;
	public static volatile SingularAttribute<Protokoll, String> ergebnisListeRecipeIds;
	public static volatile SingularAttribute<Protokoll, Integer> numberGetAllIngredients;
	public static volatile SingularAttribute<Protokoll, Integer> newRecipesPersisted;
	public static volatile SingularAttribute<Protokoll, Integer> newProductsPersisted;

}

