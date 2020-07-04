import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { RegistrationCourseSharedModule } from 'app/shared/shared.module';
import { ExtraUserComponent } from './extra-user.component';
import { ExtraUserDetailComponent } from './extra-user-detail.component';
import { ExtraUserUpdateComponent } from './extra-user-update.component';
import { ExtraUserDeleteDialogComponent } from './extra-user-delete-dialog.component';
import { extraUserRoute } from './extra-user.route';

@NgModule({
  imports: [RegistrationCourseSharedModule, RouterModule.forChild(extraUserRoute)],
  declarations: [ExtraUserComponent, ExtraUserDetailComponent, ExtraUserUpdateComponent, ExtraUserDeleteDialogComponent],
  entryComponents: [ExtraUserDeleteDialogComponent]
})
export class RegistrationCourseExtraUserModule {}
