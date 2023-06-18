import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TrancheComponent } from '../list/tranche.component';
import { TrancheDetailComponent } from '../detail/tranche-detail.component';
import { TrancheUpdateComponent } from '../update/tranche-update.component';
import { TrancheRoutingResolveService } from './tranche-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const trancheRoute: Routes = [
  {
    path: '',
    component: TrancheComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TrancheDetailComponent,
    resolve: {
      tranche: TrancheRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TrancheUpdateComponent,
    resolve: {
      tranche: TrancheRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TrancheUpdateComponent,
    resolve: {
      tranche: TrancheRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(trancheRoute)],
  exports: [RouterModule],
})
export class TrancheRoutingModule {}
