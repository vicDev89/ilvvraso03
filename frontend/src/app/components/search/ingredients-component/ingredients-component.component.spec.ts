import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { IngredientsComponentComponent } from './ingredients-component.component';

describe('IngredientsComponentComponent', () => {
  let component: IngredientsComponentComponent;
  let fixture: ComponentFixture<IngredientsComponentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ IngredientsComponentComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(IngredientsComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
