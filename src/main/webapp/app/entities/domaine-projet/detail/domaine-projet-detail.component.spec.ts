import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DomaineProjetDetailComponent } from './domaine-projet-detail.component';

describe('DomaineProjet Management Detail Component', () => {
  let comp: DomaineProjetDetailComponent;
  let fixture: ComponentFixture<DomaineProjetDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DomaineProjetDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ domaineProjet: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DomaineProjetDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DomaineProjetDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load domaineProjet on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.domaineProjet).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
