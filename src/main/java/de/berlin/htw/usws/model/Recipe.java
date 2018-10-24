package de.berlin.htw.usws.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;


@Getter
@Setter
@Entity
public class Recipe {

    private String title;

    @Column
    private String preparation;

    @OneToMany
    @Column
    private List<Ingredient> ingredients;

    @Column
    private int cookingTime;

    @Column
    private int preparationTime;

    @OneToMany
    @Column
    private List<Opinion> opinions;

    @Column
    private DifficultyLevel difficultyLevel;

    // Saved as URL
    @Column
    private String picture;
}
