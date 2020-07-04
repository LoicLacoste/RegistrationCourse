import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IRegistration } from 'app/shared/model/registration.model';
import { RegistrationService } from './registration.service';

@Component({
  templateUrl: './registration-delete-dialog.component.html'
})
export class RegistrationDeleteDialogComponent {
  registration?: IRegistration;

  constructor(
    protected registrationService: RegistrationService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.registrationService.delete(id).subscribe(() => {
      this.eventManager.broadcast('registrationListModification');
      this.activeModal.close();
    });
  }
}
