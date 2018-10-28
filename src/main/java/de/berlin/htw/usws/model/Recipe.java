package de.berlin.htw.usws.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.List;


@Getter
@Setter
@Entity
public class Recipe extends BaseEntity {

    // TODO: (Vic) ID von BaseEntity mit Recipe-ID überschreiben

    private String title;

    @Column
    private String preparation;

    @OneToMany
    @JoinColumn
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
