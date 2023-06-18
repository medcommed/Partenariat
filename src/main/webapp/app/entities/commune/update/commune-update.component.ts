import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { CommuneFormService, CommuneFormGroup } from './commune-form.service';
import { ICommune } from '../commune.model';
import { CommuneService } from '../service/commune.service';
import { IProvince } from 'app/entities/province/province.model';
import { ProvinceService } from 'app/entities/province/service/province.service';

@Component({
  selector: 'jhi-commune-update',
  templateUrl: './commune-update.component.html',
})
export class CommuneUpdateComponent implements OnInit {
  isSaving = false;
  commune: ICommune | null = null;

  provincesSharedCollection: IProvince[] = [];

  editForm: CommuneFormGroup = this.communeFormService.createCommuneFormGroup();

  constructor(
    protected communeService: CommuneService,
    protected communeFormService: CommuneFormService,
    protected provinceService: ProvinceService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareProvince = (o1: IProvince | null, o2: IProvince | null): boolean => this.provinceService.compareProvince(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ commune }) => {
      this.commune = commune;
      if (commune) {
        this.updateForm(commune);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const commune = this.communeFormService.getCommune(this.editForm);
    if (commune.id !== null) {
      this.subscribeToSaveResponse(this.communeService.update(commune));
    } else {
      this.subscribeToSaveResponse(this.communeService.create(commune));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICommune>>): void {
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

  protected updateForm(commune: ICommune): void {
    this.commune = commune;
    this.communeFormService.resetForm(this.editForm, commune);

    this.provincesSharedCollection = this.provinceService.addProvinceToCollectionIfMissing<IProvince>(
      this.provincesSharedCollection,
      commune.provinces
    );
  }

  protected loadRelationshipsOptions(): void {
    this.provinceService
      .query()
      .pipe(map((res: HttpResponse<IProvince[]>) => res.body ?? []))
      .pipe(
        map((provinces: IProvince[]) =>
          this.provinceService.addProvinceToCollectionIfMissing<IProvince>(provinces, this.commune?.provinces)
        )
      )
      .subscribe((provinces: IProvince[]) => (this.provincesSharedCollection = provinces));
  }
}
