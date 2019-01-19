import {Component, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {Recipe} from '../../../dataclasses/Recipe';
import {IngreatService} from '../../../services/ingreat.service';
import {HttpErrorResponse} from '@angular/common/http';
import {SupermarketGEO} from "../../../dataclasses/SupermarketGEO";

@Component({
  selector: 'app-recipes-component',
  templateUrl: './recipes-component.component.html',
  styleUrls: ['./recipes-component.component.css']
})
export class RecipesComponentComponent implements OnInit, OnChanges {

  @Input() searchedIngredients: string[] = [];
  @Input() supermarketGeoLocations: SupermarketGEO[] = [];

  recipes: Recipe[] = [];
  restRecipes: Recipe[] = [];
  restLoaded: boolean = false;

  isCurrentlySearching: boolean = false;

  constructor( private ingreatService: IngreatService) { }

  ngOnInit() {
    this.recipes = [];
    this.searchedIngredients = [];
  }

  ngOnChanges(changes: SimpleChanges): void {
    if(this.searchedIngredients && this.searchedIngredients.length > 0){
      this.isCurrentlySearching = true;
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
      this.isCurrentlySearching = false;
      }, (error: HttpErrorResponse) => {
        console.log(`Backend returned code ${error.status}, body was: ${error.error}`);
        this.isCurrentlySearching = false;
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
