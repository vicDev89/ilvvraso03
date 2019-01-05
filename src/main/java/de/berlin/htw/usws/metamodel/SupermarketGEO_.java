package de.berlin.htw.usws.metamodel;

import de.berlin.htw.usws.model.SupermarketGEO;
import de.berlin.htw.usws.model.enums.Supermarket;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(SupermarketGEO.class)
public abstract class SupermarketGEO_ extends BaseEntity_ {

	public static volatile SingularAttribute<SupermarketGEO, Supermarket> supermarket;
	public static volatile SingularAttribute<SupermarketGEO, String> zip;
	public static volatile SingularAttribute<SupermarketGEO, String> supermarketName;
	public static volatile SingularAttribute<SupermarketGEO, Float> lng;
	public static volatile SingularAttribute<SupermarketGEO, String> housenumber;
	public static volatile SingularAttribute<SupermarketGEO, String> city;
	public static volatile SingularAttribute<SupermarketGEO, String> street;
	public static volatile SingularAttribute<SupermarketGEO, String> phonenumber;
	public static volatile SingularAttribute<SupermarketGEO, Float> let;
	public static volatile SingularAttribute<SupermarketGEO, String> marketID;

}

