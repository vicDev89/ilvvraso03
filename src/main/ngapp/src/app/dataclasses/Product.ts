import {Supermarket} from './Supermarket';

export class Product {


  constructor(name, supermarket, price) {
    this.name = name;
    this.supermarket = supermarket;
    this.price = price;
  }

  name: string;
  supermarket: Supermarket;
  price: number;

}
