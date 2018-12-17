import {Component, EventEmitter, Output} from '@angular/core';
import {Ingredient} from "../../../dataclasses/Ingredient";

/**
 * Logic concerning the ingredients-list.
 *
 * @since 05.11.2018
 * @author Lucas Larisch
 */
@Component({
  selector: 'app-ingredients-component',
  templateUrl: './ingredients-component.component.html',
  styleUrls: ['./ingredients-component.component.css']
})
export class IngredientsComponentComponent {

  @Output()
  registeredIngredients: EventEmitter<Map<Ingredient, AmountInMeasure>> = new EventEmitter();

  /** List containing all ingredients the user has added. */
  private _registeredIngredients: Map<Ingredient, AmountInMeasure> = new Map<Ingredient, AmountInMeasure>();

  /**
   * Pushes an ingredient to the array containing all added
   * ingredients.
   *
   * @since 05.11.2018
   * @author Lucas Larisch
   * @param {Ingredient} ingredient Ingredient to be added.
   */
  onIngredientAdded(ingredient: Ingredient): void {
    this._registeredIngredients.set(ingredient, undefined);
  }

  /**
   * Method to change the amount in a measure of an ingredient. In case of the
   * amount being 0, the ingredient will be deleted from {@link _registeredIngredients}.
   *
   * @since 12.11.2018
   * @author Lucas Larisch
   * @param $event Amount in a measure to be set for the ingredient.
   * @param ingredient The ingredient the amount of is to be changed.
   */
  onAmountChanged($event: AmountInMeasure, ingredient: Ingredient): void {
    if ($event.amount === 0) {
      this._registeredIngredients.delete(ingredient);
    } else {
      this._registeredIngredients.set(ingredient, $event);
    }
  }

  /**
   * Returns all added ingredients as array.
   *
   * @since 12.11.2018
   * @author Lucas Larisch
   * @returns {Ingredient[]} All added ingredients as array.
   */
  registeredIngredientsAsArray(): Ingredient[] {
      return Array.from(this._registeredIngredients.keys());
  }

  /**
   * Emits the registered ingredients to the output value.
   *
   * @since 10.11.2018
   * @author Lucas Larisch
   */
  onSearchRecipes(): void {
    this.registeredIngredients.emit(this._registeredIngredients);
  }
}

/**
 * @since 12.11.2018
 * @author Lucas Larisch
 */
export class AmountInMeasure {

  constructor(amount?: number, measure?: string) {
    this.amount = amount;
    this.measure = measure;
  }

  amount: number;
  measure: string;
}
