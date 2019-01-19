import {Component, Inject, OnInit, ViewChild} from '@angular/core';
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
  missingIngredients: IngredientInRecipe[];
  supermarketGeoLocations: SupermarketGEO[];
  searchedIngredients: string[];

  superMarketEnum = Supermarket;

  // google maps zoom level
  zoom: number = 14;

  // initial center position for the map
  lat: number = 52.519569;
  lng: number = 13.409057;

  urlEdekaLogo = '../../../../assets/edeka-logo-map.png';
  urlREWELogo = '../../../../assets/rewe-logo-map.png';

  showLackIngredients: boolean = false;
  supermarket: Supermarket;

  infoWindowOpened = null;
  previous_info_window = null;

  @ViewChild('map') private map;

  ngOnInit() {
    this.recipe = this.data.recipe;
    this.missingIngredients = this.data.fehlendeZustaten;
    this.supermarketGeoLocations = this.data.supermarketGeoLocations;
    this.searchedIngredients = this.data.searchedIngredients;
    this.findMe();
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

  findMe() {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition((position) => {
        this.lat = position.coords.latitude;
        this.lng = position.coords.longitude;
      }, (error) => {
        console.log("Position Error:" + error.message);
      });
    } else {
      alert("Geolocation is not supported by this browser.");
    }
  }

  close_window(){
    if (this.previous_info_window != null ) {
      this.previous_info_window.close()
    }
  }

  select_marker(event){
    let infoWindow = event.infoWindow._results[0];
    if (this.previous_info_window == null)
      this.previous_info_window = infoWindow;
    else{
      this.infoWindowOpened = infoWindow;
      this.previous_info_window.close()
    }
    this.previous_info_window = infoWindow
  }

}
