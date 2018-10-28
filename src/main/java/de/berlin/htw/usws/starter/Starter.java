package de.berlin.htw.usws.starter;

import de.berlin.htw.usws.model.Product;
import de.berlin.htw.usws.model.Supermarket;
import de.berlin.htw.usws.webCrawlers.RecipeCrawlerChefkoch;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

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

        //  new CrawlerService().start();
        new RecipeCrawlerChefkoch().scrapRecipe(3593891540449959L);

		EntityManager entityManager = getEntityManager();
		entityManager.getTransaction().begin();

//        ProductService productService = new ProductService();
        Product testProduct = new Product("Apfel", Supermarket.EDEKA, 1.50, 2.50);
//        productService.persistProduct(testProduct);

        entityManager.persist(testProduct);

		entityManager.getTransaction().commit();

		entityManager.clear();


    }
}
