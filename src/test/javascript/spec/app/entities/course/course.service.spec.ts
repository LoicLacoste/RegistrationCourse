import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { CourseService } from 'app/entities/course/course.service';
import { ICourse, Course } from 'app/shared/model/course.model';

describe('Service Tests', () => {
  describe('Course Service', () => {
    let injector: TestBed;
    let service: CourseService;
    let httpMock: HttpTestingController;
    let elemDefault: ICourse;
    let expectedResult: ICourse | ICourse[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(CourseService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Course(0, 0, 'AAAAAAA', 'AAAAAAA', 0, 0, currentDate, 'image/png', 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dateCourse: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Course', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dateCourse: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateCourse: currentDate
          },
          returnedFromService
        );

        service.create(new Course()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Course', () => {
        const returnedFromService = Object.assign(
          {
            name: 1,
            description: 'BBBBBB',
            address: 'BBBBBB',
            price: 1,
            places: 1,
            dateCourse: currentDate.format(DATE_TIME_FORMAT),
            imageCourse: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateCourse: currentDate
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Course', () => {
        const returnedFromService = Object.assign(
          {
            name: 1,
            description: 'BBBBBB',
            address: 'BBBBBB',
            price: 1,
            places: 1,
            dateCourse: currentDate.format(DATE_TIME_FORMAT),
            imageCourse: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateCourse: currentDate
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Course', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
