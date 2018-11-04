package de.berlin.htw.usws.webcrawlers;

import testutils.FakerProducer;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DatenmodelTest {

    private static final EntityManagerFactory entityManagerFactory;
    private static final String PERSISTENCE_UNIT_NAME = "ingrEatDBPg";


    static {

        entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

    }

    public static EntityManager getEntityManager() {

        return entityManagerFactory.createEntityManager();

    }

    @Test
    public void checkPerstenceUnit() {

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
