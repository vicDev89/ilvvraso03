import {Injectable, OnInit} from '@angular/core';
import {Recipe} from '../dataclasses/Recipe';
import {Ingredient} from '../dataclasses/Ingredient';
import {DifficultyLevel} from '../dataclasses/DifficultyLevel';
import {IngredientInRecipe} from '../dataclasses/IngredientInRecipe';
import {from, Observable, of} from 'rxjs';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {Product} from '../dataclasses/Product';
import {Supermarket} from '../dataclasses/Supermarket';
import {IngredientsList} from '../dataclasses/IngredientsList';

@Injectable({
  providedIn: 'root'
})
export class IngreatService{
  private url: string;
  private ingredients: Ingredient[];

  constructor(private http: HttpClient) {
    this.url = 'https://localhost:8080/api/';
    this.getAllIngredients();
  }

  searchIngredients(term: string): Observable<Ingredient[]> {
    if (!term.trim()) {
      return of([]);
    }
    return from(
      [
        this.ingredients.filter(
          ingredient => ingredient.name.toLowerCase().includes(term.toLowerCase())
        )
      ]
    );
  }

  reqRecipesByIngredients(ingredients: string[]): Observable<any>{
    var ingredientsList = new IngredientsList(ingredients);
    return this.http.post<any>(this.url + 'getRecipes', ingredientsList);
  }

  getAllIngredients(){
    return this.http.get<Ingredient[]>(this.url + 'getAllIngredients').subscribe(data => {
      // this.ingreatService.reqRecipesByIngredients(searchedIngredients).subscribe( data => {
      this.ingredients = data;
    }, (error: HttpErrorResponse) => {
      console.log(`Backend returned code ${error.status}, body was: ${error.error}`);
    });

  }

  reqMeasuresOfIngredient(ingredientName: string): Observable<string[]> {
    return this.http.get<string[]>(this.url + 'getMeasures/' + ingredientName);
  }

}

