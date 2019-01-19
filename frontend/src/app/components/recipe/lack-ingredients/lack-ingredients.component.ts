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

  @Input() missingIngredients: IngredientInRecipe[];
  @Input() superkarket: Supermarket;

  constructor() { };

  ngOnChanges(changes: SimpleChanges): void {
  }

  getSupermarketValue(index: number){
    return Supermarket[index];
  }


  isProductExistsInSupermarket(ingredient: Ingredient, supermarket: Supermarket) {
    for (const product of ingredient.products) {
      if(product.supermarket == supermarket){
        return true;
      }
    }
    return false;
  }
}
