package de.berlin.htw.usws.model;

import de.berlin.htw.usws.model.enums.DifficultyLevel;
import de.berlin.htw.usws.model.enums.RecipeSite;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;


@Getter
@Setter
@Entity
@NamedQueries({
        @NamedQuery(name = Recipe.BY_IDENTIFIER,
                query = "select r from Recipe r where r.identifier=?1"),
        @NamedQuery(name = Recipe.BY_TITLE,
                query = "select r from Recipe r where r.title=?1"),
        @NamedQuery(name = Recipe.BY_COUNT,
                query = "select count(r) from Recipe r where r.recipeSite=?1")
})
public class Recipe extends BaseEntity {

    public static final String BY_IDENTIFIER = "recipeByIdentifier";

    public static final String BY_TITLE = "recipeByTitle";

    public static final String BY_COUNT = "recipeByCount";

    @Column
    private String title;

    @Column(columnDefinition = "TEXT")
    private String preparation;

    @Column
    private int preparationTimeInMin;

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

    @Column
    private String identifier;

    @Column
    @Enumerated(EnumType.STRING)
    private RecipeSite recipeSite;
}
