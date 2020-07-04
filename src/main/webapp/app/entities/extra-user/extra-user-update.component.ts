import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IExtraUser, ExtraUser } from 'app/shared/model/extra-user.model';
import { ExtraUserService } from './extra-user.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';

@Component({
  selector: 'jhi-extra-user-update',
  templateUrl: './extra-user-update.component.html'
})
export class ExtraUserUpdateComponent implements OnInit {
  isSaving = false;
  users: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    nom: [null, [Validators.required]],
    prenom: [null, [Validators.required]],
    dateNaissance: [null, [Validators.required]],
    user: []
  });

  constructor(
    protected extraUserService: ExtraUserService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ extraUser }) => {
      if (!extraUser.id) {
        const today = moment().startOf('day');
        extraUser.dateNaissance = today;
      }

      this.updateForm(extraUser);

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));
    });
  }

  updateForm(extraUser: IExtraUser): void {
    this.editForm.patchValue({
      id: extraUser.id,
      nom: extraUser.nom,
      prenom: extraUser.prenom,
      dateNaissance: extraUser.dateNaissance ? extraUser.dateNaissance.format(DATE_TIME_FORMAT) : null,
      user: extraUser.user
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const extraUser = this.createFromForm();
    if (extraUser.id !== undefined) {
      this.subscribeToSaveResponse(this.extraUserService.update(extraUser));
    } else {
      this.subscribeToSaveResponse(this.extraUserService.create(extraUser));
    }
  }

  private createFromForm(): IExtraUser {
    return {
      ...new ExtraUser(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      prenom: this.editForm.get(['prenom'])!.value,
      dateNaissance: this.editForm.get(['dateNaissance'])!.value
        ? moment(this.editForm.get(['dateNaissance'])!.value, DATE_TIME_FORMAT)
        : undefined,
      user: this.editForm.get(['user'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExtraUser>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: IUser): any {
    return item.id;
  }
}
