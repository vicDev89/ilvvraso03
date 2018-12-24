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
import {SupermarktGEO} from "../dataclasses/SupermarktGEO";

@Injectable({
  providedIn: 'root'
})
export class IngreatService{
  private url: string;
  private ingredients: Ingredient[];

  constructor(private http: HttpClient) {
    this.url = 'http://1d61324f.ngrok.io/api/';
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

  getFirst10RecipesByIngredients(ingredients: string[]): Observable<any>{
    var ingredientsList = new IngredientsList(ingredients);
    return this.http.post<any>(this.url + 'getRecipesMax', ingredientsList);
  }

  getRestOfRecipesByIngredients(ingredients: string[]): Observable<any>{
    var ingredientsList = new IngredientsList(ingredients);
    return this.http.post<any>(this.url + 'getRecipesRest', ingredientsList);
  }

  getAllIngredients(){
    return this.http.get<Ingredient[]>(this.url + 'getAllIngredients').subscribe(data => {
      // this.ingreatService.getFirst10RecipesByIngredients(searchedIngredients).subscribe( data => {
      this.ingredients = data;
    }, (error: HttpErrorResponse) => {
      console.log(`Backend returned code ${error.status}, body was: ${error.error}`);
    });

  }

  reqMeasuresOfIngredient(ingredientName: string): Observable<string[]> {
    return this.http.get<string[]>(this.url + 'getMeasures/' + ingredientName);
  }

  getSupermarktLocations(){
    return this.getMockLocations();
    // return this.http.get<SupermarktGEO[]>(this.url + '/getSupermarktLocations');
  }

  //MOCK
  getMockLocations(){
    let mockEdeka = new SupermarktGEO(
      Supermarket.EDEKA,
      'EDEKA Fromm',
      52.552609, 13.347636,
      'Müllerstraße',
      '127',
      'Berlin',
      13349);

    let mockRewe = new SupermarktGEO(
      Supermarket.REWE,
      'REWE',
      52.563057, 13.328710,
      'Kurt-Schumacher-Damm',
      '1-15',
      'Berlin',
      13405);

    return of(mockEdeka, mockRewe);
  }

}

