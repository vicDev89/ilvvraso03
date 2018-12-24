import {Component, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {Recipe} from '../../../dataclasses/Recipe';
import {IngreatService} from '../../../services/ingreat.service';
import {HttpErrorResponse} from '@angular/common/http';

@Component({
  selector: 'app-recipes-component',
  templateUrl: './recipes-component.component.html',
  styleUrls: ['./recipes-component.component.css']
})
export class RecipesComponentComponent implements OnInit, OnChanges {

  @Input() searchedIngredients: string[] = [];

  recipes: Recipe[] = [];
  restRecipes: Recipe[] = [];
  restLoaded: boolean = false;

  // google maps zoom level
  zoom: number =12;

  // initial center position for the map
  lat: number = 52.555580;
  lng: number = 13.346326;

  //edeka Beispiel
  edekaLat: number = 52.552609;
  edekaLng: number = 13.347636;

  //Rewe Beispiel
  reweLat: number = 52.563057;
  reweLng: number = 13.328710;

  urlEdekaLogo = '../../../../assets/edeka-logo-map.png';
  urlREWELogo = '../../../../assets/rewe-logo-map.png';


  constructor( private ingreatService: IngreatService) { }

  ngOnInit() {
    this.recipes = [];
    this.searchedIngredients = [];
  }

  ngOnChanges(changes: SimpleChanges): void {
    if(this.searchedIngredients && this.searchedIngredients.length > 0){
        this.ingreatService.getFirst10RecipesByIngredients(this.searchedIngredients).subscribe(data => {
        this.recipes = this.loadFehlendeZutatenToRecipes(data);
          if(data.length === 10){
            this.ingreatService.getRestOfRecipesByIngredients(this.searchedIngredients).subscribe(data => {
              this.restRecipes = this.loadFehlendeZutatenToRecipes(data);
              this.restLoaded = true;
            }, (error: HttpErrorResponse) => {
              console.log(`Backend returned code ${error.status}, body was: ${error.error}`);
            });
          }
      }, (error: HttpErrorResponse) => {
        console.log(`Backend returned code ${error.status}, body was: ${error.error}`);
      });
    }
  }

  loadRestInToRecipes(){
    this.recipes = this.recipes.concat(this.restRecipes);
  }

  loadFehlendeZutatenToRecipes(recipes: Recipe[]){
    for (const recipe of recipes) {
      recipe.fehlendeZutaten = [];
      for (const ingredientInRecipe of recipe.ingredientInRecipes) {
        if(!this.searchedIngredients.includes(ingredientInRecipe.ingredient.name)){
          recipe.fehlendeZutaten.push(ingredientInRecipe);
        }
      }
    }
    recipes.sort((a,b) => a.fehlendeZutaten.length > b.fehlendeZutaten.length ? 1:-1);
    return recipes;
  }

}
