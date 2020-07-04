import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IExtraUser } from 'app/shared/model/extra-user.model';
import { ExtraUserService } from './extra-user.service';
import { ExtraUserDeleteDialogComponent } from './extra-user-delete-dialog.component';

@Component({
  selector: 'jhi-extra-user',
  templateUrl: './extra-user.component.html'
})
export class ExtraUserComponent implements OnInit, OnDestroy {
  extraUsers?: IExtraUser[];
  eventSubscriber?: Subscription;

  constructor(protected extraUserService: ExtraUserService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.extraUserService.query().subscribe((res: HttpResponse<IExtraUser[]>) => (this.extraUsers = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInExtraUsers();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IExtraUser): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInExtraUsers(): void {
    this.eventSubscriber = this.eventManager.subscribe('extraUserListModification', () => this.loadAll());
  }

  delete(extraUser: IExtraUser): void {
    const modalRef = this.modalService.open(ExtraUserDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.extraUser = extraUser;
  }
}
