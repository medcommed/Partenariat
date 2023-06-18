import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TrancheComponent } from './list/tranche.component';
import { TrancheDetailComponent } from './detail/tranche-detail.component';
import { TrancheUpdateComponent } from './update/tranche-update.component';
import { TrancheDeleteDialogComponent } from './delete/tranche-delete-dialog.component';
import { TrancheRoutingModule } from './route/tranche-routing.module';

@NgModule({
  imports: [SharedModule, TrancheRoutingModule],
  declarations: [TrancheComponent, TrancheDetailComponent, TrancheUpdateComponent, TrancheDeleteDialogComponent],
})
export class TrancheModule {}
