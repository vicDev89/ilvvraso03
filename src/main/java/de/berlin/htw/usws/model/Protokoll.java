package de.berlin.htw.usws.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Getter
@Setter
public class Protokoll extends BaseEntity{

    // Ob die Scheduler oder ob über die API und mit welcher Methode
    @Column
    private String erzeuger;

    //  für Recipe- und Product-Scheduler
    @Column
    private int newProductsPersisted;

    // nur für Recipe-Scheduler
    @Column
    private int newIngredientsPersisted;

    // nur für Recipe-Scheduler
    @Column
    private int newRecipesPersisted;

    // die übergebene Parameter bei den API-Aufrufe
    @Column(columnDefinition = "TEXT")
    private String aufrufparameter;

    // nur für die getRecipe-API-Aufrufe
    @Column(columnDefinition = "TEXT")
    private String ergebnisListeRecipeIds;

    // für alle GET-API-Aufrufe
    @Column
    private int numberGetElements;

    // nur für SupermarketGEO-Scheduler
    @Column
    private int updateSupermarketGEO;

    // nur für SupermarketGEO-Scheduler
    @Column
    private int newSupermarketGEO;

}
