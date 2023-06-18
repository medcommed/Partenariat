import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDomaineProjet } from '../domaine-projet.model';
import { DomaineProjetService } from '../service/domaine-projet.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './domaine-projet-delete-dialog.component.html',
})
export class DomaineProjetDeleteDialogComponent {
  domaineProjet?: IDomaineProjet;

  constructor(protected domaineProjetService: DomaineProjetService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.domaineProjetService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
