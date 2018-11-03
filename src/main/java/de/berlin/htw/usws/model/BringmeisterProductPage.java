package de.berlin.htw.usws.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class BringmeisterProductPage {

    @JsonProperty("products")
    private List<BringmeisterProduct> products;
}
