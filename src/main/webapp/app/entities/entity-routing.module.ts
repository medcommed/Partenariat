import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'commune',
        data: { pageTitle: 'partenariatApp.commune.home.title' },
        loadChildren: () => import('./commune/commune.module').then(m => m.CommuneModule),
      },
      {
        path: 'convention',
        data: { pageTitle: 'partenariatApp.convention.home.title' },
        loadChildren: () => import('./convention/convention.module').then(m => m.ConventionModule),
      },
      {
        path: 'domaine-projet',
        data: { pageTitle: 'partenariatApp.domaineProjet.home.title' },
        loadChildren: () => import('./domaine-projet/domaine-projet.module').then(m => m.DomaineProjetModule),
      },
      {
        path: 'partenaire',
        data: { pageTitle: 'partenariatApp.partenaire.home.title' },
        loadChildren: () => import('./partenaire/partenaire.module').then(m => m.PartenaireModule),
      },
      {
        path: 'projet',
        data: { pageTitle: 'partenariatApp.projet.home.title' },
        loadChildren: () => import('./projet/projet.module').then(m => m.ProjetModule),
      },
      {
        path: 'province',
        data: { pageTitle: 'partenariatApp.province.home.title' },
        loadChildren: () => import('./province/province.module').then(m => m.ProvinceModule),
      },
      {
        path: 'region',
        data: { pageTitle: 'partenariatApp.region.home.title' },
        loadChildren: () => import('./region/region.module').then(m => m.RegionModule),
      },
      {
        path: 'situation-projet',
        data: { pageTitle: 'partenariatApp.situationProjet.home.title' },
        loadChildren: () => import('./situation-projet/situation-projet.module').then(m => m.SituationProjetModule),
      },
      {
        path: 'tranche',
        data: { pageTitle: 'partenariatApp.tranche.home.title' },
        loadChildren: () => import('./tranche/tranche.module').then(m => m.TrancheModule),
      },
      {
        path: 'type-convention',
        data: { pageTitle: 'partenariatApp.typeConvention.home.title' },
        loadChildren: () => import('./type-convention/type-convention.module').then(m => m.TypeConventionModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
