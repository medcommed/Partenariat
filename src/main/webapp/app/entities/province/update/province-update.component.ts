import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ProvinceFormService, ProvinceFormGroup } from './province-form.service';
import { IProvince } from '../province.model';
import { ProvinceService } from '../service/province.service';
import { IRegion } from 'app/entities/region/region.model';
import { RegionService } from 'app/entities/region/service/region.service';

@Component({
  selector: 'jhi-province-update',
  templateUrl: './province-update.component.html',
})
export class ProvinceUpdateComponent implements OnInit {
  isSaving = false;
  province: IProvince | null = null;

  regionsSharedCollection: IRegion[] = [];

  editForm: ProvinceFormGroup = this.provinceFormService.createProvinceFormGroup();

  constructor(
    protected provinceService: ProvinceService,
    protected provinceFormService: ProvinceFormService,
    protected regionService: RegionService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareRegion = (o1: IRegion | null, o2: IRegion | null): boolean => this.regionService.compareRegion(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ province }) => {
      this.province = province;
      if (province) {
        this.updateForm(province);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const province = this.provinceFormService.getProvince(this.editForm);
    if (province.id !== null) {
      this.subscribeToSaveResponse(this.provinceService.update(province));
    } else {
      this.subscribeToSaveResponse(this.provinceService.create(province));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProvince>>): void {
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

  protected updateForm(province: IProvince): void {
    this.province = province;
    this.provinceFormService.resetForm(this.editForm, province);

    this.regionsSharedCollection = this.regionService.addRegionToCollectionIfMissing<IRegion>(
      this.regionsSharedCollection,
      province.region
    );
  }

  protected loadRelationshipsOptions(): void {
    this.regionService
      .query()
      .pipe(map((res: HttpResponse<IRegion[]>) => res.body ?? []))
      .pipe(map((regions: IRegion[]) => this.regionService.addRegionToCollectionIfMissing<IRegion>(regions, this.province?.region)))
      .subscribe((regions: IRegion[]) => (this.regionsSharedCollection = regions));
  }
}
