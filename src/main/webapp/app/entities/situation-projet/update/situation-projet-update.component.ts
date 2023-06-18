import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { SituationProjetFormService, SituationProjetFormGroup } from './situation-projet-form.service';
import { ISituationProjet } from '../situation-projet.model';
import { SituationProjetService } from '../service/situation-projet.service';
import { IProjet } from 'app/entities/projet/projet.model';
import { ProjetService } from 'app/entities/projet/service/projet.service';

@Component({
  selector: 'jhi-situation-projet-update',
  templateUrl: './situation-projet-update.component.html',
})
export class SituationProjetUpdateComponent implements OnInit {
  isSaving = false;
  situationProjet: ISituationProjet | null = null;

  projetsSharedCollection: IProjet[] = [];

  editForm: SituationProjetFormGroup = this.situationProjetFormService.createSituationProjetFormGroup();

  constructor(
    protected situationProjetService: SituationProjetService,
    protected situationProjetFormService: SituationProjetFormService,
    protected projetService: ProjetService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareProjet = (o1: IProjet | null, o2: IProjet | null): boolean => this.projetService.compareProjet(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ situationProjet }) => {
      this.situationProjet = situationProjet;
      if (situationProjet) {
        this.updateForm(situationProjet);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const situationProjet = this.situationProjetFormService.getSituationProjet(this.editForm);
    if (situationProjet.id !== null) {
      this.subscribeToSaveResponse(this.situationProjetService.update(situationProjet));
    } else {
      this.subscribeToSaveResponse(this.situationProjetService.create(situationProjet));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISituationProjet>>): void {
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

  protected updateForm(situationProjet: ISituationProjet): void {
    this.situationProjet = situationProjet;
    this.situationProjetFormService.resetForm(this.editForm, situationProjet);

    this.projetsSharedCollection = this.projetService.addProjetToCollectionIfMissing<IProjet>(
      this.projetsSharedCollection,
      situationProjet.projet
    );
  }

  protected loadRelationshipsOptions(): void {
    this.projetService
      .query()
      .pipe(map((res: HttpResponse<IProjet[]>) => res.body ?? []))
      .pipe(map((projets: IProjet[]) => this.projetService.addProjetToCollectionIfMissing<IProjet>(projets, this.situationProjet?.projet)))
      .subscribe((projets: IProjet[]) => (this.projetsSharedCollection = projets));
  }
}
