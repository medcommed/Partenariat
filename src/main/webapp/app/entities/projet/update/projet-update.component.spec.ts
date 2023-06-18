import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ProjetFormService } from './projet-form.service';
import { ProjetService } from '../service/projet.service';
import { IProjet } from '../projet.model';
import { ICommune } from 'app/entities/commune/commune.model';
import { CommuneService } from 'app/entities/commune/service/commune.service';
import { IDomaineProjet } from 'app/entities/domaine-projet/domaine-projet.model';
import { DomaineProjetService } from 'app/entities/domaine-projet/service/domaine-projet.service';

import { ProjetUpdateComponent } from './projet-update.component';

describe('Projet Management Update Component', () => {
  let comp: ProjetUpdateComponent;
  let fixture: ComponentFixture<ProjetUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let projetFormService: ProjetFormService;
  let projetService: ProjetService;
  let communeService: CommuneService;
  let domaineProjetService: DomaineProjetService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ProjetUpdateComponent],
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
      .overrideTemplate(ProjetUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProjetUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    projetFormService = TestBed.inject(ProjetFormService);
    projetService = TestBed.inject(ProjetService);
    communeService = TestBed.inject(CommuneService);
    domaineProjetService = TestBed.inject(DomaineProjetService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Commune query and add missing value', () => {
      const projet: IProjet = { id: 456 };
      const comune: ICommune = { id: 21356 };
      projet.comune = comune;

      const communeCollection: ICommune[] = [{ id: 5557 }];
      jest.spyOn(communeService, 'query').mockReturnValue(of(new HttpResponse({ body: communeCollection })));
      const additionalCommunes = [comune];
      const expectedCollection: ICommune[] = [...additionalCommunes, ...communeCollection];
      jest.spyOn(communeService, 'addCommuneToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ projet });
      comp.ngOnInit();

      expect(communeService.query).toHaveBeenCalled();
      expect(communeService.addCommuneToCollectionIfMissing).toHaveBeenCalledWith(
        communeCollection,
        ...additionalCommunes.map(expect.objectContaining)
      );
      expect(comp.communesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call DomaineProjet query and add missing value', () => {
      const projet: IProjet = { id: 456 };
      const domaineProjet: IDomaineProjet = { id: 4268 };
      projet.domaineProjet = domaineProjet;

      const domaineProjetCollection: IDomaineProjet[] = [{ id: 35970 }];
      jest.spyOn(domaineProjetService, 'query').mockReturnValue(of(new HttpResponse({ body: domaineProjetCollection })));
      const additionalDomaineProjets = [domaineProjet];
      const expectedCollection: IDomaineProjet[] = [...additionalDomaineProjets, ...domaineProjetCollection];
      jest.spyOn(domaineProjetService, 'addDomaineProjetToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ projet });
      comp.ngOnInit();

      expect(domaineProjetService.query).toHaveBeenCalled();
      expect(domaineProjetService.addDomaineProjetToCollectionIfMissing).toHaveBeenCalledWith(
        domaineProjetCollection,
        ...additionalDomaineProjets.map(expect.objectContaining)
      );
      expect(comp.domaineProjetsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const projet: IProjet = { id: 456 };
      const comune: ICommune = { id: 41802 };
      projet.comune = comune;
      const domaineProjet: IDomaineProjet = { id: 48053 };
      projet.domaineProjet = domaineProjet;

      activatedRoute.data = of({ projet });
      comp.ngOnInit();

      expect(comp.communesSharedCollection).toContain(comune);
      expect(comp.domaineProjetsSharedCollection).toContain(domaineProjet);
      expect(comp.projet).toEqual(projet);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProjet>>();
      const projet = { id: 123 };
      jest.spyOn(projetFormService, 'getProjet').mockReturnValue(projet);
      jest.spyOn(projetService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ projet });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: projet }));
      saveSubject.complete();

      // THEN
      expect(projetFormService.getProjet).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(projetService.update).toHaveBeenCalledWith(expect.objectContaining(projet));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProjet>>();
      const projet = { id: 123 };
      jest.spyOn(projetFormService, 'getProjet').mockReturnValue({ id: null });
      jest.spyOn(projetService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ projet: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: projet }));
      saveSubject.complete();

      // THEN
      expect(projetFormService.getProjet).toHaveBeenCalled();
      expect(projetService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProjet>>();
      const projet = { id: 123 };
      jest.spyOn(projetService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ projet });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(projetService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCommune', () => {
      it('Should forward to communeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(communeService, 'compareCommune');
        comp.compareCommune(entity, entity2);
        expect(communeService.compareCommune).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareDomaineProjet', () => {
      it('Should forward to domaineProjetService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(domaineProjetService, 'compareDomaineProjet');
        comp.compareDomaineProjet(entity, entity2);
        expect(domaineProjetService.compareDomaineProjet).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
