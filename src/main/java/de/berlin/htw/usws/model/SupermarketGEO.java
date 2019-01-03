package de.berlin.htw.usws.model;

import de.berlin.htw.usws.model.enums.Supermarket;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SupermarketGEO {

    private Supermarket supermarket;
    private  String supermarketName;
    private float let;
    private float lng;
    private String street;
    private String housenumber;
    private String city;
    private int zip;

}
