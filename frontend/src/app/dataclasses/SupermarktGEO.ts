import {Supermarket} from "./Supermarket";

export class SupermarktGEO {


  constructor(supermarkt, supermarktName , lat, lng, street, housenumber, city, zip) {
    this.supermarkt = supermarkt;
    this.supermarktName = supermarktName;
    this.lat = lat;
    this.lng = lng;
    this.street = street;
    this.housenumber = housenumber;
    this.city = city;
    this.zip = zip;
  }

  supermarkt: Supermarket;
  supermarktName: string;
  lat: number;
  lng: number;
  street: string;
  housenumber: string;
  city: string;
  zip: number;

}
