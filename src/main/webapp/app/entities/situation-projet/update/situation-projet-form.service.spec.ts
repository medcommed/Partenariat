import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../situation-projet.test-samples';

import { SituationProjetFormService } from './situation-projet-form.service';

describe('SituationProjet Form Service', () => {
  let service: SituationProjetFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SituationProjetFormService);
  });

  describe('Service methods', () => {
    describe('createSituationProjetFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSituationProjetFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dateStatutValid: expect.any(Object),
            statutProjet: expect.any(Object),
            projet: expect.any(Object),
          })
        );
      });

      it('passing ISituationProjet should create a new form with FormGroup', () => {
        const formGroup = service.createSituationProjetFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dateStatutValid: expect.any(Object),
            statutProjet: expect.any(Object),
            projet: expect.any(Object),
          })
        );
      });
    });

    describe('getSituationProjet', () => {
      it('should return NewSituationProjet for default SituationProjet initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createSituationProjetFormGroup(sampleWithNewData);

        const situationProjet = service.getSituationProjet(formGroup) as any;

        expect(situationProjet).toMatchObject(sampleWithNewData);
      });

      it('should return NewSituationProjet for empty SituationProjet initial value', () => {
        const formGroup = service.createSituationProjetFormGroup();

        const situationProjet = service.getSituationProjet(formGroup) as any;

        expect(situationProjet).toMatchObject({});
      });

      it('should return ISituationProjet', () => {
        const formGroup = service.createSituationProjetFormGroup(sampleWithRequiredData);

        const situationProjet = service.getSituationProjet(formGroup) as any;

        expect(situationProjet).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISituationProjet should not enable id FormControl', () => {
        const formGroup = service.createSituationProjetFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSituationProjet should disable id FormControl', () => {
        const formGroup = service.createSituationProjetFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
