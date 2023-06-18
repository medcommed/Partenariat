import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CommuneFormService } from './commune-form.service';
import { CommuneService } from '../service/commune.service';
import { ICommune } from '../commune.model';
import { IProvince } from 'app/entities/province/province.model';
import { ProvinceService } from 'app/entities/province/service/province.service';

import { CommuneUpdateComponent } from './commune-update.component';

describe('Commune Management Update Component', () => {
  let comp: CommuneUpdateComponent;
  let fixture: ComponentFixture<CommuneUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let communeFormService: CommuneFormService;
  let communeService: CommuneService;
  let provinceService: ProvinceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CommuneUpdateComponent],
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
      .overrideTemplate(CommuneUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CommuneUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    communeFormService = TestBed.inject(CommuneFormService);
    communeService = TestBed.inject(CommuneService);
    provinceService = TestBed.inject(ProvinceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Province query and add missing value', () => {
      const commune: ICommune = { id: 456 };
      const provinces: IProvince = { id: 64722 };
      commune.provinces = provinces;

      const provinceCollection: IProvince[] = [{ id: 55145 }];
      jest.spyOn(provinceService, 'query').mockReturnValue(of(new HttpResponse({ body: provinceCollection })));
      const additionalProvinces = [provinces];
      const expectedCollection: IProvince[] = [...additionalProvinces, ...provinceCollection];
      jest.spyOn(provinceService, 'addProvinceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ commune });
      comp.ngOnInit();

      expect(provinceService.query).toHaveBeenCalled();
      expect(provinceService.addProvinceToCollectionIfMissing).toHaveBeenCalledWith(
        provinceCollection,
        ...additionalProvinces.map(expect.objectContaining)
      );
      expect(comp.provincesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const commune: ICommune = { id: 456 };
      const provinces: IProvince = { id: 61213 };
      commune.provinces = provinces;

      activatedRoute.data = of({ commune });
      comp.ngOnInit();

      expect(comp.provincesSharedCollection).toContain(provinces);
      expect(comp.commune).toEqual(commune);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICommune>>();
      const commune = { id: 123 };
      jest.spyOn(communeFormService, 'getCommune').mockReturnValue(commune);
      jest.spyOn(communeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ commune });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: commune }));
      saveSubject.complete();

      // THEN
      expect(communeFormService.getCommune).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(communeService.update).toHaveBeenCalledWith(expect.objectContaining(commune));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICommune>>();
      const commune = { id: 123 };
      jest.spyOn(communeFormService, 'getCommune').mockReturnValue({ id: null });
      jest.spyOn(communeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ commune: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: commune }));
      saveSubject.complete();

      // THEN
      expect(communeFormService.getCommune).toHaveBeenCalled();
      expect(communeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICommune>>();
      const commune = { id: 123 };
      jest.spyOn(communeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ commune });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(communeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareProvince', () => {
      it('Should forward to provinceService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(provinceService, 'compareProvince');
        comp.compareProvince(entity, entity2);
        expect(provinceService.compareProvince).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
