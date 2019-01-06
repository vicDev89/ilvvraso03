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

    public SupermarketGEO convertReweMarketGeoLocationToSupermarketGeo() {
        return new SupermarketGEO(
                String.valueOf(this.id),
                Supermarket.REWE,
                this.name,
                (double)this.geoLocation.getLatitude(),
                (double)this.geoLocation.getLongitude(),
                this.address.getStreet(),
                this.address.getHouseNumber(),
                this.address.getCity(),
                this.address.getPostalCode(),
                this.phone);
    }
}
