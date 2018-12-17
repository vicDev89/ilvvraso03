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

  constructor( private ingreatService: IngreatService) { }

  ngOnInit() {
    this.recipes = [];
    this.searchedIngredients = [];
  }

  ngOnChanges(changes: SimpleChanges): void {
    if(this.searchedIngredients && this.searchedIngredients.length > 0){
        this.ingreatService.reqRecipesByIngredients(this.searchedIngredients).subscribe( data => {
        this.recipes = data;
      }, (error: HttpErrorResponse) => {
        console.log(`Backend returned code ${error.status}, body was: ${error.error}`);
      });
    }

  }

}
