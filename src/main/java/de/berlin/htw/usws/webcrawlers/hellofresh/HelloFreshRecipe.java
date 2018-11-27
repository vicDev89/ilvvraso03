package de.berlin.htw.usws.webcrawlers.hellofresh;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class HelloFreshRecipe {

    @JsonProperty("websiteUrl")
    private String websiteUrl;

}
