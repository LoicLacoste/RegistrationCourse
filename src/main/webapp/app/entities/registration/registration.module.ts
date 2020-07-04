import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { RegistrationCourseSharedModule } from 'app/shared/shared.module';
import { RegistrationComponent } from './registration.component';
import { RegistrationDetailComponent } from './registration-detail.component';
import { RegistrationUpdateComponent } from './registration-update.component';
import { RegistrationDeleteDialogComponent } from './registration-delete-dialog.component';
import { registrationRoute } from './registration.route';

@NgModule({
  imports: [RegistrationCourseSharedModule, RouterModule.forChild(registrationRoute)],
  declarations: [RegistrationComponent, RegistrationDetailComponent, RegistrationUpdateComponent, RegistrationDeleteDialogComponent],
  entryComponents: [RegistrationDeleteDialogComponent]
})
export class RegistrationCourseRegistrationModule {}
