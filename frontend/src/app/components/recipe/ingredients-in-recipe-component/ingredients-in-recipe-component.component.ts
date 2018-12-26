import {Component, Input, OnInit} from '@angular/core';
import {IngredientInRecipe} from '../../../dataclasses/IngredientInRecipe';
import {Recipe} from "../../../dataclasses/Recipe";

@Component({
  selector: 'app-ingredients-in-recipe-component',
  templateUrl: './ingredients-in-recipe-component.component.html',
  styleUrls: ['./ingredients-in-recipe-component.component.css']
})
export class IngredientsInRecipeComponentComponent implements OnInit{

  @Input() recipe : Recipe;
  @Input() searchedIngredients : string[];

  ingredientsInRecipes: IngredientInRecipe[];

  constructor() { }

  ngOnInit(): void {
    this.ingredientsInRecipes = this.recipe.ingredientInRecipes;
  }

  isSearchedIngredient(ingredientInRecipe : IngredientInRecipe){
    for (const searchedIngredient of this.searchedIngredients) {
      if (ingredientInRecipe.ingredient.name.includes(searchedIngredient)){
        return true;
      }
    }
    return false;
    // console.log(this.fehlendeZutatenNamen.toString());
    // console.log(ingredientInRecipe.ingredient.name);
    // return this.searchedIngredients.includes(ingredientInRecipe.ingredient.name);
  }

}
