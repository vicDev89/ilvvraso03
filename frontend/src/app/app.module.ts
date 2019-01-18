import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { MatDialogModule, MatTooltipModule } from '@angular/material';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import { AgmCoreModule } from '@agm/core';


import { AppComponent } from './app.component';
import { IngredientsComponentComponent } from './components/search/ingredients-component/ingredients-component.component';
import { HeaderComponentComponent } from './components/design/header-component/header-component.component';
import { RecipesComponentComponent } from './components/recipe/recipes-component/recipes-component.component';
import { IngredientsSearchComponentComponent } from './components/search/ingredients-search-component/ingredients-search-component.component';
import { RecipeComponentComponent } from './components/recipe/recipe-component/recipe-component.component';
import { RecipePopupComponent } from './components/recipe/recipe-popup/recipe-popup.component';
import { IngredientComponentComponent } from './components/search/ingredient-component/ingredient-component.component';
import { IngredientsInRecipeComponentComponent } from './components/recipe/ingredients-in-recipe-component/ingredients-in-recipe-component.component';
import {HttpClientModule} from '@angular/common/http';
import { LackIngredientsComponent } from './components/recipe/lack-ingredients/lack-ingredients.component';
import { LoadingComponent } from './components/design/loading/loading.component';
import { NoRecipeFoundComponent } from './components/recipe/no-recipe-found/no-recipe-found.component';

@NgModule({
  declarations: [
    AppComponent,
    IngredientsComponentComponent,
    HeaderComponentComponent,
    RecipesComponentComponent,
    IngredientsSearchComponentComponent,
    RecipeComponentComponent,
    RecipePopupComponent,
    IngredientComponentComponent,
    IngredientsInRecipeComponentComponent,
    LackIngredientsComponent,
    LoadingComponent,
    NoRecipeFoundComponent
  ],
  imports: [
    BrowserModule,
    MatDialogModule,
    BrowserAnimationsModule,
    NgbModule,
    HttpClientModule,
    MatTooltipModule,
    AgmCoreModule.forRoot({
      apiKey: 'AIzaSyBfwuLl4dK8T7H5ltbiWicPOieT4qym0Oc'
    })
  ],
  exports: [
    MatTooltipModule
  ],
  providers: [],
  bootstrap: [AppComponent],
  entryComponents: [RecipePopupComponent]
})
export class AppModule { }
