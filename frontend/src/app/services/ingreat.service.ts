import {Injectable} from '@angular/core';
import {Ingredient} from '../dataclasses/Ingredient';
import {from, Observable, of} from 'rxjs';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {IngredientsList} from '../dataclasses/IngredientsList';
import {SupermarketGEO} from "../dataclasses/SupermarketGEO";

@Injectable({
  providedIn: 'root'
})
export class IngreatService{
  readonly url: string;
  private ingredients: Ingredient[];

  constructor(private http: HttpClient) {
    this.url = 'http://1d61324f.ngrok.io/api/';
    this.getAllIngredients();
  }

  searchIngredients(term: string): Observable<Ingredient[]> {
    if (!term.trim()) {
      return of([]);
    }
    if (this.ingredients) {
      return from(
        [
          this.ingredients.filter(
            ingredient => {
              const iNameLowerCase = ingredient.name.toLowerCase();
              const termLowerCase = term.toLowerCase();
              if (iNameLowerCase.includes(termLowerCase)) {
                const index = iNameLowerCase.indexOf(termLowerCase);
                return index === 0 || iNameLowerCase.charAt(index - 1).match('\\s');
              } else {
                return false;
              }
            }
          )
        ]
      );
    } else {
      return undefined;
    }
  }

  getFirst10RecipesByIngredients(ingredients: string[]): Observable<any>{
    const ingredientsList = new IngredientsList(ingredients);
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

  getSupermarktLocations() : Observable<SupermarketGEO[]>{
    return this.http.get<SupermarketGEO[]>(this.url + 'getAllSupermarketGeo');
  }

  isIngredientsSet(): boolean {
    return !!this.ingredients;
  }

}
