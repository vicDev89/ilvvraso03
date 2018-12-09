package de.berlin.htw.usws.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "name"))
@NamedQueries({
        @NamedQuery(name = Ingredient.BY_NAME,
                query = "select i from Ingredient i where i.name=?1")
})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Ingredient extends BaseEntity {


    public static final String BY_NAME = "ingredientByName";

    @Column
    private String name;

//    @JsonIgnore
//    @OneToMany(cascade = CascadeType.PERSIST,
//            mappedBy = "ingredient", orphanRemoval = true)
//    private List<IngredientInRecipe> ingredientsInRecipe;

    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER,
            mappedBy = "ingredient", orphanRemoval = true)
    private List<Product> products;

    public Ingredient(String name) {
        this.name = name;
    }

}
