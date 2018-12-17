import {Component, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {IngredientInRecipe} from '../../../dataclasses/IngredientInRecipe';
import {Ingredient} from '../../../dataclasses/Ingredient';

@Component({
  selector: 'app-lack-ingredients',
  templateUrl: './lack-ingredients.component.html',
  styleUrls: ['./lack-ingredients.component.css']
})
export class LackIngredientsComponent implements OnInit {

  @Input() fehlendeZustaten: IngredientInRecipe[];

  constructor() { }

  ngOnInit() {
  }



}
