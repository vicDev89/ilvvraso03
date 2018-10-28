package de.berlin.htw.usws.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Getter
@Setter
@Entity
public class Ingredient extends BaseEntity{

    @Column
    private String name;

    @ManyToOne
    private Recipe recipe;
}
