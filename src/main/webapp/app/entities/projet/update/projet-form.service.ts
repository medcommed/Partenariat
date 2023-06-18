import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IProjet, NewProjet } from '../projet.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProjet for edit and NewProjetFormGroupInput for create.
 */
type ProjetFormGroupInput = IProjet | PartialWithRequiredKeyOf<NewProjet>;

type ProjetFormDefaults = Pick<NewProjet, 'id'>;

type ProjetFormGroupContent = {
  id: FormControl<IProjet['id'] | NewProjet['id']>;
  nomProjet: FormControl<IProjet['nomProjet']>;
  anneeProjet: FormControl<IProjet['anneeProjet']>;
  dateDebut: FormControl<IProjet['dateDebut']>;
  dureeProjet: FormControl<IProjet['dureeProjet']>;
  montantProjet: FormControl<IProjet['montantProjet']>;
  comune: FormControl<IProjet['comune']>;
  domaineProjet: FormControl<IProjet['domaineProjet']>;
};

export type ProjetFormGroup = FormGroup<ProjetFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProjetFormService {
  createProjetFormGroup(projet: ProjetFormGroupInput = { id: null }): ProjetFormGroup {
    const projetRawValue = {
      ...this.getFormDefaults(),
      ...projet,
    };
    return new FormGroup<ProjetFormGroupContent>({
      id: new FormControl(
        { value: projetRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      nomProjet: new FormControl(projetRawValue.nomProjet, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      anneeProjet: new FormControl(projetRawValue.anneeProjet, {
        validators: [Validators.required],
      }),
      dateDebut: new FormControl(projetRawValue.dateDebut, {
        validators: [Validators.required],
      }),
      dureeProjet: new FormControl(projetRawValue.dureeProjet, {
        validators: [Validators.required],
      }),
      montantProjet: new FormControl(projetRawValue.montantProjet, {
        validators: [Validators.required],
      }),
      comune: new FormControl(projetRawValue.comune),
      domaineProjet: new FormControl(projetRawValue.domaineProjet),
    });
  }

  getProjet(form: ProjetFormGroup): IProjet | NewProjet {
    return form.getRawValue() as IProjet | NewProjet;
  }

  resetForm(form: ProjetFormGroup, projet: ProjetFormGroupInput): void {
    const projetRawValue = { ...this.getFormDefaults(), ...projet };
    form.reset(
      {
        ...projetRawValue,
        id: { value: projetRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ProjetFormDefaults {
    return {
      id: null,
    };
  }
}
