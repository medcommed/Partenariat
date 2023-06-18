import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SituationProjetDetailComponent } from './situation-projet-detail.component';

describe('SituationProjet Management Detail Component', () => {
  let comp: SituationProjetDetailComponent;
  let fixture: ComponentFixture<SituationProjetDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SituationProjetDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ situationProjet: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SituationProjetDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SituationProjetDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load situationProjet on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.situationProjet).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
