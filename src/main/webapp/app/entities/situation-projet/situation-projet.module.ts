import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SituationProjetComponent } from './list/situation-projet.component';
import { SituationProjetDetailComponent } from './detail/situation-projet-detail.component';
import { SituationProjetUpdateComponent } from './update/situation-projet-update.component';
import { SituationProjetDeleteDialogComponent } from './delete/situation-projet-delete-dialog.component';
import { SituationProjetRoutingModule } from './route/situation-projet-routing.module';

@NgModule({
  imports: [SharedModule, SituationProjetRoutingModule],
  declarations: [
    SituationProjetComponent,
    SituationProjetDetailComponent,
    SituationProjetUpdateComponent,
    SituationProjetDeleteDialogComponent,
  ],
})
export class SituationProjetModule {}
