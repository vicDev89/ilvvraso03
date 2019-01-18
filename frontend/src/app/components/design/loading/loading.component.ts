import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-loading',
  templateUrl: './loading.component.html',
  styleUrls: ['./loading.component.css']
})
export class LoadingComponent implements OnInit {

  @Input() size: number;

  random_icon: string;

  private readonly CLASSES = [
    // TODO: Include commented icons?
    "fas fa-lemon",
    "fas fa-stroopwafel",
    // "fas fa-carrot",
    "fas fa-apple-alt",
    // "fas fa-candy-cane",
    "fas fa-cookie",
    "fas fa-utensils",
    "fas fa-cookie-bite",
    "fas fa-cocktail"
  ];

  ngOnInit(): void {
    this.random_icon = this.CLASSES[Math.floor(Math.random() * this.CLASSES.length)];
  }

}
