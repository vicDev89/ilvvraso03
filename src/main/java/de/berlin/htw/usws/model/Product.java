package de.berlin.htw.usws.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.berlin.htw.usws.model.enums.Supermarket;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NamedQueries({
        @NamedQuery(name = Product.BY_PRODUCTNAME_AND_SUPERMARKET,
                query = "select p from Product p where p.name=?1 and p.supermarket=?2"),
        @NamedQuery(name = Product.BY_INGREDIENTNAME_AND_SUPERMARKET,
                query = "select count(p) from Product p inner join Ingredient i on p.ingredient= i.id where i.name=?1 and p.supermarket=?2")
})
public class Product extends BaseEntity {

    public static final String BY_PRODUCTNAME_AND_SUPERMARKET = "productByProductnameAndSupermarket";

    public static final String BY_INGREDIENTNAME_AND_SUPERMARKET = "productByIngrredientnameAndSupermarket";

    @Column
    private String name;

    @Column
    @Enumerated(EnumType.STRING)
    private Supermarket supermarket;

    @Column
    private Double price;

    @JsonIgnore
    @ManyToOne
    private Ingredient ingredient;


    public Product(final String name, final Supermarket supermarket, final Double price) {
        this.name = name;
        this.supermarket = supermarket;
        this.price = price;
    }

    // This no-arg-constructor needed for persistence-unit and entity-annotation
    public Product() {
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + getId() +
                ", name='" + name + '\'' +
                ", supermarket=" + supermarket +
                ", price=" + price +
                '}';
    }
}
