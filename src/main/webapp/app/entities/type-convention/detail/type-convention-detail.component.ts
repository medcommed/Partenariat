import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITypeConvention } from '../type-convention.model';

@Component({
  selector: 'jhi-type-convention-detail',
  templateUrl: './type-convention-detail.component.html',
})
export class TypeConventionDetailComponent implements OnInit {
  typeConvention: ITypeConvention | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ typeConvention }) => {
      this.typeConvention = typeConvention;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
