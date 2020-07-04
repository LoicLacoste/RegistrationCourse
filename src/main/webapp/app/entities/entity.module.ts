import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'course',
        loadChildren: () => import('./course/course.module').then(m => m.RegistrationCourseCourseModule)
      },
      {
        path: 'registration',
        loadChildren: () => import('./registration/registration.module').then(m => m.RegistrationCourseRegistrationModule)
      },
      {
        path: 'extra-user',
        loadChildren: () => import('./extra-user/extra-user.module').then(m => m.RegistrationCourseExtraUserModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class RegistrationCourseEntityModule {}
