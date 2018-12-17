import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { IngredientComponentComponent } from './ingredient-component.component';

describe('IngredientComponentComponent', () => {
  let component: IngredientComponentComponent;
  let fixture: ComponentFixture<IngredientComponentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ IngredientComponentComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(IngredientComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
