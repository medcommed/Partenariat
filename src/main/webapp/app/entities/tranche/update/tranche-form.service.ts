import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITranche, NewTranche } from '../tranche.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITranche for edit and NewTrancheFormGroupInput for create.
 */
type TrancheFormGroupInput = ITranche | PartialWithRequiredKeyOf<NewTranche>;

type TrancheFormDefaults = Pick<NewTranche, 'id'>;

type TrancheFormGroupContent = {
  id: FormControl<ITranche['id'] | NewTranche['id']>;
  nomTranche: FormControl<ITranche['nomTranche']>;
  dateDeffet: FormControl<ITranche['dateDeffet']>;
  montantTranche: FormControl<ITranche['montantTranche']>;
  rapportTranche: FormControl<ITranche['rapportTranche']>;
  projet: FormControl<ITranche['projet']>;
};

export type TrancheFormGroup = FormGroup<TrancheFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TrancheFormService {
  createTrancheFormGroup(tranche: TrancheFormGroupInput = { id: null }): TrancheFormGroup {
    const trancheRawValue = {
      ...this.getFormDefaults(),
      ...tranche,
    };
    return new FormGroup<TrancheFormGroupContent>({
      id: new FormControl(
        { value: trancheRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      nomTranche: new FormControl(trancheRawValue.nomTranche, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      dateDeffet: new FormControl(trancheRawValue.dateDeffet, {
        validators: [Validators.required],
      }),
      montantTranche: new FormControl(trancheRawValue.montantTranche, {
        validators: [Validators.required],
      }),
      rapportTranche: new FormControl(trancheRawValue.rapportTranche, {
        validators: [Validators.required],
      }),
      projet: new FormControl(trancheRawValue.projet, {
        validators: [Validators.required],
      }),
    });
  }

  getTranche(form: TrancheFormGroup): ITranche | NewTranche {
    return form.getRawValue() as ITranche | NewTranche;
  }

  resetForm(form: TrancheFormGroup, tranche: TrancheFormGroupInput): void {
    const trancheRawValue = { ...this.getFormDefaults(), ...tranche };
    form.reset(
      {
        ...trancheRawValue,
        id: { value: trancheRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): TrancheFormDefaults {
    return {
      id: null,
    };
  }
}
