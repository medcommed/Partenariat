import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IDomaineProjet, NewDomaineProjet } from '../domaine-projet.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDomaineProjet for edit and NewDomaineProjetFormGroupInput for create.
 */
type DomaineProjetFormGroupInput = IDomaineProjet | PartialWithRequiredKeyOf<NewDomaineProjet>;

type DomaineProjetFormDefaults = Pick<NewDomaineProjet, 'id'>;

type DomaineProjetFormGroupContent = {
  id: FormControl<IDomaineProjet['id'] | NewDomaineProjet['id']>;
  designationAr: FormControl<IDomaineProjet['designationAr']>;
  designationFr: FormControl<IDomaineProjet['designationFr']>;
};

export type DomaineProjetFormGroup = FormGroup<DomaineProjetFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DomaineProjetFormService {
  createDomaineProjetFormGroup(domaineProjet: DomaineProjetFormGroupInput = { id: null }): DomaineProjetFormGroup {
    const domaineProjetRawValue = {
      ...this.getFormDefaults(),
      ...domaineProjet,
    };
    return new FormGroup<DomaineProjetFormGroupContent>({
      id: new FormControl(
        { value: domaineProjetRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      designationAr: new FormControl(domaineProjetRawValue.designationAr, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      designationFr: new FormControl(domaineProjetRawValue.designationFr, {
        validators: [Validators.maxLength(255)],
      }),
    });
  }

  getDomaineProjet(form: DomaineProjetFormGroup): IDomaineProjet | NewDomaineProjet {
    return form.getRawValue() as IDomaineProjet | NewDomaineProjet;
  }

  resetForm(form: DomaineProjetFormGroup, domaineProjet: DomaineProjetFormGroupInput): void {
    const domaineProjetRawValue = { ...this.getFormDefaults(), ...domaineProjet };
    form.reset(
      {
        ...domaineProjetRawValue,
        id: { value: domaineProjetRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): DomaineProjetFormDefaults {
    return {
      id: null,
    };
  }
}
