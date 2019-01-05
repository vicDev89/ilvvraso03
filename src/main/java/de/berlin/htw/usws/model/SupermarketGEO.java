package de.berlin.htw.usws.model;

import de.berlin.htw.usws.model.enums.Supermarket;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@Entity
@NoArgsConstructor
@NamedQueries({
        @NamedQuery(name = SupermarketGEO.BY_MARKTID_SUPERMARKET,
                query = "select s from SupermarketGEO s where s.marketID=?1 and s.supermarket=?2")
})
public class SupermarketGEO extends BaseEntity {


    public static final String BY_MARKTID_SUPERMARKET = "supermarketGEOByIDAndSupermarket";

    @Column
    private String marketID;

    @Column
    @Enumerated(EnumType.STRING)
    private Supermarket supermarket;

    @Column
    private String supermarketName;

    @Column
    private float let;

    @Column
    private float lng;

    @Column
    private String street;

    @Column
    private String housenumber;

    @Column
    private String city;

    @Column
    private String zip;

    @Column
    private String phonenumber;

    public boolean sameSupermarketGEO(SupermarketGEO supermarketGEO){
        return this.supermarketName.equals(supermarketGEO.getSupermarketName()) &&
                this.let == supermarketGEO.getLet() &&
                this.lng == supermarketGEO.getLng() &&
                this.street.equals(supermarketGEO.getStreet()) &&
                this.housenumber.equals(supermarketGEO.getHousenumber()) &&
                this.city.equals(supermarketGEO.getCity()) &&
                this.zip.equals(supermarketGEO.getZip()) &&
                this.phonenumber.equals(supermarketGEO.getPhonenumber());
    }

}
