import {Component, Input, OnInit} from '@angular/core';
import {Recipe} from '../../../dataclasses/Recipe';
import {MatDialog} from '@angular/material';
import {RecipePopupComponent} from '../recipe-popup/recipe-popup.component';
import {IngredientInRecipe} from '../../../dataclasses/IngredientInRecipe';
import {SupermarketGEO} from "../../../dataclasses/SupermarketGEO";


@Component({
  selector: 'app-recipe-component',
  templateUrl: './recipe-component.component.html',
  styleUrls: ['./recipe-component.component.css', '../../../shared_styles/rating.css']
})
export class RecipeComponentComponent implements OnInit {

  @Input() recipe: Recipe;
  @Input() supermarketGeoLocations: SupermarketGEO[] = [];
  @Input() searchedIngredients: string[] = [];

  missingIngredients: IngredientInRecipe[] = [];

  constructor(public dialog: MatDialog) { }

  ngOnInit() {
    this.missingIngredients = this.recipe.fehlendeZutaten;
  }

  openDialog() {
    const dialogRef = this.dialog.open(RecipePopupComponent, {
      height: '600px',
      width: '800px',
      data: {recipe: this.recipe,
        fehlendeZustaten: this.missingIngredients,
        supermarketGeoLocations: this.supermarketGeoLocations,
        searchedIngredients: this.searchedIngredients}
    });
  }

  getNamesOf(missingIngredients: IngredientInRecipe[]) {
    let names = [];
    for (let i = 0; i < missingIngredients.length; i++) {
      let ingredientName = this.missingIngredients[i].ingredient.name;
      if (i > 0) {
        names.push(' ' + ingredientName);
      } else {
        names.push(ingredientName);
      }
    }
    return names;
  }

  getMissingIngredientsString(length: number): string {
    if (length > 1) {
      return length + ' Zutaten fehlen';
    } else {
      return 'Eine Zutat fehlt';
    }
  }
}
