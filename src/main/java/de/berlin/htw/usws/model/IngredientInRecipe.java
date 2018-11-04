package de.berlin.htw.usws.model;

import lombok.AccessLevel;
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
public class IngredientInRecipe extends BaseEntity{

    public static final String BY_RECIPE_ID = "ingredientsNumberByRecipeId";

    @Setter(value = AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column( updatable = false, nullable = false)
    private Long id;

    @OneToOne
    private Ingredient ingredient;

    @OneToOne
    private Recipe recipe;

    @Column
    private Double quantity;

    @Column
    private String measure;

}