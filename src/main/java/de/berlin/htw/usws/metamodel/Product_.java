package de.berlin.htw.usws.metamodel;

import de.berlin.htw.usws.model.Product;
import de.berlin.htw.usws.model.Supermarket;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Product.class)
public abstract class Product_ extends BaseEntity_ {

	public static volatile SingularAttribute<Product, Supermarket> supermarket;
	public static volatile SingularAttribute<Product, Double> priceMin;
	public static volatile SingularAttribute<Product, Double> priceMax;
	public static volatile SingularAttribute<Product, String> name;
	public static volatile SingularAttribute<Product, Long> id;

}

