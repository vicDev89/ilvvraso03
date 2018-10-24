package de.berlin.htw.usws.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Getter
@Setter
@Entity
public class IngredientsInRecipe {

    private Ingredient ingredient;

    private Recipe recipe;

    private double quantity;

    private String measure;
}
