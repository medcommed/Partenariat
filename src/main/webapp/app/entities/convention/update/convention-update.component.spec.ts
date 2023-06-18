import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ConventionFormService } from './convention-form.service';
import { ConventionService } from '../service/convention.service';
import { IConvention } from '../convention.model';
import { IProjet } from 'app/entities/projet/projet.model';
import { ProjetService } from 'app/entities/projet/service/projet.service';
import { ITypeConvention } from 'app/entities/type-convention/type-convention.model';
import { TypeConventionService } from 'app/entities/type-convention/service/type-convention.service';
import { IPartenaire } from 'app/entities/partenaire/partenaire.model';
import { PartenaireService } from 'app/entities/partenaire/service/partenaire.service';

import { ConventionUpdateComponent } from './convention-update.component';

describe('Convention Management Update Component', () => {
  let comp: ConventionUpdateComponent;
  let fixture: ComponentFixture<ConventionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let conventionFormService: ConventionFormService;
  let conventionService: ConventionService;
  let projetService: ProjetService;
  let typeConventionService: TypeConventionService;
  let partenaireService: PartenaireService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ConventionUpdateComponent],
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
      .overrideTemplate(ConventionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ConventionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    conventionFormService = TestBed.inject(ConventionFormService);
    conventionService = TestBed.inject(ConventionService);
    projetService = TestBed.inject(ProjetService);
    typeConventionService = TestBed.inject(TypeConventionService);
    partenaireService = TestBed.inject(PartenaireService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Projet query and add missing value', () => {
      const convention: IConvention = { id: 456 };
      const projet: IProjet = { id: 74851 };
      convention.projet = projet;

      const projetCollection: IProjet[] = [{ id: 31850 }];
      jest.spyOn(projetService, 'query').mockReturnValue(of(new HttpResponse({ body: projetCollection })));
      const additionalProjets = [projet];
      const expectedCollection: IProjet[] = [...additionalProjets, ...projetCollection];
      jest.spyOn(projetService, 'addProjetToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ convention });
      comp.ngOnInit();

      expect(projetService.query).toHaveBeenCalled();
      expect(projetService.addProjetToCollectionIfMissing).toHaveBeenCalledWith(
        projetCollection,
        ...additionalProjets.map(expect.objectContaining)
      );
      expect(comp.projetsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call TypeConvention query and add missing value', () => {
      const convention: IConvention = { id: 456 };
      const typeConvention: ITypeConvention = { id: 33010 };
      convention.typeConvention = typeConvention;

      const typeConventionCollection: ITypeConvention[] = [{ id: 53926 }];
      jest.spyOn(typeConventionService, 'query').mockReturnValue(of(new HttpResponse({ body: typeConventionCollection })));
      const additionalTypeConventions = [typeConvention];
      const expectedCollection: ITypeConvention[] = [...additionalTypeConventions, ...typeConventionCollection];
      jest.spyOn(typeConventionService, 'addTypeConventionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ convention });
      comp.ngOnInit();

      expect(typeConventionService.query).toHaveBeenCalled();
      expect(typeConventionService.addTypeConventionToCollectionIfMissing).toHaveBeenCalledWith(
        typeConventionCollection,
        ...additionalTypeConventions.map(expect.objectContaining)
      );
      expect(comp.typeConventionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Partenaire query and add missing value', () => {
      const convention: IConvention = { id: 456 };
      const partenaires: IPartenaire[] = [{ id: 74505 }];
      convention.partenaires = partenaires;

      const partenaireCollection: IPartenaire[] = [{ id: 2516 }];
      jest.spyOn(partenaireService, 'query').mockReturnValue(of(new HttpResponse({ body: partenaireCollection })));
      const additionalPartenaires = [...partenaires];
      const expectedCollection: IPartenaire[] = [...additionalPartenaires, ...partenaireCollection];
      jest.spyOn(partenaireService, 'addPartenaireToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ convention });
      comp.ngOnInit();

      expect(partenaireService.query).toHaveBeenCalled();
      expect(partenaireService.addPartenaireToCollectionIfMissing).toHaveBeenCalledWith(
        partenaireCollection,
        ...additionalPartenaires.map(expect.objectContaining)
      );
      expect(comp.partenairesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const convention: IConvention = { id: 456 };
      const projet: IProjet = { id: 43350 };
      convention.projet = projet;
      const typeConvention: ITypeConvention = { id: 29523 };
      convention.typeConvention = typeConvention;
      const partenaire: IPartenaire = { id: 52638 };
      convention.partenaires = [partenaire];

      activatedRoute.data = of({ convention });
      comp.ngOnInit();

      expect(comp.projetsSharedCollection).toContain(projet);
      expect(comp.typeConventionsSharedCollection).toContain(typeConvention);
      expect(comp.partenairesSharedCollection).toContain(partenaire);
      expect(comp.convention).toEqual(convention);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IConvention>>();
      const convention = { id: 123 };
      jest.spyOn(conventionFormService, 'getConvention').mockReturnValue(convention);
      jest.spyOn(conventionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ convention });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: convention }));
      saveSubject.complete();

      // THEN
      expect(conventionFormService.getConvention).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(conventionService.update).toHaveBeenCalledWith(expect.objectContaining(convention));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IConvention>>();
      const convention = { id: 123 };
      jest.spyOn(conventionFormService, 'getConvention').mockReturnValue({ id: null });
      jest.spyOn(conventionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ convention: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: convention }));
      saveSubject.complete();

      // THEN
      expect(conventionFormService.getConvention).toHaveBeenCalled();
      expect(conventionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IConvention>>();
      const convention = { id: 123 };
      jest.spyOn(conventionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ convention });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(conventionService.update).toHaveBeenCalled();
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

    describe('compareTypeConvention', () => {
      it('Should forward to typeConventionService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(typeConventionService, 'compareTypeConvention');
        comp.compareTypeConvention(entity, entity2);
        expect(typeConventionService.compareTypeConvention).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('comparePartenaire', () => {
      it('Should forward to partenaireService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(partenaireService, 'comparePartenaire');
        comp.comparePartenaire(entity, entity2);
        expect(partenaireService.comparePartenaire).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
