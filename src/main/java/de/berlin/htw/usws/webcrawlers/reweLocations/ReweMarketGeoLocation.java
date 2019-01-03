package de.berlin.htw.usws.webcrawlers.reweLocations;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.berlin.htw.usws.model.SupermarketGEO;
import de.berlin.htw.usws.model.enums.Supermarket;
import lombok.Data;

@Data
public class ReweMarketGeoLocation {

    @JsonProperty("id")
    private float id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("address")
    private Address address;

    @JsonProperty("geoLocation")
    private GeoLocation geoLocation;

    public SupermarketGEO convertReweMarketGeoLocationToSupermarketGeo(){
        return new SupermarketGEO(Supermarket.REWE,
                this.name,
                this.geoLocation.getLatitude(),
                this.geoLocation.getLongitude(),
                this.address.getStreet(),
                this.address.getHouseNumber(),
                this.address.getCity(),
                Integer.valueOf(this.address.getPostalCode()) );
    }
}