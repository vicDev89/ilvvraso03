import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { IngredientsInRecipeComponentComponent } from './ingredients-in-recipe-component.component';

describe('IngredientsInRecipeComponentComponent', () => {
  let component: IngredientsInRecipeComponentComponent;
  let fixture: ComponentFixture<IngredientsInRecipeComponentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ IngredientsInRecipeComponentComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(IngredientsInRecipeComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
