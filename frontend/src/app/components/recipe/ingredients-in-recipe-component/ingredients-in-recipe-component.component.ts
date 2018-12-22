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
  ingredientsInRecipes: IngredientInRecipe[];
  fehlendeZutatenNamen : string[] = [];

  constructor() { }

  ngOnInit(): void {
    this.ingredientsInRecipes = this.recipe.ingredientInRecipes;
    this.recipe.fehlendeZutaten.forEach((zutat) => this.fehlendeZutatenNamen.push(zutat.ingredient.name));
  }

  isNotLackIngredient(ingredientInRecipe : IngredientInRecipe){
    console.log(this.fehlendeZutatenNamen.toString());
    console.log(ingredientInRecipe.ingredient.name);
    return !this.fehlendeZutatenNamen.toString().includes(ingredientInRecipe.ingredient.name);
    // return !this.fehlendeZutatenNamen.includes(ingredientInRecipe.ingredient.name);
  }

}
