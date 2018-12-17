import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material';
import {Recipe} from '../../../dataclasses/Recipe';
import {IngredientInRecipe} from '../../../dataclasses/IngredientInRecipe';

export interface DialogData {
  recipe: Recipe;
  fehlendeZustaten: IngredientInRecipe[];
}

@Component({
  selector: 'app-recipe-popup',
  templateUrl: './recipe-popup.component.html',
  styleUrls: ['./recipe-popup.component.css']
})
export class RecipePopupComponent implements OnInit {

  constructor(public dialogRef: MatDialogRef<RecipePopupComponent>,
              @Inject(MAT_DIALOG_DATA) public data: DialogData) {
  }

  recipe: Recipe;
  fehlendeZustaten: IngredientInRecipe[];

  ngOnInit() {
    this.recipe = this.data.recipe;
    this.fehlendeZustaten = this.data.fehlendeZustaten;
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

}
