import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TrancheDetailComponent } from './tranche-detail.component';

describe('Tranche Management Detail Component', () => {
  let comp: TrancheDetailComponent;
  let fixture: ComponentFixture<TrancheDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TrancheDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ tranche: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TrancheDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TrancheDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load tranche on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.tranche).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
