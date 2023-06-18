import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DomaineProjetComponent } from '../list/domaine-projet.component';
import { DomaineProjetDetailComponent } from '../detail/domaine-projet-detail.component';
import { DomaineProjetUpdateComponent } from '../update/domaine-projet-update.component';
import { DomaineProjetRoutingResolveService } from './domaine-projet-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const domaineProjetRoute: Routes = [
  {
    path: '',
    component: DomaineProjetComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DomaineProjetDetailComponent,
    resolve: {
      domaineProjet: DomaineProjetRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DomaineProjetUpdateComponent,
    resolve: {
      domaineProjet: DomaineProjetRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DomaineProjetUpdateComponent,
    resolve: {
      domaineProjet: DomaineProjetRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(domaineProjetRoute)],
  exports: [RouterModule],
})
export class DomaineProjetRoutingModule {}
