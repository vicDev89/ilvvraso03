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

  constructor( private ingreatService: IngreatService) { }

  ngOnInit() {
    this.recipes = [];
    this.searchedIngredients = [];
  }

  ngOnChanges(changes: SimpleChanges): void {
    if(this.searchedIngredients && this.searchedIngredients.length > 0){
        this.ingreatService.getFirst10RecipesByIngredients(this.searchedIngredients).subscribe(data => {
        this.recipes = data;
          if(data.length === 10){
            this.ingreatService.getRestOfRecipesByIngredients(this.searchedIngredients).subscribe(data => {
              this.restRecipes = data;
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
    console.log("loadRestInToRecipes function was called");
    console.log("Recipes.length: " + this.recipes.length);
    console.log("RestRecipes: " + this.restRecipes.length);
    this.recipes = this.recipes.concat(this.restRecipes);
    console.log("Recipes.length after concat: " + this.recipes.length);

  }

}
