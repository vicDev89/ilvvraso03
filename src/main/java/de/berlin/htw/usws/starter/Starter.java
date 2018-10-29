package de.berlin.htw.usws.starter;

import de.berlin.htw.usws.model.Product;
import de.berlin.htw.usws.model.Supermarket;
import de.berlin.htw.usws.util.FakerProducer;
import de.berlin.htw.usws.webCrawlers.RecipeCrawlerChefkoch;
import lombok.extern.slf4j.Slf4j;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@Slf4j
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

		EntityManager entityManager = getEntityManager();
		entityManager.getTransaction().begin();

        for(int i = 0; i<10; i++) {
            entityManager.persist(FakerProducer.createFakeRecipe());
            entityManager.persist(FakerProducer.createFakeProduct());
            entityManager.persist(FakerProducer.createFakeIngredient());
        }
		entityManager.getTransaction().commit();

		entityManager.clear();


    }
}
