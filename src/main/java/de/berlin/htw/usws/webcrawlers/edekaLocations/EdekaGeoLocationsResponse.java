package de.berlin.htw.usws.webcrawlers.edekaLocations;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EdekaGeoLocationsResponse {

    @JsonProperty("response")
    EdekaResponse Response;

}
