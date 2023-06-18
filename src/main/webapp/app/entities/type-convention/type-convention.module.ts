import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TypeConventionComponent } from './list/type-convention.component';
import { TypeConventionDetailComponent } from './detail/type-convention-detail.component';
import { TypeConventionUpdateComponent } from './update/type-convention-update.component';
import { TypeConventionDeleteDialogComponent } from './delete/type-convention-delete-dialog.component';
import { TypeConventionRoutingModule } from './route/type-convention-routing.module';

@NgModule({
  imports: [SharedModule, TypeConventionRoutingModule],
  declarations: [
    TypeConventionComponent,
    TypeConventionDetailComponent,
    TypeConventionUpdateComponent,
    TypeConventionDeleteDialogComponent,
  ],
})
export class TypeConventionModule {}
