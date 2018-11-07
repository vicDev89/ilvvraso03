package de.berlin.htw.usws.webcrawlers;

import org.junit.Ignore;
import testutils.FakerProducer;
import org.junit.Test;

import javax.persistence.EntityManager;

public class DatenmodelTest extends PersistTestBase{

    @Test
    @Ignore
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
