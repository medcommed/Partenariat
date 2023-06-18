import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../convention.test-samples';

import { ConventionFormService } from './convention-form.service';

describe('Convention Form Service', () => {
  let service: ConventionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ConventionFormService);
  });

  describe('Service methods', () => {
    describe('createConventionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createConventionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            aveanau: expect.any(Object),
            dateDebutConv: expect.any(Object),
            dureeConvention: expect.any(Object),
            etatConvention: expect.any(Object),
            nbrTranche: expect.any(Object),
            nomConvention: expect.any(Object),
            objectif: expect.any(Object),
            projet: expect.any(Object),
            typeConvention: expect.any(Object),
            partenaires: expect.any(Object),
          })
        );
      });

      it('passing IConvention should create a new form with FormGroup', () => {
        const formGroup = service.createConventionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            aveanau: expect.any(Object),
            dateDebutConv: expect.any(Object),
            dureeConvention: expect.any(Object),
            etatConvention: expect.any(Object),
            nbrTranche: expect.any(Object),
            nomConvention: expect.any(Object),
            objectif: expect.any(Object),
            projet: expect.any(Object),
            typeConvention: expect.any(Object),
            partenaires: expect.any(Object),
          })
        );
      });
    });

    describe('getConvention', () => {
      it('should return NewConvention for default Convention initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createConventionFormGroup(sampleWithNewData);

        const convention = service.getConvention(formGroup) as any;

        expect(convention).toMatchObject(sampleWithNewData);
      });

      it('should return NewConvention for empty Convention initial value', () => {
        const formGroup = service.createConventionFormGroup();

        const convention = service.getConvention(formGroup) as any;

        expect(convention).toMatchObject({});
      });

      it('should return IConvention', () => {
        const formGroup = service.createConventionFormGroup(sampleWithRequiredData);

        const convention = service.getConvention(formGroup) as any;

        expect(convention).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IConvention should not enable id FormControl', () => {
        const formGroup = service.createConventionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewConvention should disable id FormControl', () => {
        const formGroup = service.createConventionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
