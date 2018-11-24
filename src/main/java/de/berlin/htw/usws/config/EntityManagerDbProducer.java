package de.berlin.htw.usws.config;


import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@ApplicationScoped
public class EntityManagerDbProducer {

    @PersistenceContext(unitName = "ingrEatDBPg")
    private EntityManager entityManager;

    @Produces
    @Default
    @RequestScoped
    private EntityManager provide() {
        return this.entityManager;
    }

}
