import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TypeConventionDetailComponent } from './type-convention-detail.component';

describe('TypeConvention Management Detail Component', () => {
  let comp: TypeConventionDetailComponent;
  let fixture: ComponentFixture<TypeConventionDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TypeConventionDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ typeConvention: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TypeConventionDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TypeConventionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load typeConvention on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.typeConvention).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
