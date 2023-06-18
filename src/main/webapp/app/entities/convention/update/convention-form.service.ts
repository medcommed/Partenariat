import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IConvention, NewConvention } from '../convention.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IConvention for edit and NewConventionFormGroupInput for create.
 */
type ConventionFormGroupInput = IConvention | PartialWithRequiredKeyOf<NewConvention>;

type ConventionFormDefaults = Pick<NewConvention, 'id' | 'partenaires'>;

type ConventionFormGroupContent = {
  id: FormControl<IConvention['id'] | NewConvention['id']>;
  aveanau: FormControl<IConvention['aveanau']>;
  dateDebutConv: FormControl<IConvention['dateDebutConv']>;
  dureeConvention: FormControl<IConvention['dureeConvention']>;
  etatConvention: FormControl<IConvention['etatConvention']>;
  nbrTranche: FormControl<IConvention['nbrTranche']>;
  nomConvention: FormControl<IConvention['nomConvention']>;
  objectif: FormControl<IConvention['objectif']>;
  projet: FormControl<IConvention['projet']>;
  typeConvention: FormControl<IConvention['typeConvention']>;
  partenaires: FormControl<IConvention['partenaires']>;
};

export type ConventionFormGroup = FormGroup<ConventionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ConventionFormService {
  createConventionFormGroup(convention: ConventionFormGroupInput = { id: null }): ConventionFormGroup {
    const conventionRawValue = {
      ...this.getFormDefaults(),
      ...convention,
    };
    return new FormGroup<ConventionFormGroupContent>({
      id: new FormControl(
        { value: conventionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      aveanau: new FormControl(conventionRawValue.aveanau, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      dateDebutConv: new FormControl(conventionRawValue.dateDebutConv, {
        validators: [Validators.required],
      }),
      dureeConvention: new FormControl(conventionRawValue.dureeConvention, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      etatConvention: new FormControl(conventionRawValue.etatConvention, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      nbrTranche: new FormControl(conventionRawValue.nbrTranche, {
        validators: [Validators.required],
      }),
      nomConvention: new FormControl(conventionRawValue.nomConvention, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      objectif: new FormControl(conventionRawValue.objectif, {
        validators: [Validators.required],
      }),
      projet: new FormControl(conventionRawValue.projet, {
        validators: [Validators.required],
      }),
      typeConvention: new FormControl(conventionRawValue.typeConvention, {
        validators: [Validators.required],
      }),
      partenaires: new FormControl(conventionRawValue.partenaires ?? []),
    });
  }

  getConvention(form: ConventionFormGroup): IConvention | NewConvention {
    return form.getRawValue() as IConvention | NewConvention;
  }

  resetForm(form: ConventionFormGroup, convention: ConventionFormGroupInput): void {
    const conventionRawValue = { ...this.getFormDefaults(), ...convention };
    form.reset(
      {
        ...conventionRawValue,
        id: { value: conventionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ConventionFormDefaults {
    return {
      id: null,
      partenaires: [],
    };
  }
}
