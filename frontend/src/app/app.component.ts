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
    this.declareColors();
    this.getSupermarketLocations();
  }

  extractKeyOfSearchedIngredients(searchedIngredients: Map<Ingredient, AmountInMeasure>){
    var searchedIngredientsNames = [];
    searchedIngredients.forEach(function (v,k) {
      searchedIngredientsNames.push(k.name);
    });
    this.searchedIngredientsNames = searchedIngredientsNames;
  }

  getSupermarketLocations(){
    this.ingreatService.getSupermarktLocations().subscribe(data => {
      this.supermarketsGEO = data;
    }, (error: HttpErrorResponse) => {
      console.log(`Backend returned code ${error.status}, body was: ${error.error}`);
    });
  }

  /**
   * Declares colors (hex-values) as variables that can be called in CSS files.
   *
   * @since 19.11.2018
   * @author Lucas Larisch
   */
  declareColors(): void {
    // TODO: Makes sense in our case?
    let primaryMain = '0,0,0';
    let primaryMainAccent = '255,255,255';

    const colors = new Map([
      ['primaryColor', 'rgb(158,0,57)'],
      ['primaryMain', 'rgb('+primaryMain+')'],
      ['primaryMainTransparent', 'rgba('+primaryMain+',0.7)'],
      ['primaryMainAccent', 'rgb('+primaryMainAccent+')'],
      ['primaryMainAccentTransparent', 'rgba('+primaryMainAccent+',0.7)']
    ]);

    Array.from(colors.entries()).forEach(([name, value]) => {
      document.body.style.setProperty(`--${name}`, value);
    });
  }

}
