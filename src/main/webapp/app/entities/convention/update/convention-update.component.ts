import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ConventionFormService, ConventionFormGroup } from './convention-form.service';
import { IConvention } from '../convention.model';
import { ConventionService } from '../service/convention.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IProjet } from 'app/entities/projet/projet.model';
import { ProjetService } from 'app/entities/projet/service/projet.service';
import { ITypeConvention } from 'app/entities/type-convention/type-convention.model';
import { TypeConventionService } from 'app/entities/type-convention/service/type-convention.service';
import { IPartenaire } from 'app/entities/partenaire/partenaire.model';
import { PartenaireService } from 'app/entities/partenaire/service/partenaire.service';

@Component({
  selector: 'jhi-convention-update',
  templateUrl: './convention-update.component.html',
})
export class ConventionUpdateComponent implements OnInit {
  isSaving = false;
  convention: IConvention | null = null;

  projetsSharedCollection: IProjet[] = [];
  typeConventionsSharedCollection: ITypeConvention[] = [];
  partenairesSharedCollection: IPartenaire[] = [];

  editForm: ConventionFormGroup = this.conventionFormService.createConventionFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected conventionService: ConventionService,
    protected conventionFormService: ConventionFormService,
    protected projetService: ProjetService,
    protected typeConventionService: TypeConventionService,
    protected partenaireService: PartenaireService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareProjet = (o1: IProjet | null, o2: IProjet | null): boolean => this.projetService.compareProjet(o1, o2);

  compareTypeConvention = (o1: ITypeConvention | null, o2: ITypeConvention | null): boolean =>
    this.typeConventionService.compareTypeConvention(o1, o2);

  comparePartenaire = (o1: IPartenaire | null, o2: IPartenaire | null): boolean => this.partenaireService.comparePartenaire(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ convention }) => {
      this.convention = convention;
      if (convention) {
        this.updateForm(convention);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('partenariatApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const convention = this.conventionFormService.getConvention(this.editForm);
    if (convention.id !== null) {
      this.subscribeToSaveResponse(this.conventionService.update(convention));
    } else {
      this.subscribeToSaveResponse(this.conventionService.create(convention));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IConvention>>): void {
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

  protected updateForm(convention: IConvention): void {
    this.convention = convention;
    this.conventionFormService.resetForm(this.editForm, convention);

    this.projetsSharedCollection = this.projetService.addProjetToCollectionIfMissing<IProjet>(
      this.projetsSharedCollection,
      convention.projet
    );
    this.typeConventionsSharedCollection = this.typeConventionService.addTypeConventionToCollectionIfMissing<ITypeConvention>(
      this.typeConventionsSharedCollection,
      convention.typeConvention
    );
    this.partenairesSharedCollection = this.partenaireService.addPartenaireToCollectionIfMissing<IPartenaire>(
      this.partenairesSharedCollection,
      ...(convention.partenaires ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.projetService
      .query()
      .pipe(map((res: HttpResponse<IProjet[]>) => res.body ?? []))
      .pipe(map((projets: IProjet[]) => this.projetService.addProjetToCollectionIfMissing<IProjet>(projets, this.convention?.projet)))
      .subscribe((projets: IProjet[]) => (this.projetsSharedCollection = projets));

    this.typeConventionService
      .query()
      .pipe(map((res: HttpResponse<ITypeConvention[]>) => res.body ?? []))
      .pipe(
        map((typeConventions: ITypeConvention[]) =>
          this.typeConventionService.addTypeConventionToCollectionIfMissing<ITypeConvention>(
            typeConventions,
            this.convention?.typeConvention
          )
        )
      )
      .subscribe((typeConventions: ITypeConvention[]) => (this.typeConventionsSharedCollection = typeConventions));

    this.partenaireService
      .query()
      .pipe(map((res: HttpResponse<IPartenaire[]>) => res.body ?? []))
      .pipe(
        map((partenaires: IPartenaire[]) =>
          this.partenaireService.addPartenaireToCollectionIfMissing<IPartenaire>(partenaires, ...(this.convention?.partenaires ?? []))
        )
      )
      .subscribe((partenaires: IPartenaire[]) => (this.partenairesSharedCollection = partenaires));
  }
}
