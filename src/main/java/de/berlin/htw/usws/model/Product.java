package de.berlin.htw.usws.model;

import lombok.AccessLevel;
import lombok.Getter;

import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NamedQueries({
        @NamedQuery(name = Product.BY_ID,
                query = "select p from Product p where p.id=?1")
})
public class Product extends BaseEntity{

    public static final String BY_ID = "productById";

    @Setter(value = AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_generator")
    @SequenceGenerator(name="product_generator", sequenceName = "product_seq", allocationSize=50)
    @Column
    private Long id;

    @Column
    private String name;

    @Column
    @Enumerated(EnumType.STRING)
    private Supermarket supermarket;

    @Column
    private Double priceMin;

    @Column
    private Double priceMax;


    public Product(final String name, final Supermarket supermarket, final Double priceMin, final Double priceMax) {
        this.name = name;
        this.supermarket = supermarket;
        this.priceMin = priceMin;
        this.priceMax = priceMax;
    }

    // This no-arg-constructor needed for persistence-unit and entity-annotation
    public Product() {
    }
}
