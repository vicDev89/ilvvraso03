package de.berlin.htw.usws.model;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Ingredient extends BaseEntity{

    @Setter(value = AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ingredient_generator")
    @SequenceGenerator(name="ingredient_generator", sequenceName = "ingredient_seq", allocationSize=50)
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column
    private String name;

}
