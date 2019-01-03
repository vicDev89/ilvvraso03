package de.berlin.htw.usws.webcrawlers.reweLocations;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;

@Data
public class ReweGeoLocationsResponse {

    @JsonProperty("markets")
    ArrayList<ReweMarketGeoLocation> markets = new ArrayList<>();

    @JsonProperty("total")
    private int total;

}
