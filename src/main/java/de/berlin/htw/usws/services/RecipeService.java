package de.berlin.htw.usws.services;

import de.berlin.htw.usws.model.Recipe;
import de.berlin.htw.usws.repositories.recipe.RecipeRepository;
import org.apache.deltaspike.core.api.provider.BeanProvider;

import javax.ejb.Stateless;
import javax.inject.Inject;

import java.io.Serializable;

@Stateless
public class RecipeService implements Serializable {

//    @Inject
//    private RecipeRepository recipeRepository;

    public Recipe getRecipe() {

        Recipe recipe = BeanProvider.getDependent(RecipeRepository.class).get().findBy(1L);
  //      Recipe recipe = this.recipeRepository.findBy(1L);
        if(recipe == null) {
            recipe = new Recipe();
        }
        return recipe;
    }
}
