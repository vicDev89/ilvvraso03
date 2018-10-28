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

    @Column
    private String preparation;

    @OneToMany
    private List<Ingredient> ingredients;

    // in minutes
    @Column
    private int cookingTimeInMin;

    // in minutes
    @Column
    private int preparationTimeInMin;

    @Column
    private Double rate;

    @Column
    private DifficultyLevel difficultyLevel;

    // Saved as URL
    @Column
    private String picture;
}
