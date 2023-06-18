import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISituationProjet } from '../situation-projet.model';

@Component({
  selector: 'jhi-situation-projet-detail',
  templateUrl: './situation-projet-detail.component.html',
})
export class SituationProjetDetailComponent implements OnInit {
  situationProjet: ISituationProjet | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ situationProjet }) => {
      this.situationProjet = situationProjet;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
