import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IExtraUser, ExtraUser } from 'app/shared/model/extra-user.model';
import { ExtraUserService } from './extra-user.service';
import { ExtraUserComponent } from './extra-user.component';
import { ExtraUserDetailComponent } from './extra-user-detail.component';
import { ExtraUserUpdateComponent } from './extra-user-update.component';

@Injectable({ providedIn: 'root' })
export class ExtraUserResolve implements Resolve<IExtraUser> {
  constructor(private service: ExtraUserService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IExtraUser> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((extraUser: HttpResponse<ExtraUser>) => {
          if (extraUser.body) {
            return of(extraUser.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ExtraUser());
  }
}

export const extraUserRoute: Routes = [
  {
    path: '',
    component: ExtraUserComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'registrationCourseApp.extraUser.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ExtraUserDetailComponent,
    resolve: {
      extraUser: ExtraUserResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'registrationCourseApp.extraUser.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ExtraUserUpdateComponent,
    resolve: {
      extraUser: ExtraUserResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'registrationCourseApp.extraUser.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ExtraUserUpdateComponent,
    resolve: {
      extraUser: ExtraUserResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'registrationCourseApp.extraUser.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
