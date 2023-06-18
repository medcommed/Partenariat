import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { TrancheFormService, TrancheFormGroup } from './tranche-form.service';
import { ITranche } from '../tranche.model';
import { TrancheService } from '../service/tranche.service';
import { IProjet } from 'app/entities/projet/projet.model';
import { ProjetService } from 'app/entities/projet/service/projet.service';

@Component({
  selector: 'jhi-tranche-update',
  templateUrl: './tranche-update.component.html',
})
export class TrancheUpdateComponent implements OnInit {
  isSaving = false;
  tranche: ITranche | null = null;

  projetsSharedCollection: IProjet[] = [];

  editForm: TrancheFormGroup = this.trancheFormService.createTrancheFormGroup();

  constructor(
    protected trancheService: TrancheService,
    protected trancheFormService: TrancheFormService,
    protected projetService: ProjetService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareProjet = (o1: IProjet | null, o2: IProjet | null): boolean => this.projetService.compareProjet(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tranche }) => {
      this.tranche = tranche;
      if (tranche) {
        this.updateForm(tranche);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tranche = this.trancheFormService.getTranche(this.editForm);
    if (tranche.id !== null) {
      this.subscribeToSaveResponse(this.trancheService.update(tranche));
    } else {
      this.subscribeToSaveResponse(this.trancheService.create(tranche));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITranche>>): void {
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

  protected updateForm(tranche: ITranche): void {
    this.tranche = tranche;
    this.trancheFormService.resetForm(this.editForm, tranche);

    this.projetsSharedCollection = this.projetService.addProjetToCollectionIfMissing<IProjet>(this.projetsSharedCollection, tranche.projet);
  }

  protected loadRelationshipsOptions(): void {
    this.projetService
      .query()
      .pipe(map((res: HttpResponse<IProjet[]>) => res.body ?? []))
      .pipe(map((projets: IProjet[]) => this.projetService.addProjetToCollectionIfMissing<IProjet>(projets, this.tranche?.projet)))
      .subscribe((projets: IProjet[]) => (this.projetsSharedCollection = projets));
  }
}
