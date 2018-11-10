package de.berlin.htw.usws.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;


@Getter
@Setter
@Entity
@NamedQueries({
        @NamedQuery(name = Recipe.BY_ID,
                query = "select r from Recipe r where r.id=?1")
})
public class Recipe extends BaseEntity {

    public static final String BY_ID = "recipeById";

    @Id
    @Column( updatable = false, nullable = false)
    private Long id;

    @Column
    private String title;

    @Column(columnDefinition = "TEXT")
    private String preparation;

    @Column
    private int cookingTimeInMin;

    @Column
    private int preparationTimeInMin;

    @Column
    private int restingTimeInMin;

    @Column
    private Double rate;

    @Column
    @Enumerated(EnumType.STRING)
    private DifficultyLevel difficultyLevel;

    @Column
    private String pictureUrl;

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "recipe", orphanRemoval = true)
    private List<IngredientInRecipe> ingredientInRecipes;
}
