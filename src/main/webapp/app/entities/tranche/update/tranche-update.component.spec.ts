import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TrancheFormService } from './tranche-form.service';
import { TrancheService } from '../service/tranche.service';
import { ITranche } from '../tranche.model';
import { IProjet } from 'app/entities/projet/projet.model';
import { ProjetService } from 'app/entities/projet/service/projet.service';

import { TrancheUpdateComponent } from './tranche-update.component';

describe('Tranche Management Update Component', () => {
  let comp: TrancheUpdateComponent;
  let fixture: ComponentFixture<TrancheUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let trancheFormService: TrancheFormService;
  let trancheService: TrancheService;
  let projetService: ProjetService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TrancheUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(TrancheUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TrancheUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    trancheFormService = TestBed.inject(TrancheFormService);
    trancheService = TestBed.inject(TrancheService);
    projetService = TestBed.inject(ProjetService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Projet query and add missing value', () => {
      const tranche: ITranche = { id: 456 };
      const projet: IProjet = { id: 10173 };
      tranche.projet = projet;

      const projetCollection: IProjet[] = [{ id: 7317 }];
      jest.spyOn(projetService, 'query').mockReturnValue(of(new HttpResponse({ body: projetCollection })));
      const additionalProjets = [projet];
      const expectedCollection: IProjet[] = [...additionalProjets, ...projetCollection];
      jest.spyOn(projetService, 'addProjetToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ tranche });
      comp.ngOnInit();

      expect(projetService.query).toHaveBeenCalled();
      expect(projetService.addProjetToCollectionIfMissing).toHaveBeenCalledWith(
        projetCollection,
        ...additionalProjets.map(expect.objectContaining)
      );
      expect(comp.projetsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const tranche: ITranche = { id: 456 };
      const projet: IProjet = { id: 22995 };
      tranche.projet = projet;

      activatedRoute.data = of({ tranche });
      comp.ngOnInit();

      expect(comp.projetsSharedCollection).toContain(projet);
      expect(comp.tranche).toEqual(tranche);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITranche>>();
      const tranche = { id: 123 };
      jest.spyOn(trancheFormService, 'getTranche').mockReturnValue(tranche);
      jest.spyOn(trancheService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tranche });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tranche }));
      saveSubject.complete();

      // THEN
      expect(trancheFormService.getTranche).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(trancheService.update).toHaveBeenCalledWith(expect.objectContaining(tranche));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITranche>>();
      const tranche = { id: 123 };
      jest.spyOn(trancheFormService, 'getTranche').mockReturnValue({ id: null });
      jest.spyOn(trancheService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tranche: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tranche }));
      saveSubject.complete();

      // THEN
      expect(trancheFormService.getTranche).toHaveBeenCalled();
      expect(trancheService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITranche>>();
      const tranche = { id: 123 };
      jest.spyOn(trancheService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tranche });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(trancheService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareProjet', () => {
      it('Should forward to projetService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(projetService, 'compareProjet');
        comp.compareProjet(entity, entity2);
        expect(projetService.compareProjet).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
