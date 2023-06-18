import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDomaineProjet } from '../domaine-projet.model';

@Component({
  selector: 'jhi-domaine-projet-detail',
  templateUrl: './domaine-projet-detail.component.html',
})
export class DomaineProjetDetailComponent implements OnInit {
  domaineProjet: IDomaineProjet | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ domaineProjet }) => {
      this.domaineProjet = domaineProjet;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
