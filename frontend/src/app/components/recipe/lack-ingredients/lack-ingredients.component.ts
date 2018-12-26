import {Component, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {IngredientInRecipe} from '../../../dataclasses/IngredientInRecipe';
import {Supermarket} from "../../../dataclasses/Supermarket";
import {Ingredient} from "../../../dataclasses/Ingredient";

@Component({
  selector: 'app-lack-ingredients',
  templateUrl: './lack-ingredients.component.html',
  styleUrls: ['./lack-ingredients.component.css']
})
export class LackIngredientsComponent implements OnChanges {

  @Input() fehlendeZustaten: IngredientInRecipe[];
  @Input() superkarket: Supermarket;

  constructor() { };

  ngOnChanges(changes: SimpleChanges): void {
    console.log("Supermarket lack ingredients:" + this.superkarket);
  }

  getSupermarketValue(index: number){
    return Supermarket[index];
  }


  isProductExistsInSupermarket(ingredient: Ingredient, supermarket: Supermarket) {
    let supermarketValue = this.getSupermarketValue(supermarket);
    for (const product of ingredient.products) {
      if(product.supermarket.toString() == supermarketValue){
        return true;
      }
    }
    return false;
  }
}
