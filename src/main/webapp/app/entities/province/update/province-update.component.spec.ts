import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ProvinceFormService } from './province-form.service';
import { ProvinceService } from '../service/province.service';
import { IProvince } from '../province.model';
import { IRegion } from 'app/entities/region/region.model';
import { RegionService } from 'app/entities/region/service/region.service';

import { ProvinceUpdateComponent } from './province-update.component';

describe('Province Management Update Component', () => {
  let comp: ProvinceUpdateComponent;
  let fixture: ComponentFixture<ProvinceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let provinceFormService: ProvinceFormService;
  let provinceService: ProvinceService;
  let regionService: RegionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ProvinceUpdateComponent],
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
      .overrideTemplate(ProvinceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProvinceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    provinceFormService = TestBed.inject(ProvinceFormService);
    provinceService = TestBed.inject(ProvinceService);
    regionService = TestBed.inject(RegionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Region query and add missing value', () => {
      const province: IProvince = { id: 456 };
      const region: IRegion = { id: 81212 };
      province.region = region;

      const regionCollection: IRegion[] = [{ id: 26943 }];
      jest.spyOn(regionService, 'query').mockReturnValue(of(new HttpResponse({ body: regionCollection })));
      const additionalRegions = [region];
      const expectedCollection: IRegion[] = [...additionalRegions, ...regionCollection];
      jest.spyOn(regionService, 'addRegionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ province });
      comp.ngOnInit();

      expect(regionService.query).toHaveBeenCalled();
      expect(regionService.addRegionToCollectionIfMissing).toHaveBeenCalledWith(
        regionCollection,
        ...additionalRegions.map(expect.objectContaining)
      );
      expect(comp.regionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const province: IProvince = { id: 456 };
      const region: IRegion = { id: 92166 };
      province.region = region;

      activatedRoute.data = of({ province });
      comp.ngOnInit();

      expect(comp.regionsSharedCollection).toContain(region);
      expect(comp.province).toEqual(province);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProvince>>();
      const province = { id: 123 };
      jest.spyOn(provinceFormService, 'getProvince').mockReturnValue(province);
      jest.spyOn(provinceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ province });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: province }));
      saveSubject.complete();

      // THEN
      expect(provinceFormService.getProvince).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(provinceService.update).toHaveBeenCalledWith(expect.objectContaining(province));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProvince>>();
      const province = { id: 123 };
      jest.spyOn(provinceFormService, 'getProvince').mockReturnValue({ id: null });
      jest.spyOn(provinceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ province: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: province }));
      saveSubject.complete();

      // THEN
      expect(provinceFormService.getProvince).toHaveBeenCalled();
      expect(provinceService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProvince>>();
      const province = { id: 123 };
      jest.spyOn(provinceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ province });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(provinceService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareRegion', () => {
      it('Should forward to regionService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(regionService, 'compareRegion');
        comp.compareRegion(entity, entity2);
        expect(regionService.compareRegion).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
