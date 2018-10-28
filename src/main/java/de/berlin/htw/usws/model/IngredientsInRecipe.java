package de.berlin.htw.usws.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;


@Getter
@Setter
@Entity
public class IngredientsInRecipe extends BaseEntity{

    @JoinColumn
    private Ingredient ingredient;

    @JoinColumn
    private Recipe recipe;

    @Column
    private double quantity;

    @Column
    private String measure;
}
