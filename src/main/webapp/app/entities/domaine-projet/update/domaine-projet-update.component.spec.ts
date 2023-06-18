import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DomaineProjetFormService } from './domaine-projet-form.service';
import { DomaineProjetService } from '../service/domaine-projet.service';
import { IDomaineProjet } from '../domaine-projet.model';

import { DomaineProjetUpdateComponent } from './domaine-projet-update.component';

describe('DomaineProjet Management Update Component', () => {
  let comp: DomaineProjetUpdateComponent;
  let fixture: ComponentFixture<DomaineProjetUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let domaineProjetFormService: DomaineProjetFormService;
  let domaineProjetService: DomaineProjetService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DomaineProjetUpdateComponent],
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
      .overrideTemplate(DomaineProjetUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DomaineProjetUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    domaineProjetFormService = TestBed.inject(DomaineProjetFormService);
    domaineProjetService = TestBed.inject(DomaineProjetService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const domaineProjet: IDomaineProjet = { id: 456 };

      activatedRoute.data = of({ domaineProjet });
      comp.ngOnInit();

      expect(comp.domaineProjet).toEqual(domaineProjet);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDomaineProjet>>();
      const domaineProjet = { id: 123 };
      jest.spyOn(domaineProjetFormService, 'getDomaineProjet').mockReturnValue(domaineProjet);
      jest.spyOn(domaineProjetService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ domaineProjet });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: domaineProjet }));
      saveSubject.complete();

      // THEN
      expect(domaineProjetFormService.getDomaineProjet).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(domaineProjetService.update).toHaveBeenCalledWith(expect.objectContaining(domaineProjet));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDomaineProjet>>();
      const domaineProjet = { id: 123 };
      jest.spyOn(domaineProjetFormService, 'getDomaineProjet').mockReturnValue({ id: null });
      jest.spyOn(domaineProjetService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ domaineProjet: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: domaineProjet }));
      saveSubject.complete();

      // THEN
      expect(domaineProjetFormService.getDomaineProjet).toHaveBeenCalled();
      expect(domaineProjetService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDomaineProjet>>();
      const domaineProjet = { id: 123 };
      jest.spyOn(domaineProjetService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ domaineProjet });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(domaineProjetService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
