package de.berlin.htw.usws.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

@Getter
@Setter
@Entity
public class Product extends BaseEntity{

    @Column
    private Supermarket supermarket;

    @Column
    private double priceMin;

    @Column
    private double priceMax;


}
