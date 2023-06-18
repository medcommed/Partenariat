import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { PartenaireFormService, PartenaireFormGroup } from './partenaire-form.service';
import { IPartenaire } from '../partenaire.model';
import { PartenaireService } from '../service/partenaire.service';

@Component({
  selector: 'jhi-partenaire-update',
  templateUrl: './partenaire-update.component.html',
})
export class PartenaireUpdateComponent implements OnInit {
  isSaving = false;
  partenaire: IPartenaire | null = null;

  editForm: PartenaireFormGroup = this.partenaireFormService.createPartenaireFormGroup();

  constructor(
    protected partenaireService: PartenaireService,
    protected partenaireFormService: PartenaireFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ partenaire }) => {
      this.partenaire = partenaire;
      if (partenaire) {
        this.updateForm(partenaire);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const partenaire = this.partenaireFormService.getPartenaire(this.editForm);
    if (partenaire.id !== null) {
      this.subscribeToSaveResponse(this.partenaireService.update(partenaire));
    } else {
      this.subscribeToSaveResponse(this.partenaireService.create(partenaire));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPartenaire>>): void {
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

  protected updateForm(partenaire: IPartenaire): void {
    this.partenaire = partenaire;
    this.partenaireFormService.resetForm(this.editForm, partenaire);
  }
}
