import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Ingredient} from "../../../dataclasses/Ingredient";
import {AmountInMeasure} from "../ingredients-component/ingredients-component.component";
import {IngreatService} from "../../../services/ingreat.service";

@Component({
  selector: 'app-ingredient-component',
  templateUrl: './ingredient-component.component.html',
  styleUrls: ['./ingredient-component.component.css']
})
export class IngredientComponentComponent implements OnInit {

  @Input()
  ingredient: Ingredient;

  @Output()
  amountInMeasure: EventEmitter<AmountInMeasure> = new EventEmitter<AmountInMeasure>();

  measures: string[];

  private amountInMeasureValue: AmountInMeasure = new AmountInMeasure();

  constructor(
    private ingreatService: IngreatService
  ){ }

  /**
   * @author Lucas Larisch
   * @since 02.12.2018
   */
  ngOnInit(): void {
    this.ingreatService.reqMeasuresOfIngredient(this.ingredient.name).subscribe(
      measures => this.measures = measures
    );
  }

  /**
   * Sets a new object {@link AmountInMeasure} to the EventEmitter with
   * {@link AmountInMeasure.amount} being 0.
   *
   * @since 12.11.2018
   * @author Lucas Larisch
   */
  deleteIngredient(): void {
    this.amountInMeasureValue.amount = 0;
    this.amountInMeasure.emit(this.amountInMeasureValue);
  }

  /**
   *
   * @since 30.11.2018
   * @author Lucas Larisch
   * @param accordion
   * @param panel
   */
  showOrHideMeasure(accordion: HTMLDivElement, panel: HTMLDivElement): void {
    accordion.classList.toggle("active");
    if (panel.style.maxHeight) {
      panel.style.maxHeight = null;
    } else {
      panel.style.maxHeight = panel.scrollHeight + "px";
    }
  }

  /**
   *
   * @since 30.11.2018
   * @author Lucas Larisch
   */
  amountOrMeasureChanged(amount: HTMLInputElement, measure: HTMLSelectElement): void {
    let value = Number(amount.value);
    if(value && value > 0) {
      this.amountInMeasureValue.amount = value;
    } else if (value < 0) {
      this.amountInMeasureValue.amount = - value;
      amount.value = '' + this.amountInMeasureValue.amount;
    }
    this.amountInMeasureValue.measure = this.measures[measure.selectedIndex];
    this.amountInMeasure.emit(this.amountInMeasureValue);
  }
}
