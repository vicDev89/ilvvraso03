package de.berlin.htw.usws.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Getter
@Setter
@Entity
@NamedQueries({
        @NamedQuery(name = IngredientInRecipe.MEASURE_BY_INGREDIENT,
                query = "select distinct(ir.measure) from IngredientInRecipe ir INNER JOIN Ingredient i ON ir.ingredient=i.id WHERE i.name LIKE CONCAT('%',?1, '%')")
})
public class IngredientInRecipe extends BaseEntity {

    public static final String MEASURE_BY_INGREDIENT = "measuresByIngredient";

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