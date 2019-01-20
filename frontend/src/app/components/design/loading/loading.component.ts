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
    "fas fa-lemon",
    "fas fa-stroopwafel",
    "fas fa-apple-alt",
    "fas fa-cookie",
    "fas fa-utensils",
    "fas fa-cookie-bite",
    "fas fa-mortar-pestle",
    "fas fa-wine-glass-alt",
    "fas fa-glass-martini-alt",
    "fas fa-utensil-spoon"
  ];

  ngOnInit(): void {
    this.random_icon = this.CLASSES[Math.floor(Math.random() * this.CLASSES.length)];
  }

}
