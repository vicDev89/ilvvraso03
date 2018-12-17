import {Product} from './Product';

export class Ingredient {

  constructor(id, name) {
    this.id = id;
    this.name = name;
  }

  id: number;
  name: string;
  products: Product[];
}
