package de.berlin.htw.usws.services;

import de.berlin.htw.usws.model.Recipe;
import de.berlin.htw.usws.repositories.recipe.RecipeRepository;
import org.apache.deltaspike.core.api.provider.BeanProvider;

import javax.inject.Inject;

public class RecipeService {

//    @Inject
//    private RecipeRepository recipeRepository;

    public Recipe getRecipe() {
        Recipe recipe = BeanProvider.getContextualReference(RecipeRepository.class).findBy(1L);
//        Recipe recipe = this.recipeRepository.findBy(1L);
        if(recipe == null) {
            recipe = new Recipe();
        }
        return recipe;
    }
}
