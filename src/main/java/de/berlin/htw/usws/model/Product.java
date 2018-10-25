package de.berlin.htw.usws.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

@Getter
@Setter
@Entity
@AllArgsConstructor
public class Product extends BaseEntity{


    @Column
    private String name;

    @Column
    private Supermarket supermarket;

    @Column
    private Double priceMin;

    @Column
    private Double priceMax;

}
