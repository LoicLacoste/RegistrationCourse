import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IRegistration } from 'app/shared/model/registration.model';
import { RegistrationService } from './registration.service';
import { RegistrationDeleteDialogComponent } from './registration-delete-dialog.component';

@Component({
  selector: 'jhi-registration',
  templateUrl: './registration.component.html'
})
export class RegistrationComponent implements OnInit, OnDestroy {
  registrations?: IRegistration[];
  eventSubscriber?: Subscription;

  constructor(
    protected registrationService: RegistrationService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.registrationService.query().subscribe((res: HttpResponse<IRegistration[]>) => (this.registrations = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInRegistrations();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IRegistration): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInRegistrations(): void {
    this.eventSubscriber = this.eventManager.subscribe('registrationListModification', () => this.loadAll());
  }

  delete(registration: IRegistration): void {
    const modalRef = this.modalService.open(RegistrationDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.registration = registration;
  }
}
