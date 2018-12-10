package de.berlin.htw.usws.model;

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
                query = "select i from Ingredient i where i.name=?1"),
        @NamedQuery(name = Ingredient.BY_ALL,
                query = "select i from Ingredient i")
})
@NamedEntityGraphs({
        @NamedEntityGraph(name = "withProducts", attributeNodes = @NamedAttributeNode("products"))
})
public class Ingredient extends BaseEntity {

    public static final String BY_NAME = "ingredientByName";

    public static final String BY_ALL = "allIngredients";

    @Column
    private String name;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "ingredient", orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Product> products;

    public Ingredient(String name) {
        this.name = name;
    }

}
