import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ProjetFormService, ProjetFormGroup } from './projet-form.service';
import { IProjet } from '../projet.model';
import { ProjetService } from '../service/projet.service';
import { ICommune } from 'app/entities/commune/commune.model';
import { CommuneService } from 'app/entities/commune/service/commune.service';
import { IDomaineProjet } from 'app/entities/domaine-projet/domaine-projet.model';
import { DomaineProjetService } from 'app/entities/domaine-projet/service/domaine-projet.service';

@Component({
  selector: 'jhi-projet-update',
  templateUrl: './projet-update.component.html',
})
export class ProjetUpdateComponent implements OnInit {
  isSaving = false;
  projet: IProjet | null = null;

  communesSharedCollection: ICommune[] = [];
  domaineProjetsSharedCollection: IDomaineProjet[] = [];

  editForm: ProjetFormGroup = this.projetFormService.createProjetFormGroup();

  constructor(
    protected projetService: ProjetService,
    protected projetFormService: ProjetFormService,
    protected communeService: CommuneService,
    protected domaineProjetService: DomaineProjetService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareCommune = (o1: ICommune | null, o2: ICommune | null): boolean => this.communeService.compareCommune(o1, o2);

  compareDomaineProjet = (o1: IDomaineProjet | null, o2: IDomaineProjet | null): boolean =>
    this.domaineProjetService.compareDomaineProjet(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ projet }) => {
      this.projet = projet;
      if (projet) {
        this.updateForm(projet);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const projet = this.projetFormService.getProjet(this.editForm);
    if (projet.id !== null) {
      this.subscribeToSaveResponse(this.projetService.update(projet));
    } else {
      this.subscribeToSaveResponse(this.projetService.create(projet));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProjet>>): void {
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

  protected updateForm(projet: IProjet): void {
    this.projet = projet;
    this.projetFormService.resetForm(this.editForm, projet);

    this.communesSharedCollection = this.communeService.addCommuneToCollectionIfMissing<ICommune>(
      this.communesSharedCollection,
      projet.comune
    );
    this.domaineProjetsSharedCollection = this.domaineProjetService.addDomaineProjetToCollectionIfMissing<IDomaineProjet>(
      this.domaineProjetsSharedCollection,
      projet.domaineProjet
    );
  }

  protected loadRelationshipsOptions(): void {
    this.communeService
      .query()
      .pipe(map((res: HttpResponse<ICommune[]>) => res.body ?? []))
      .pipe(map((communes: ICommune[]) => this.communeService.addCommuneToCollectionIfMissing<ICommune>(communes, this.projet?.comune)))
      .subscribe((communes: ICommune[]) => (this.communesSharedCollection = communes));

    this.domaineProjetService
      .query()
      .pipe(map((res: HttpResponse<IDomaineProjet[]>) => res.body ?? []))
      .pipe(
        map((domaineProjets: IDomaineProjet[]) =>
          this.domaineProjetService.addDomaineProjetToCollectionIfMissing<IDomaineProjet>(domaineProjets, this.projet?.domaineProjet)
        )
      )
      .subscribe((domaineProjets: IDomaineProjet[]) => (this.domaineProjetsSharedCollection = domaineProjets));
  }
}
