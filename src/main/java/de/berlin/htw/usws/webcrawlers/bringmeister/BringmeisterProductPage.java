package de.berlin.htw.usws.webcrawlers.bringmeister;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class BringmeisterProductPage {

    @JsonProperty("products")
    private List<BringmeisterProduct> products;
}
