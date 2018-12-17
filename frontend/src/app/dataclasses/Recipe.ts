import {DifficultyLevel} from './DifficultyLevel';
import {IngredientInRecipe} from './IngredientInRecipe';
import {RecipeSite} from './RecipeSite';

export class Recipe {
  id: number;
  title: string;
  preparation: string;
  preparationTimeInMin: number;
  rate: number;
  difficultyLevel: DifficultyLevel;
  pictureUrl: string;
  ingredientInRecipes: IngredientInRecipe[];
  identifier: string;
  recipeSite: RecipeSite;
}
