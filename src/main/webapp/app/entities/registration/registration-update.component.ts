import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IRegistration, Registration } from 'app/shared/model/registration.model';
import { RegistrationService } from './registration.service';

@Component({
  selector: 'jhi-registration-update',
  templateUrl: './registration-update.component.html'
})
export class RegistrationUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    isCertOk: [],
    isPaye: [],
    dossard: [],
    temps: [],
    date: []
  });

  constructor(protected registrationService: RegistrationService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ registration }) => {
      if (!registration.id) {
        const today = moment().startOf('day');
        registration.date = today;
      }

      this.updateForm(registration);
    });
  }

  updateForm(registration: IRegistration): void {
    this.editForm.patchValue({
      id: registration.id,
      isCertOk: registration.isCertOk,
      isPaye: registration.isPaye,
      dossard: registration.dossard,
      temps: registration.temps,
      date: registration.date ? registration.date.format(DATE_TIME_FORMAT) : null
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const registration = this.createFromForm();
    if (registration.id !== undefined) {
      this.subscribeToSaveResponse(this.registrationService.update(registration));
    } else {
      this.subscribeToSaveResponse(this.registrationService.create(registration));
    }
  }

  private createFromForm(): IRegistration {
    return {
      ...new Registration(),
      id: this.editForm.get(['id'])!.value,
      isCertOk: this.editForm.get(['isCertOk'])!.value,
      isPaye: this.editForm.get(['isPaye'])!.value,
      dossard: this.editForm.get(['dossard'])!.value,
      temps: this.editForm.get(['temps'])!.value,
      date: this.editForm.get(['date'])!.value ? moment(this.editForm.get(['date'])!.value, DATE_TIME_FORMAT) : undefined
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRegistration>>): void {
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
}
