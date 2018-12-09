package de.berlin.htw.usws.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Getter
@Setter
@Entity
@NamedQueries({
        @NamedQuery(name = IngredientInRecipe.BY_RECIPE_ID,
                query = "select count(ir) from IngredientInRecipe ir inner join Recipe r on ir.recipe=r.id where r.id=?1")
})
public class IngredientInRecipe extends BaseEntity {

    public static final String BY_RECIPE_ID = "ingredientsNumberByRecipeId";

    @ManyToOne
    private Ingredient ingredient;

    @ManyToOne
    @JsonIgnore
    private Recipe recipe;

    @Column
    private Double quantity;

    @Column
    private String measure;

}