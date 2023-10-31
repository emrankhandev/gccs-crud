import { NgModule } from '@angular/core';
import { SuperAdminRoutingModule } from './super-admin.routing.module';
import {AppSharedModule} from "../../shared/app-shared.module";
import {PasswordPolicyListComponent} from "./page/password-policy/list/password-policy-list.component";
import {
  PasswordPolicyAddDialogComponent
} from "./page/password-policy/dialog/password-policy-add-dialog/password-policy-add-dialog.component";
import {AppuserListComponent} from "./page/app-user/list/appuser-list.component";
import {
  AppuserAddDialogComponent
} from "./page/app-user/dialog/appuser-add-dialog/appuser-add-dialog.component";
import {FileValidatorListComponent} from "./page/file-validator/list/file-validator-list.component";
import {
  FileValidatorAddDialogComponent
} from "./page/file-validator/dialog/file-validator-add-dialog/file-validator-add-dialog.component";
import {MenuItemAddDialogComponent} from "./page/menu-tem/dialog/menu-item-add-dialog/menu-item-add-dialog.component";
import {MenuItemListComponent} from "./page/menu-tem/list/menu-item-list.component";


import {
  MenuItemDetailsDialogComponent
} from "./page/menu-tem/dialog/menu-item-details-dialog/menu-item-details-dialog.component";
import {UserRoleAddDialogComponent} from "./page/user-role/dialog/user-role-add-dialog/user-role-add-dialog.component";
import {UserRoleListComponent} from "./page/user-role/list/user-role-list.component";
import {SubReportMasterListComponent} from "./page/sub-report-master/list/sub-report-master-list.component";
import {
  SubReportMasterAddDialogComponent
} from "./page/sub-report-master/dialog/sub-report-master-add-dialog/sub-report-master-add-dialog.component";
import {ParameterMasterListComponent} from "./page/parameter-master/list/parameter-master-list.component";
import {
  ParameterMasterAddDialogComponent
} from "./page/parameter-master/dialog/parameter-master-add-dialog/parameter-master-add-dialog.component";
import {
  UserRoleAssignAddDialogComponent
} from "./page/user-role-assign/dialog/user-role-assign-add-dialog/user-role-assign-add-dialog.component";
import {UserRoleAssignListComponent} from "./page/user-role-assign/list/user-role-assign-list.component";
import {ParameterAssignListComponent} from "./page/parameter-assign/list/parameter-assign-list.component";
import {
  ParameterAssignAddDialogComponent
} from "./page/parameter-assign/dialog/parameter-assign-add-dialog/parameter-assign-add-dialog.component";
import {
  ReportUploadAddDialogComponent
} from "./page/report-upload/dialog/add-dialog/report-upload-add-dialog.component";
import {ReportUploadListComponent} from "./page/report-upload/list/report-upload-list.component";
import {ReportConfigureComponent} from "./page/report/report-configure.component";
import {ApprovalTeamListComponent} from "./approve-control/page/approval-team/list/approval-team-list.component";
import {
  ApprovalTeamAddDialogComponent
} from "./approve-control/page/approval-team/dialog/approval-team-add-dialog/approval-team-add-dialog.component";
import {
  ApprovalConfigurationListComponent
} from "./approve-control/page/approval-configuration/list/approval-configuration-list.component";
import {
  ApprovalConfigurationAddDialogComponent
} from "./approve-control/page/approval-configuration/dialog/approval-configuration-add-dialog/approval-configuration-add-dialog.component";
import {
  ApprovalHistoryListComponent
} from "./approve-control/page/approval-history/list/approval-history-list.component";
import {UserLoginComponent} from "./page/user-login/user-login.component";
import {SmsFactoryListComponent} from "./page/sms-factory/list/sms-factory-list.component";
import {MatRadioModule} from "@angular/material/radio";
import {RecoverPasswordComponent} from "./page/recover-password/recover-password.component";
import {RecoverPasswordOtpComponent} from "./page/recover-password/recover-password-otp/recover-password-otp.component";
import {NewPasswordComponent} from "./page/recover-password/new-password/new-password.component";
import {RecoverSuccessPageComponent} from "./page/recover-password/recover-success-page/recover-success-page.component";
import {ResetPasswordComponent} from "./page/reset-password/reset-password.component";
import {
  ApprovalHistoryDialogComponent
} from "./approve-control/page/approval-history/dialog/approval-history-dialog/approval-history-dialog.component";
import {NotificationListComponent} from "./page/notification/list/notification-list.component";
import {NotificationAddDialogComponent} from "./page/notification/dialog/add-dialog/notification-add-dialog.component";
import {UserSignupComponent} from "./page/user-signup/user-signup.component";
import {OtpPageComponent} from "./page/user-signup/otp-page/otp-page.component";
import {FormsModule} from "@angular/forms";



@NgModule({
  declarations: [
    PasswordPolicyListComponent,
    PasswordPolicyAddDialogComponent,
    AppuserListComponent,
    AppuserAddDialogComponent,
    FileValidatorListComponent,
    FileValidatorAddDialogComponent,
    MenuItemAddDialogComponent,
    MenuItemListComponent,
    MenuItemDetailsDialogComponent,
    UserRoleAddDialogComponent,
    UserRoleListComponent,
    UserRoleAssignListComponent,
    UserRoleAssignAddDialogComponent,
    SubReportMasterListComponent,
    SubReportMasterAddDialogComponent,
    ParameterMasterListComponent,
    ParameterMasterAddDialogComponent,
    ParameterAssignListComponent,
    ParameterAssignAddDialogComponent,
    ReportUploadAddDialogComponent,
    ReportUploadListComponent,
    ReportConfigureComponent,
    UserLoginComponent,
    RecoverPasswordComponent,
    RecoverPasswordOtpComponent,
    NewPasswordComponent,
    RecoverSuccessPageComponent,
    ResetPasswordComponent,
    SmsFactoryListComponent,


    ApprovalTeamListComponent,
    ApprovalTeamAddDialogComponent,
    ApprovalConfigurationListComponent,
    ApprovalConfigurationAddDialogComponent,
    ApprovalHistoryListComponent,
    ApprovalHistoryDialogComponent,

    NotificationListComponent,
    NotificationAddDialogComponent,
    UserSignupComponent,
    OtpPageComponent
  ],
    imports: [
        AppSharedModule,
        SuperAdminRoutingModule,
        MatRadioModule,
        FormsModule,

    ]
})
export class SuperAdminModule { }
