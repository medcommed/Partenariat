import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { DomaineProjetFormService, DomaineProjetFormGroup } from './domaine-projet-form.service';
import { IDomaineProjet } from '../domaine-projet.model';
import { DomaineProjetService } from '../service/domaine-projet.service';

@Component({
  selector: 'jhi-domaine-projet-update',
  templateUrl: './domaine-projet-update.component.html',
})
export class DomaineProjetUpdateComponent implements OnInit {
  isSaving = false;
  domaineProjet: IDomaineProjet | null = null;

  editForm: DomaineProjetFormGroup = this.domaineProjetFormService.createDomaineProjetFormGroup();

  constructor(
    protected domaineProjetService: DomaineProjetService,
    protected domaineProjetFormService: DomaineProjetFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ domaineProjet }) => {
      this.domaineProjet = domaineProjet;
      if (domaineProjet) {
        this.updateForm(domaineProjet);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const domaineProjet = this.domaineProjetFormService.getDomaineProjet(this.editForm);
    if (domaineProjet.id !== null) {
      this.subscribeToSaveResponse(this.domaineProjetService.update(domaineProjet));
    } else {
      this.subscribeToSaveResponse(this.domaineProjetService.create(domaineProjet));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDomaineProjet>>): void {
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

  protected updateForm(domaineProjet: IDomaineProjet): void {
    this.domaineProjet = domaineProjet;
    this.domaineProjetFormService.resetForm(this.editForm, domaineProjet);
  }
}
