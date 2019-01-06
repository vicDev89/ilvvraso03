package de.berlin.htw.usws.webcrawlers.edekaLocations;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.berlin.htw.usws.model.SupermarketGEO;
import de.berlin.htw.usws.model.enums.Supermarket;
import lombok.Data;

@Data
public class EdekaMarketGeoLocation {

    @JsonProperty("geoLng_doubleField_d")
    private float geoLng_doubleField_d;

    @JsonProperty("urlExtern_tlc")
    private String urlExtern_tlc;

    @JsonProperty("telefon_tlc")
    private String telefon_tlc;

    @JsonProperty("geoLat_doubleField_d")
    private float geoLat_doubleField_d;

    @JsonProperty("marktID_tlc")
    private String marktID_tlc;

    @JsonProperty("strasse_tlc")
    private String strasse_tlc;

    @JsonProperty("ort_tlc")
    private String ort_tlc;

    @JsonProperty("name_tlc")
    private String name_tlc;

    @JsonProperty("plz_tlc")
    private String plz_tlc;

    public SupermarketGEO convertEdekaMarketGeoLocationToSupermarketGeo(){
        return new SupermarketGEO(
                this.marktID_tlc,
                Supermarket.EDEKA,
                this.name_tlc,
                (double)this.geoLat_doubleField_d,
                (double)this.geoLng_doubleField_d,
                getStreet(this.strasse_tlc),
                getHousenumber(this.strasse_tlc),
                this.ort_tlc,
                this.plz_tlc,
                this.telefon_tlc);
    }

    private String getStreet(String streetAndHousenumber) {
        String[] arr = streetAndHousenumber.split("\\d+", 2);
        return arr[0].trim();
    }

    private String getHousenumber(String streetAndHousenumber) {
        String[] arr = streetAndHousenumber.split("\\d+", 2);
        if(arr.length > 1) {
            String pt1 = arr[0].trim();
            return streetAndHousenumber.substring(pt1.length() + 1).trim();
        } else {
            return null;
        }
    }
}
