import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITranche } from '../tranche.model';

@Component({
  selector: 'jhi-tranche-detail',
  templateUrl: './tranche-detail.component.html',
})
export class TrancheDetailComponent implements OnInit {
  tranche: ITranche | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tranche }) => {
      this.tranche = tranche;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
