package de.berlin.htw.usws.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Getter
@Setter
@Entity
public class IngredientsInRecipe extends BaseEntity{

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
    private double quantity;

    @Column
    private String measure;
}
