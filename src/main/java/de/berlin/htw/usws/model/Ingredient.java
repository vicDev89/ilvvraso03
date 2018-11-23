package de.berlin.htw.usws.model;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(uniqueConstraints=@UniqueConstraint(columnNames="name"))
@NamedQueries({
        @NamedQuery(name = Ingredient.BY_NAME,
                query = "select i from Ingredient i where i.name=?1")
})
public class Ingredient extends BaseEntity{


    public static final String BY_NAME = "ingredientByName";

    @Setter(value = AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ingredient_generator")
    @SequenceGenerator(name="ingredient_generator", sequenceName = "ingredient_seq", allocationSize=50)
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column
    private String name;

    @OneToMany(cascade = CascadeType.PERSIST,
            mappedBy = "ingredient", orphanRemoval = true)
    private List<IngredientInRecipe> ingredientsInRecipe;

    @OneToMany(cascade = CascadeType.PERSIST,
            mappedBy = "ingredient", orphanRemoval = true)
    private List<Product> products;

    public Ingredient(String name) {
        this.name = name;
    }

}
