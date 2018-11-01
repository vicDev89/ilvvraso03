package testutils;

import javax.enterprise.context.ApplicationScoped;

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


public class EntityManagerDbProducer {

    @PersistenceContext(name = "ingrEatDB_unit")
    private EntityManager entityManager;

    @Produces
    @ApplicationScoped
    private EntityManager provide() {
        return this.entityManager;
    }

}
