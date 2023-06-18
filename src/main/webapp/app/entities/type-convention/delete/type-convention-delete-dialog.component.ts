import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITypeConvention } from '../type-convention.model';
import { TypeConventionService } from '../service/type-convention.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './type-convention-delete-dialog.component.html',
})
export class TypeConventionDeleteDialogComponent {
  typeConvention?: ITypeConvention;

  constructor(protected typeConventionService: TypeConventionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.typeConventionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
