import {Component} from '@angular/core';
import {IngreatService} from "../../../services/ingreat.service";

@Component({
  selector: 'app-header-component',
  templateUrl: './header-component.component.html',
  styleUrls: ['./header-component.component.css']
})
export class HeaderComponentComponent {

  constructor(
    private ingreatService: IngreatService
  ) { }

  getHeight(): number {
    return document.getElementById('header').offsetHeight;
  }

  returnToStart(): void {
    if (!this.ingreatService.isNeverBeenSearchedForRecipes) {
      this.ingreatService.isNeverBeenSearchedForRecipes = true;
    }
  }
}
