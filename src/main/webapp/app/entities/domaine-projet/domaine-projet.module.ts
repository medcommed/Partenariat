import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DomaineProjetComponent } from './list/domaine-projet.component';
import { DomaineProjetDetailComponent } from './detail/domaine-projet-detail.component';
import { DomaineProjetUpdateComponent } from './update/domaine-projet-update.component';
import { DomaineProjetDeleteDialogComponent } from './delete/domaine-projet-delete-dialog.component';
import { DomaineProjetRoutingModule } from './route/domaine-projet-routing.module';

@NgModule({
  imports: [SharedModule, DomaineProjetRoutingModule],
  declarations: [DomaineProjetComponent, DomaineProjetDetailComponent, DomaineProjetUpdateComponent, DomaineProjetDeleteDialogComponent],
})
export class DomaineProjetModule {}
