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
                query = "select count(r) from Recipe r where r.recipeSite=?1"),
        @NamedQuery(name = Recipe.BY_IDENTIFIER_AND_TITLE,
                query = "select r from Recipe r where r.identifier=?1 and r.title=?2"),
        @NamedQuery(name = Recipe.BY_URL,
                query = "select r from Recipe r where r.url=?1")
})
@NamedEntityGraphs({
        @NamedEntityGraph(name = "withIngredientsAndProducts", attributeNodes = @NamedAttributeNode(value="ingredientInRecipes"))
})
public class Recipe extends BaseEntity {

    public static final String BY_IDENTIFIER = "recipeByIdentifier";

    public static final String BY_TITLE = "recipeByTitle";

    public static final String BY_COUNT = "recipeByCount";

    public static final String BY_IDENTIFIER_AND_TITLE = "recipeByIdentifierAndTitle";

    public static final String BY_URL = "recipeByUrl";

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

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER,
            mappedBy = "recipe",orphanRemoval = true)
    private List<IngredientInRecipe> ingredientInRecipes;

    @Column
    private String identifier;

    @Column
    @Enumerated(EnumType.STRING)
    private RecipeSite recipeSite;

    @Column
    private String url;
}
