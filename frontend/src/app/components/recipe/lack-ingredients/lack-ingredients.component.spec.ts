import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LackIngredientsComponent } from './lack-ingredients.component';

describe('LackIngredientsComponent', () => {
  let component: LackIngredientsComponent;
  let fixture: ComponentFixture<LackIngredientsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LackIngredientsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LackIngredientsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
