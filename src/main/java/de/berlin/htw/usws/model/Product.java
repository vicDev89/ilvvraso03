package de.berlin.htw.usws.model;

import de.berlin.htw.usws.model.enums.Supermarket;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NamedQueries({
        @NamedQuery(name = Product.BY_NAME_AND_SUPERMARKET,
                query = "select p from Product p where p.name=?1 and p.supermarket=?2")
})
public class Product extends BaseEntity {

    public static final String BY_NAME_AND_SUPERMARKET = "productByNameAndSupermarket";

    @Column
    private String name;

    @Column
    @Enumerated(EnumType.STRING)
    private Supermarket supermarket;

    @Column
    private Double priceMin;

    @Column
    private Double priceMax;

    @ManyToOne
    private Ingredient ingredient;


    public Product(final String name, final Supermarket supermarket, final Double priceMin, final Double priceMax) {
        this.name = name;
        this.supermarket = supermarket;
        this.priceMin = priceMin;
        this.priceMax = priceMax;
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
                ", priceMin=" + priceMin +
                ", priceMax=" + priceMax +
                '}';
    }
}
