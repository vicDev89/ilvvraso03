import {Component, Input, OnInit} from '@angular/core';
import {Recipe} from '../../../dataclasses/Recipe';
import {MatDialog} from '@angular/material';
import {RecipePopupComponent} from '../recipe-popup/recipe-popup.component';
import {IngredientInRecipe} from '../../../dataclasses/IngredientInRecipe';


@Component({
  selector: 'app-recipe-component',
  templateUrl: './recipe-component.component.html',
  styleUrls: ['./recipe-component.component.css']
})
export class RecipeComponentComponent implements OnInit {

  @Input() recipe: Recipe;

  fehlendeZustaten: IngredientInRecipe[] = [];

  constructor(public dialog: MatDialog) { }

  ngOnInit() {
    this.fehlendeZustaten = this.recipe.fehlendeZutaten;
  }

  openDialog() {
    const dialogRef = this.dialog.open(RecipePopupComponent, {
      height: '600px',
      width: '800px',
      data: {recipe: this.recipe, fehlendeZustaten: this.fehlendeZustaten}
    });
  }

  getNamesOf(fehlendeZustaten: IngredientInRecipe[]) {
    var names = [];
    for (const ingredientInRecipe of fehlendeZustaten) {
      names.push(ingredientInRecipe.ingredient.name);
    }
    return names;
  }
}
