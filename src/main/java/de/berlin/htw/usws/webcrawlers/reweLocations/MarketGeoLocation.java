package de.berlin.htw.usws.webcrawlers.reweLocations;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MarketGeoLocation {

    @JsonProperty("id")
    private float id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("address")
    private Address Address;

    @JsonProperty("geoLocation")
    private GeoLocation GeoLocation;
}
