import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../tranche.test-samples';

import { TrancheFormService } from './tranche-form.service';

describe('Tranche Form Service', () => {
  let service: TrancheFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TrancheFormService);
  });

  describe('Service methods', () => {
    describe('createTrancheFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTrancheFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nomTranche: expect.any(Object),
            dateDeffet: expect.any(Object),
            montantTranche: expect.any(Object),
            rapportTranche: expect.any(Object),
            projet: expect.any(Object),
          })
        );
      });

      it('passing ITranche should create a new form with FormGroup', () => {
        const formGroup = service.createTrancheFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nomTranche: expect.any(Object),
            dateDeffet: expect.any(Object),
            montantTranche: expect.any(Object),
            rapportTranche: expect.any(Object),
            projet: expect.any(Object),
          })
        );
      });
    });

    describe('getTranche', () => {
      it('should return NewTranche for default Tranche initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createTrancheFormGroup(sampleWithNewData);

        const tranche = service.getTranche(formGroup) as any;

        expect(tranche).toMatchObject(sampleWithNewData);
      });

      it('should return NewTranche for empty Tranche initial value', () => {
        const formGroup = service.createTrancheFormGroup();

        const tranche = service.getTranche(formGroup) as any;

        expect(tranche).toMatchObject({});
      });

      it('should return ITranche', () => {
        const formGroup = service.createTrancheFormGroup(sampleWithRequiredData);

        const tranche = service.getTranche(formGroup) as any;

        expect(tranche).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITranche should not enable id FormControl', () => {
        const formGroup = service.createTrancheFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTranche should disable id FormControl', () => {
        const formGroup = service.createTrancheFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
