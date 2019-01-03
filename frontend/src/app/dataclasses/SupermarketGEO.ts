import {Supermarket} from "./Supermarket";

export class SupermarketGEO {


  constructor(supermarkt, supermarktName , lat, lng, street, housenumber, city, zip) {
    this.supermarket = supermarkt;
    this.supermarketName = supermarktName;
    this.lat = lat;
    this.lng = lng;
    this.street = street;
    this.housenumber = housenumber;
    this.city = city;
    this.zip = zip;
  }

  supermarket: Supermarket;
  supermarketName: string;
  lat: number;
  lng: number;
  street: string;
  housenumber: string;
  city: string;
  zip: number;

}
