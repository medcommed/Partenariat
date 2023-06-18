import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SituationProjetFormService } from './situation-projet-form.service';
import { SituationProjetService } from '../service/situation-projet.service';
import { ISituationProjet } from '../situation-projet.model';
import { IProjet } from 'app/entities/projet/projet.model';
import { ProjetService } from 'app/entities/projet/service/projet.service';

import { SituationProjetUpdateComponent } from './situation-projet-update.component';

describe('SituationProjet Management Update Component', () => {
  let comp: SituationProjetUpdateComponent;
  let fixture: ComponentFixture<SituationProjetUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let situationProjetFormService: SituationProjetFormService;
  let situationProjetService: SituationProjetService;
  let projetService: ProjetService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SituationProjetUpdateComponent],
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
      .overrideTemplate(SituationProjetUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SituationProjetUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    situationProjetFormService = TestBed.inject(SituationProjetFormService);
    situationProjetService = TestBed.inject(SituationProjetService);
    projetService = TestBed.inject(ProjetService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Projet query and add missing value', () => {
      const situationProjet: ISituationProjet = { id: 456 };
      const projet: IProjet = { id: 66555 };
      situationProjet.projet = projet;

      const projetCollection: IProjet[] = [{ id: 63637 }];
      jest.spyOn(projetService, 'query').mockReturnValue(of(new HttpResponse({ body: projetCollection })));
      const additionalProjets = [projet];
      const expectedCollection: IProjet[] = [...additionalProjets, ...projetCollection];
      jest.spyOn(projetService, 'addProjetToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ situationProjet });
      comp.ngOnInit();

      expect(projetService.query).toHaveBeenCalled();
      expect(projetService.addProjetToCollectionIfMissing).toHaveBeenCalledWith(
        projetCollection,
        ...additionalProjets.map(expect.objectContaining)
      );
      expect(comp.projetsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const situationProjet: ISituationProjet = { id: 456 };
      const projet: IProjet = { id: 90085 };
      situationProjet.projet = projet;

      activatedRoute.data = of({ situationProjet });
      comp.ngOnInit();

      expect(comp.projetsSharedCollection).toContain(projet);
      expect(comp.situationProjet).toEqual(situationProjet);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISituationProjet>>();
      const situationProjet = { id: 123 };
      jest.spyOn(situationProjetFormService, 'getSituationProjet').mockReturnValue(situationProjet);
      jest.spyOn(situationProjetService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ situationProjet });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: situationProjet }));
      saveSubject.complete();

      // THEN
      expect(situationProjetFormService.getSituationProjet).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(situationProjetService.update).toHaveBeenCalledWith(expect.objectContaining(situationProjet));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISituationProjet>>();
      const situationProjet = { id: 123 };
      jest.spyOn(situationProjetFormService, 'getSituationProjet').mockReturnValue({ id: null });
      jest.spyOn(situationProjetService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ situationProjet: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: situationProjet }));
      saveSubject.complete();

      // THEN
      expect(situationProjetFormService.getSituationProjet).toHaveBeenCalled();
      expect(situationProjetService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISituationProjet>>();
      const situationProjet = { id: 123 };
      jest.spyOn(situationProjetService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ situationProjet });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(situationProjetService.update).toHaveBeenCalled();
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
