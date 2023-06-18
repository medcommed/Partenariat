import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISituationProjet } from '../situation-projet.model';
import { SituationProjetService } from '../service/situation-projet.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './situation-projet-delete-dialog.component.html',
})
export class SituationProjetDeleteDialogComponent {
  situationProjet?: ISituationProjet;

  constructor(protected situationProjetService: SituationProjetService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.situationProjetService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
