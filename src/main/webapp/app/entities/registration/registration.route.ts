import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IRegistration, Registration } from 'app/shared/model/registration.model';
import { RegistrationService } from './registration.service';
import { RegistrationComponent } from './registration.component';
import { RegistrationDetailComponent } from './registration-detail.component';
import { RegistrationUpdateComponent } from './registration-update.component';

@Injectable({ providedIn: 'root' })
export class RegistrationResolve implements Resolve<IRegistration> {
  constructor(private service: RegistrationService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRegistration> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((registration: HttpResponse<Registration>) => {
          if (registration.body) {
            return of(registration.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Registration());
  }
}

export const registrationRoute: Routes = [
  {
    path: '',
    component: RegistrationComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'registrationCourseApp.registration.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: RegistrationDetailComponent,
    resolve: {
      registration: RegistrationResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'registrationCourseApp.registration.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: RegistrationUpdateComponent,
    resolve: {
      registration: RegistrationResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'registrationCourseApp.registration.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: RegistrationUpdateComponent,
    resolve: {
      registration: RegistrationResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'registrationCourseApp.registration.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
