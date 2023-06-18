import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../commune.test-samples';

import { CommuneFormService } from './commune-form.service';

describe('Commune Form Service', () => {
  let service: CommuneFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CommuneFormService);
  });

  describe('Service methods', () => {
    describe('createCommuneFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCommuneFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nomCommuneAr: expect.any(Object),
            nomCommuneFr: expect.any(Object),
            provinces: expect.any(Object),
          })
        );
      });

      it('passing ICommune should create a new form with FormGroup', () => {
        const formGroup = service.createCommuneFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nomCommuneAr: expect.any(Object),
            nomCommuneFr: expect.any(Object),
            provinces: expect.any(Object),
          })
        );
      });
    });

    describe('getCommune', () => {
      it('should return NewCommune for default Commune initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createCommuneFormGroup(sampleWithNewData);

        const commune = service.getCommune(formGroup) as any;

        expect(commune).toMatchObject(sampleWithNewData);
      });

      it('should return NewCommune for empty Commune initial value', () => {
        const formGroup = service.createCommuneFormGroup();

        const commune = service.getCommune(formGroup) as any;

        expect(commune).toMatchObject({});
      });

      it('should return ICommune', () => {
        const formGroup = service.createCommuneFormGroup(sampleWithRequiredData);

        const commune = service.getCommune(formGroup) as any;

        expect(commune).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICommune should not enable id FormControl', () => {
        const formGroup = service.createCommuneFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCommune should disable id FormControl', () => {
        const formGroup = service.createCommuneFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
