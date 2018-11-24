package de.berlin.htw.usws.webcrawlers.bringmeister;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class BringmeisterProduct {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("name")
    private String name;

    private Double price;

    @JsonProperty("prices")
    private void unpackNameFromNestedObject(Map<String, String> prices) {
        price = Double.parseDouble(prices.get("price"));
    }
}
