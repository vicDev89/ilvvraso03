import { TestBed } from '@angular/core/testing';

import { IngreatService } from './ingreat.service';

describe('IngreatService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: IngreatService = TestBed.get(IngreatService);
    expect(service).toBeTruthy();
  });
});
