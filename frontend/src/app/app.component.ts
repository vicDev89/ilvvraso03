import {Component, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {Ingredient} from './dataclasses/Ingredient';
import {AmountInMeasure} from './components/search/ingredients-component/ingredients-component.component';
import {SupermarketGEO} from "./dataclasses/SupermarketGEO";
import {HttpErrorResponse} from "@angular/common/http";
import {IngreatService} from "./services/ingreat.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{
  title = 'ilvvarso03FE';

  searchedIngredientsNames: string[];
  supermarketsGEO: SupermarketGEO[] = [];

  constructor( private ingreatService: IngreatService) { }

  ngOnInit(): void {
    this.getSupermarketLocations();
  }

  extractKeyOfSearchedIngredients(searchedIngredients: Map<Ingredient, AmountInMeasure>){
    var searchedIngredientsNames = [];
    searchedIngredients.forEach(function (v,k) {
      searchedIngredientsNames.push(k.name);
    });
    this.searchedIngredientsNames = searchedIngredientsNames;
  }

  getSupermarketLocations(): void {
    this.ingreatService.getSupermarktLocations().subscribe(data => {
      // this.supermarketsGEO = [data[0],data[1]];
      this.supermarketsGEO = data;
    }, (error: HttpErrorResponse) => {
      console.log(`Backend returned code ${error.status}, body was: ${error.error}`);
    });
  }

  isResized(): boolean {
    return window.innerWidth > 575;
    // = before resize breakpoint?
    // Src: https://getbootstrap.com/docs/4.2/layout/overview/
  }
}
