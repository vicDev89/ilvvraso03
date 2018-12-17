import {Recipe} from "./Recipe";
import {Ingredient} from "./Ingredient";

export class IngredientInRecipe {
  id: number;
  ingredient: Ingredient;
  recipe: Recipe;
  quantity: number;
  measure: string;
}
