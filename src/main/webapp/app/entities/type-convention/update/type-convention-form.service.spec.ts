import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../type-convention.test-samples';

import { TypeConventionFormService } from './type-convention-form.service';

describe('TypeConvention Form Service', () => {
  let service: TypeConventionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TypeConventionFormService);
  });

  describe('Service methods', () => {
    describe('createTypeConventionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTypeConventionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nomTypeAr: expect.any(Object),
            nomTypeFr: expect.any(Object),
          })
        );
      });

      it('passing ITypeConvention should create a new form with FormGroup', () => {
        const formGroup = service.createTypeConventionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nomTypeAr: expect.any(Object),
            nomTypeFr: expect.any(Object),
          })
        );
      });
    });

    describe('getTypeConvention', () => {
      it('should return NewTypeConvention for default TypeConvention initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createTypeConventionFormGroup(sampleWithNewData);

        const typeConvention = service.getTypeConvention(formGroup) as any;

        expect(typeConvention).toMatchObject(sampleWithNewData);
      });

      it('should return NewTypeConvention for empty TypeConvention initial value', () => {
        const formGroup = service.createTypeConventionFormGroup();

        const typeConvention = service.getTypeConvention(formGroup) as any;

        expect(typeConvention).toMatchObject({});
      });

      it('should return ITypeConvention', () => {
        const formGroup = service.createTypeConventionFormGroup(sampleWithRequiredData);

        const typeConvention = service.getTypeConvention(formGroup) as any;

        expect(typeConvention).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITypeConvention should not enable id FormControl', () => {
        const formGroup = service.createTypeConventionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTypeConvention should disable id FormControl', () => {
        const formGroup = service.createTypeConventionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
