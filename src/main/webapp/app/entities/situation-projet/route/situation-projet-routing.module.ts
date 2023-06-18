import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SituationProjetComponent } from '../list/situation-projet.component';
import { SituationProjetDetailComponent } from '../detail/situation-projet-detail.component';
import { SituationProjetUpdateComponent } from '../update/situation-projet-update.component';
import { SituationProjetRoutingResolveService } from './situation-projet-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const situationProjetRoute: Routes = [
  {
    path: '',
    component: SituationProjetComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SituationProjetDetailComponent,
    resolve: {
      situationProjet: SituationProjetRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SituationProjetUpdateComponent,
    resolve: {
      situationProjet: SituationProjetRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SituationProjetUpdateComponent,
    resolve: {
      situationProjet: SituationProjetRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(situationProjetRoute)],
  exports: [RouterModule],
})
export class SituationProjetRoutingModule {}
