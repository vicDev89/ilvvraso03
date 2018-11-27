package de.berlin.htw.usws.webcrawlers.hellofresh;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;


@Data
public class HelloFreshRecipesResponse {

    @JsonProperty("items")
    private List<HelloFreshRecipe> items;

}
