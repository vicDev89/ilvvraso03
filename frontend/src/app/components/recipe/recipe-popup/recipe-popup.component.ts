import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material';
import {Recipe} from '../../../dataclasses/Recipe';
import {IngredientInRecipe} from '../../../dataclasses/IngredientInRecipe';
import {SupermarketGEO} from "../../../dataclasses/SupermarketGEO";
import {Supermarket} from "../../../dataclasses/Supermarket";

export interface DialogData {
  recipe: Recipe;
  fehlendeZustaten: IngredientInRecipe[];
  supermarketGeoLocations: SupermarketGEO[];
  searchedIngredients: string[];
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
  supermarketGeoLocations: SupermarketGEO[];
  searchedIngredients: string[];

  superMarketEnum = Supermarket;

  // google maps zoom level
  zoom: number = 14;

  // initial center position for the map
  lat: number = 52.555773;
  lng: number = 13.347031;

  urlEdekaLogo = '../../../../assets/edeka-logo-map.png';
  urlREWELogo = '../../../../assets/rewe-logo-map.png';

  showLackIngredients: boolean = false;
  supermarket: Supermarket;

  ngOnInit() {
    this.recipe = this.data.recipe;
    this.fehlendeZustaten = this.data.fehlendeZustaten;
    this.supermarketGeoLocations = this.data.supermarketGeoLocations;
    this.searchedIngredients = this.data.searchedIngredients;
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  getPictureURL(supermarkt: Supermarket) {
    switch (supermarkt) {
      case Supermarket.REWE:
        return this.urlREWELogo;
      case Supermarket.EDEKA:
        return this.urlEdekaLogo;
    }
  }

}
