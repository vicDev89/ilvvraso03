package de.berlin.htw.usws.webcrawlers.edekaLocations;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;

@Data
public class EdekaResponse {

    @JsonProperty("numFound")
    private int numFound;

    @JsonProperty("docs")
    ArrayList<EdekaMarketGeoLocation> docs = new ArrayList <> ();
}
