import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../domaine-projet.test-samples';

import { DomaineProjetFormService } from './domaine-projet-form.service';

describe('DomaineProjet Form Service', () => {
  let service: DomaineProjetFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DomaineProjetFormService);
  });

  describe('Service methods', () => {
    describe('createDomaineProjetFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDomaineProjetFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            designationAr: expect.any(Object),
            designationFr: expect.any(Object),
          })
        );
      });

      it('passing IDomaineProjet should create a new form with FormGroup', () => {
        const formGroup = service.createDomaineProjetFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            designationAr: expect.any(Object),
            designationFr: expect.any(Object),
          })
        );
      });
    });

    describe('getDomaineProjet', () => {
      it('should return NewDomaineProjet for default DomaineProjet initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createDomaineProjetFormGroup(sampleWithNewData);

        const domaineProjet = service.getDomaineProjet(formGroup) as any;

        expect(domaineProjet).toMatchObject(sampleWithNewData);
      });

      it('should return NewDomaineProjet for empty DomaineProjet initial value', () => {
        const formGroup = service.createDomaineProjetFormGroup();

        const domaineProjet = service.getDomaineProjet(formGroup) as any;

        expect(domaineProjet).toMatchObject({});
      });

      it('should return IDomaineProjet', () => {
        const formGroup = service.createDomaineProjetFormGroup(sampleWithRequiredData);

        const domaineProjet = service.getDomaineProjet(formGroup) as any;

        expect(domaineProjet).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDomaineProjet should not enable id FormControl', () => {
        const formGroup = service.createDomaineProjetFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDomaineProjet should disable id FormControl', () => {
        const formGroup = service.createDomaineProjetFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
