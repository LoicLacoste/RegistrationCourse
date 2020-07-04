import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { RegistrationCourseTestModule } from '../../../test.module';
import { RegistrationUpdateComponent } from 'app/entities/registration/registration-update.component';
import { RegistrationService } from 'app/entities/registration/registration.service';
import { Registration } from 'app/shared/model/registration.model';

describe('Component Tests', () => {
  describe('Registration Management Update Component', () => {
    let comp: RegistrationUpdateComponent;
    let fixture: ComponentFixture<RegistrationUpdateComponent>;
    let service: RegistrationService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [RegistrationCourseTestModule],
        declarations: [RegistrationUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(RegistrationUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RegistrationUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(RegistrationService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Registration(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Registration();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
