import {Component, Input} from '@angular/core';

@Component({
  selector: 'app-loading-text',
  templateUrl: './loading-text.component.html',
  styleUrls: ['./loading-text.component.css']
})
export class LoadingTextComponent {

  @Input()
  size: number;

  @Input()
  text: string;

}
