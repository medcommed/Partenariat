import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TypeConventionFormService } from './type-convention-form.service';
import { TypeConventionService } from '../service/type-convention.service';
import { ITypeConvention } from '../type-convention.model';

import { TypeConventionUpdateComponent } from './type-convention-update.component';

describe('TypeConvention Management Update Component', () => {
  let comp: TypeConventionUpdateComponent;
  let fixture: ComponentFixture<TypeConventionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let typeConventionFormService: TypeConventionFormService;
  let typeConventionService: TypeConventionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TypeConventionUpdateComponent],
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
      .overrideTemplate(TypeConventionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TypeConventionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    typeConventionFormService = TestBed.inject(TypeConventionFormService);
    typeConventionService = TestBed.inject(TypeConventionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const typeConvention: ITypeConvention = { id: 456 };

      activatedRoute.data = of({ typeConvention });
      comp.ngOnInit();

      expect(comp.typeConvention).toEqual(typeConvention);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITypeConvention>>();
      const typeConvention = { id: 123 };
      jest.spyOn(typeConventionFormService, 'getTypeConvention').mockReturnValue(typeConvention);
      jest.spyOn(typeConventionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeConvention });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: typeConvention }));
      saveSubject.complete();

      // THEN
      expect(typeConventionFormService.getTypeConvention).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(typeConventionService.update).toHaveBeenCalledWith(expect.objectContaining(typeConvention));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITypeConvention>>();
      const typeConvention = { id: 123 };
      jest.spyOn(typeConventionFormService, 'getTypeConvention').mockReturnValue({ id: null });
      jest.spyOn(typeConventionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeConvention: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: typeConvention }));
      saveSubject.complete();

      // THEN
      expect(typeConventionFormService.getTypeConvention).toHaveBeenCalled();
      expect(typeConventionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITypeConvention>>();
      const typeConvention = { id: 123 };
      jest.spyOn(typeConventionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeConvention });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(typeConventionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
