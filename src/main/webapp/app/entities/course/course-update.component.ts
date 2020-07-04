import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { ICourse, Course } from 'app/shared/model/course.model';
import { CourseService } from './course.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { IRegistration } from 'app/shared/model/registration.model';
import { RegistrationService } from 'app/entities/registration/registration.service';

@Component({
  selector: 'jhi-course-update',
  templateUrl: './course-update.component.html'
})
export class CourseUpdateComponent implements OnInit {
  isSaving = false;
  registrations: IRegistration[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    description: [],
    address: [],
    price: [],
    places: [],
    dateCourse: [],
    imageCourse: [],
    imageCourseContentType: [],
    registration: []
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected courseService: CourseService,
    protected registrationService: RegistrationService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ course }) => {
      if (!course.id) {
        const today = moment().startOf('day');
        course.dateCourse = today;
      }

      this.updateForm(course);

      this.registrationService.query().subscribe((res: HttpResponse<IRegistration[]>) => (this.registrations = res.body || []));
    });
  }

  updateForm(course: ICourse): void {
    this.editForm.patchValue({
      id: course.id,
      name: course.name,
      description: course.description,
      address: course.address,
      price: course.price,
      places: course.places,
      dateCourse: course.dateCourse ? course.dateCourse.format(DATE_TIME_FORMAT) : null,
      imageCourse: course.imageCourse,
      imageCourseContentType: course.imageCourseContentType,
      registration: course.registration
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType: string, base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe(null, (err: JhiFileLoadError) => {
      this.eventManager.broadcast(
        new JhiEventWithContent<AlertError>('registrationCourseApp.error', { ...err, key: 'error.file.' + err.key })
      );
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null
    });
    if (this.elementRef && idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const course = this.createFromForm();
    if (course.id !== undefined) {
      this.subscribeToSaveResponse(this.courseService.update(course));
    } else {
      this.subscribeToSaveResponse(this.courseService.create(course));
    }
  }

  private createFromForm(): ICourse {
    return {
      ...new Course(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      address: this.editForm.get(['address'])!.value,
      price: this.editForm.get(['price'])!.value,
      places: this.editForm.get(['places'])!.value,
      dateCourse: this.editForm.get(['dateCourse'])!.value ? moment(this.editForm.get(['dateCourse'])!.value, DATE_TIME_FORMAT) : undefined,
      imageCourseContentType: this.editForm.get(['imageCourseContentType'])!.value,
      imageCourse: this.editForm.get(['imageCourse'])!.value,
      registration: this.editForm.get(['registration'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICourse>>): void {
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

  trackById(index: number, item: IRegistration): any {
    return item.id;
  }
}
