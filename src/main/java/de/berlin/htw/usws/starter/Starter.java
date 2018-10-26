package de.berlin.htw.usws.starter;

import de.berlin.htw.usws.model.Product;
import de.berlin.htw.usws.model.Supermarket;
import de.berlin.htw.usws.webCrawlers.EdekaCrawler;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class Starter {

    private static final EntityManagerFactory entityManagerFactory;
    private static final String PERSISTENCE_UNIT_NAME = "ingrEatDB_unit";

    static {

        entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

    }

    public static EntityManager getEntityManager() {

        return entityManagerFactory.createEntityManager();

    }

    public static void main(String[] args) {


        List<Product> edekaProducts = EdekaCrawler.getAllProducts();

		EntityManager entityManager = getEntityManager();
		entityManager.getTransaction().begin();

        Product testProduct = new Product("Apfel", Supermarket.EDEKA, 1.50, 2.50);
        edekaProducts.add(testProduct);
        for(Product product : edekaProducts) {
            entityManager.persist(product);
        }

		entityManager.getTransaction().commit();

		entityManager.clear();


    }
}
