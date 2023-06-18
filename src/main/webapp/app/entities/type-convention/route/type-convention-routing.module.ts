import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TypeConventionComponent } from '../list/type-convention.component';
import { TypeConventionDetailComponent } from '../detail/type-convention-detail.component';
import { TypeConventionUpdateComponent } from '../update/type-convention-update.component';
import { TypeConventionRoutingResolveService } from './type-convention-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const typeConventionRoute: Routes = [
  {
    path: '',
    component: TypeConventionComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TypeConventionDetailComponent,
    resolve: {
      typeConvention: TypeConventionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TypeConventionUpdateComponent,
    resolve: {
      typeConvention: TypeConventionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TypeConventionUpdateComponent,
    resolve: {
      typeConvention: TypeConventionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(typeConventionRoute)],
  exports: [RouterModule],
})
export class TypeConventionRoutingModule {}
