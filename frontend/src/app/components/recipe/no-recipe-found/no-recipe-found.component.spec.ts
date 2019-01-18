import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NoRecipeFoundComponent } from './no-recipe-found.component';

describe('NoRecipeFoundComponent', () => {
  let component: NoRecipeFoundComponent;
  let fixture: ComponentFixture<NoRecipeFoundComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NoRecipeFoundComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NoRecipeFoundComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
