import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { RegistrationCourseTestModule } from '../../../test.module';
import { ExtraUserComponent } from 'app/entities/extra-user/extra-user.component';
import { ExtraUserService } from 'app/entities/extra-user/extra-user.service';
import { ExtraUser } from 'app/shared/model/extra-user.model';

describe('Component Tests', () => {
  describe('ExtraUser Management Component', () => {
    let comp: ExtraUserComponent;
    let fixture: ComponentFixture<ExtraUserComponent>;
    let service: ExtraUserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [RegistrationCourseTestModule],
        declarations: [ExtraUserComponent]
      })
        .overrideTemplate(ExtraUserComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ExtraUserComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ExtraUserService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new ExtraUser(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.extraUsers && comp.extraUsers[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
