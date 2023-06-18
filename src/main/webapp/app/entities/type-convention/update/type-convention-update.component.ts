import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { TypeConventionFormService, TypeConventionFormGroup } from './type-convention-form.service';
import { ITypeConvention } from '../type-convention.model';
import { TypeConventionService } from '../service/type-convention.service';

@Component({
  selector: 'jhi-type-convention-update',
  templateUrl: './type-convention-update.component.html',
})
export class TypeConventionUpdateComponent implements OnInit {
  isSaving = false;
  typeConvention: ITypeConvention | null = null;

  editForm: TypeConventionFormGroup = this.typeConventionFormService.createTypeConventionFormGroup();

  constructor(
    protected typeConventionService: TypeConventionService,
    protected typeConventionFormService: TypeConventionFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ typeConvention }) => {
      this.typeConvention = typeConvention;
      if (typeConvention) {
        this.updateForm(typeConvention);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const typeConvention = this.typeConventionFormService.getTypeConvention(this.editForm);
    if (typeConvention.id !== null) {
      this.subscribeToSaveResponse(this.typeConventionService.update(typeConvention));
    } else {
      this.subscribeToSaveResponse(this.typeConventionService.create(typeConvention));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITypeConvention>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(typeConvention: ITypeConvention): void {
    this.typeConvention = typeConvention;
    this.typeConventionFormService.resetForm(this.editForm, typeConvention);
  }
}
