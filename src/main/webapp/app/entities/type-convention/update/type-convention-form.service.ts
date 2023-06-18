import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITypeConvention, NewTypeConvention } from '../type-convention.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITypeConvention for edit and NewTypeConventionFormGroupInput for create.
 */
type TypeConventionFormGroupInput = ITypeConvention | PartialWithRequiredKeyOf<NewTypeConvention>;

type TypeConventionFormDefaults = Pick<NewTypeConvention, 'id'>;

type TypeConventionFormGroupContent = {
  id: FormControl<ITypeConvention['id'] | NewTypeConvention['id']>;
  nomTypeAr: FormControl<ITypeConvention['nomTypeAr']>;
  nomTypeFr: FormControl<ITypeConvention['nomTypeFr']>;
};

export type TypeConventionFormGroup = FormGroup<TypeConventionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TypeConventionFormService {
  createTypeConventionFormGroup(typeConvention: TypeConventionFormGroupInput = { id: null }): TypeConventionFormGroup {
    const typeConventionRawValue = {
      ...this.getFormDefaults(),
      ...typeConvention,
    };
    return new FormGroup<TypeConventionFormGroupContent>({
      id: new FormControl(
        { value: typeConventionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      nomTypeAr: new FormControl(typeConventionRawValue.nomTypeAr, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      nomTypeFr: new FormControl(typeConventionRawValue.nomTypeFr, {
        validators: [Validators.maxLength(255)],
      }),
    });
  }

  getTypeConvention(form: TypeConventionFormGroup): ITypeConvention | NewTypeConvention {
    return form.getRawValue() as ITypeConvention | NewTypeConvention;
  }

  resetForm(form: TypeConventionFormGroup, typeConvention: TypeConventionFormGroupInput): void {
    const typeConventionRawValue = { ...this.getFormDefaults(), ...typeConvention };
    form.reset(
      {
        ...typeConventionRawValue,
        id: { value: typeConventionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): TypeConventionFormDefaults {
    return {
      id: null,
    };
  }
}
