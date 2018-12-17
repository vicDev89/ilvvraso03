import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { IngredientsSearchComponentComponent } from './ingredients-search-component.component';

describe('IngredientsSearchComponentComponent', () => {
  let component: IngredientsSearchComponentComponent;
  let fixture: ComponentFixture<IngredientsSearchComponentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ IngredientsSearchComponentComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(IngredientsSearchComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
