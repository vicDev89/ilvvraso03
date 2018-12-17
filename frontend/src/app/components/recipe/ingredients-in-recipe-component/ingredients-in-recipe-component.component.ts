import {Component, Input} from '@angular/core';
import {IngredientInRecipe} from '../../../dataclasses/IngredientInRecipe';

@Component({
  selector: 'app-ingredients-in-recipe-component',
  templateUrl: './ingredients-in-recipe-component.component.html',
  styleUrls: ['./ingredients-in-recipe-component.component.css']
})
export class IngredientsInRecipeComponentComponent {

  @Input() ingredientsInRecipes: IngredientInRecipe[];

  constructor() { }

}
