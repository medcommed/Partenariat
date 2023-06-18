import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICommune, NewCommune } from '../commune.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICommune for edit and NewCommuneFormGroupInput for create.
 */
type CommuneFormGroupInput = ICommune | PartialWithRequiredKeyOf<NewCommune>;

type CommuneFormDefaults = Pick<NewCommune, 'id'>;

type CommuneFormGroupContent = {
  id: FormControl<ICommune['id'] | NewCommune['id']>;
  nomCommuneAr: FormControl<ICommune['nomCommuneAr']>;
  nomCommuneFr: FormControl<ICommune['nomCommuneFr']>;
  provinces: FormControl<ICommune['provinces']>;
};

export type CommuneFormGroup = FormGroup<CommuneFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CommuneFormService {
  createCommuneFormGroup(commune: CommuneFormGroupInput = { id: null }): CommuneFormGroup {
    const communeRawValue = {
      ...this.getFormDefaults(),
      ...commune,
    };
    return new FormGroup<CommuneFormGroupContent>({
      id: new FormControl(
        { value: communeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      nomCommuneAr: new FormControl(communeRawValue.nomCommuneAr, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      nomCommuneFr: new FormControl(communeRawValue.nomCommuneFr, {
        validators: [Validators.maxLength(255)],
      }),
      provinces: new FormControl(communeRawValue.provinces),
    });
  }

  getCommune(form: CommuneFormGroup): ICommune | NewCommune {
    return form.getRawValue() as ICommune | NewCommune;
  }

  resetForm(form: CommuneFormGroup, commune: CommuneFormGroupInput): void {
    const communeRawValue = { ...this.getFormDefaults(), ...commune };
    form.reset(
      {
        ...communeRawValue,
        id: { value: communeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CommuneFormDefaults {
    return {
      id: null,
    };
  }
}
