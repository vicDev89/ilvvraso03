import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {distinctUntilChanged, switchMap} from "rxjs/operators";
import {Observable, Subject} from "rxjs";
import {Ingredient} from "../../../dataclasses/Ingredient";
import {IngreatService} from "../../../services/ingreat.service";

/**
 * Logic concerning the input field for adding ingredients.
 *
 * @since 05.11.2018
 * @author Lucas Larisch
 */
@Component({
  selector: 'app-ingredients-search-component',
  templateUrl: './ingredients-search-component.component.html',
  styleUrls: ['./ingredients-search-component.component.css']
})
export class IngredientsSearchComponentComponent implements OnInit {

  /**
   * Observable (Ingredient-array) for the list containing suggestions for ingredients matching
   * the entered search term.
   */
  ingredients$: Observable<Ingredient[]>;

  /** Subject for the search terms entered. */
  private searchTerms = new Subject<string>();

  /** Either undefined or an ingredient matching the input. */
  foundIngredient: Ingredient;

  /**
   * List of ingredients that have already been added to the
   * list of added ingredients. (Given in as an input)
   */
  @Input()
  registeredIngredients: Ingredient[];

  /**
   * EventEmitter holding an ingredient to be put out.
   */
  @Output()
  addedIngredient: EventEmitter<Ingredient> = new EventEmitter<Ingredient>();

  /**
   * @param {IngreatService} ingreatService Service used for dynamically requesting ingredients.
   */
  constructor(
    public ingreatService: IngreatService
  ) {}

  /**
   * On initialization, the Observable containing ingredients matching
   * the entered search term is requested and piped.
   *
   * @since 05.11.2018
   * @author Lucas Larisch
   */
  ngOnInit(): void {
    this.ingredients$ = this.searchTerms.pipe(
      distinctUntilChanged(),
      switchMap((term: string) => this.ingreatService.searchIngredients(term))
    );
  }

  /**
   * Starts a search based on the entered term.
   *
   * @since 05.11.2018
   * @author Lucas Larisch
   * @param {string} term Search term entered by the user.
   */
  search(term: string): void {
    this.searchTerms.next(term.trim());
    this.findIngredientOfSubscribedObservable(term);
  }

  /**
   * In case of {@link IngredientsSearchComponentComponent#foundIngredient} not being
   * undefined or the optional param being used, both input field with ID 'search' and
   * loaded {@link IngredientsSearchComponentComponent#ingredients$} will be reset.
   * A new ingredient is bound to the EventEmitter. This ingredient is either
   * the value given in as a param or
   * {@link IngredientsSearchComponentComponent#foundIngredient}.
   *
   * @since 05.11.2018
   * @author Lucas Larisch
   * @param {Ingredient} ingredient Ingredient to be added. (optional param)
   */
  addIngredient(ingredient?: Ingredient): void {
    if (!ingredient) {
      ingredient = this.foundIngredient;
    }
    // ingredient could be undefined:
    if (ingredient) {
      // Binds value to the output emitter:
      this.addedIngredient.emit(ingredient);

      // Clears input field / empty search for clearing the list:
      (<HTMLInputElement> document.getElementById("search")).value = "";
      this.search("");
    }
  }

  /**
   * Checks whether a string given in as a parameter matches the name of an
   * ingredient saved in the list of all added ingredients.
   *
   * @since 05.11.2018
   * @author Lucas Larisch
   * @param {string} ingredientName Name of the ingredient to be checked.
   * @returns true if the checked string is either empty or already saved in the
   *          list containing added ingredients (name of the ingredient). Otherwise
   *          false.
   */
  isIngredientAlreadyAdded(ingredientName: string): boolean {
    return this.registeredIngredients
      .map(ingredient => ingredient.name.toLowerCase())
      .includes(ingredientName.trim().toLowerCase()
      );
  }

  /**
   * Checks whether an ingredient (name) is in the list of suggested ingredients.
   * If so, {@link IngredientsSearchComponentComponent#foundIngredient} will be set
   * to this ingredient, if not it will be undefined.
   *
   * @since 10.11.2018
   * @author Lucas Larisch
   * @param {string} ingredientName Name of the ingredient to be checked if it exists.
   */
  private findIngredientOfSubscribedObservable(ingredientName: string): void {
    this.ingredients$.subscribe(
      ingredients =>
        this.foundIngredient = ingredients
          .filter(ingredient => !this.isIngredientAlreadyAdded(ingredient.name))
          .find(ingredient => ingredient.name.toLowerCase() === ingredientName.toLowerCase().trim())
    );
  }

}
