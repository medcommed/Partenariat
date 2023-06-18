import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ISituationProjet, NewSituationProjet } from '../situation-projet.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISituationProjet for edit and NewSituationProjetFormGroupInput for create.
 */
type SituationProjetFormGroupInput = ISituationProjet | PartialWithRequiredKeyOf<NewSituationProjet>;

type SituationProjetFormDefaults = Pick<NewSituationProjet, 'id'>;

type SituationProjetFormGroupContent = {
  id: FormControl<ISituationProjet['id'] | NewSituationProjet['id']>;
  dateStatutValid: FormControl<ISituationProjet['dateStatutValid']>;
  statutProjet: FormControl<ISituationProjet['statutProjet']>;
  projet: FormControl<ISituationProjet['projet']>;
};

export type SituationProjetFormGroup = FormGroup<SituationProjetFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SituationProjetFormService {
  createSituationProjetFormGroup(situationProjet: SituationProjetFormGroupInput = { id: null }): SituationProjetFormGroup {
    const situationProjetRawValue = {
      ...this.getFormDefaults(),
      ...situationProjet,
    };
    return new FormGroup<SituationProjetFormGroupContent>({
      id: new FormControl(
        { value: situationProjetRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      dateStatutValid: new FormControl(situationProjetRawValue.dateStatutValid, {
        validators: [Validators.required],
      }),
      statutProjet: new FormControl(situationProjetRawValue.statutProjet, {
        validators: [Validators.required],
      }),
      projet: new FormControl(situationProjetRawValue.projet, {
        validators: [Validators.required],
      }),
    });
  }

  getSituationProjet(form: SituationProjetFormGroup): ISituationProjet | NewSituationProjet {
    return form.getRawValue() as ISituationProjet | NewSituationProjet;
  }

  resetForm(form: SituationProjetFormGroup, situationProjet: SituationProjetFormGroupInput): void {
    const situationProjetRawValue = { ...this.getFormDefaults(), ...situationProjet };
    form.reset(
      {
        ...situationProjetRawValue,
        id: { value: situationProjetRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): SituationProjetFormDefaults {
    return {
      id: null,
    };
  }
}
