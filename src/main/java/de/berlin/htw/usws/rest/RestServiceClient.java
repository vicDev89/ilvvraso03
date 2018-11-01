package de.berlin.htw.usws.rest;

import de.berlin.htw.usws.model.Recipe;
import org.apache.deltaspike.core.api.config.ConfigResolver;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

/**
 * Utility-Klasse für Inhalte aus der DB. Realisiert durch REST Aufrufe (für Test-Zwecke)
 *
 */
public class RestServiceClient {

    public static RestServiceClient create() {
        return new RestServiceClient();
    }

    private RestServiceClient() {
    }

    private Response doRestServiceGetRequest(final String operation) {
        final ResteasyClientBuilder resteasyClientBuilder = new ResteasyClientBuilder();
        final ResteasyClient client = resteasyClientBuilder.build();

        // Config-Resolver anpassen --> URL?
        final ResteasyWebTarget target = client.target(ConfigResolver.resolve("test.url").getValue() + "/api/test/" +
                operation);
        final Response response = target.request().get();
        return this.validateResponse(response);
    }

    private Response doRestServicePostRequest(final String operation, final Object object) {
        final ResteasyClientBuilder resteasyClientBuilder = new ResteasyClientBuilder();
        final ResteasyClient client = resteasyClientBuilder.build();
        final ResteasyWebTarget target = client.target(ConfigResolver.resolve("test.url").getValue() + "/api/test/" +
                operation);
        final Response response = target.request().post(Entity.entity(object, "application/json"));
        return this.validateResponse(response);
    }

    private Response validateResponse(final Response response) {
        switch (response.getStatus()) {
            case 404:
                return null;
            case 500:
                throw new RuntimeException("Internal Server Error");
            default:
                return response;
        }
    }

    /**
     * Get a recipe from DB by ID
     * @param recipeId
     * @return
     */
    public Recipe getRecipe(final long recipeId) {
        final Response response = this.doRestServiceGetRequest("getRecipe/" + recipeId);
        return response != null ? response.readEntity(Recipe.class) : null;
    }

    /**
     * Create a recipe
     * @param nachweis
     * @return
     */
    public Recipe createNachweis(final Recipe nachweis) {
        final Response response = this.doRestServicePostRequest("createRecipe", nachweis);
        return response != null ? response.readEntity(Recipe.class) : null;
    }

}
