import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {DefaultLayoutComponent} from "../../layout";
import {PasswordPolicyListComponent} from "./page/password-policy/list/password-policy-list.component";
import {AppuserListComponent} from "./page/app-user/list/appuser-list.component";
import {FileValidatorListComponent} from "./page/file-validator/list/file-validator-list.component";
import {MenuItemListComponent} from "./page/menu-tem/list/menu-item-list.component";
import {SubReportMasterListComponent} from "./page/sub-report-master/list/sub-report-master-list.component";
import {AuthGuard} from "../../core/auth/guards/auth.guard";
import {UserRoleListComponent} from "./page/user-role/list/user-role-list.component";
import {ParameterMasterListComponent} from "./page/parameter-master/list/parameter-master-list.component";
import {UserRoleAssignListComponent} from "./page/user-role-assign/list/user-role-assign-list.component";
import {ParameterAssignListComponent} from "./page/parameter-assign/list/parameter-assign-list.component";
import {ReportUploadListComponent} from "./page/report-upload/list/report-upload-list.component";
import {ReportConfigureComponent} from "./page/report/report-configure.component";
import {ApprovalTeamListComponent} from "./approve-control/page/approval-team/list/approval-team-list.component";
import {
  ApprovalConfigurationListComponent
} from "./approve-control/page/approval-configuration/list/approval-configuration-list.component";
import {
  ApprovalHistoryListComponent
} from "./approve-control/page/approval-history/list/approval-history-list.component";
import {SmsFactoryListComponent} from "./page/sms-factory/list/sms-factory-list.component";
import {NotificationListComponent} from "./page/notification/list/notification-list.component";
import {PAGE_REQUEST_MARINE, PAGE_REQUEST_TRAFFIC} from "../../core/constants/router.constants";


const routes: Routes = [
    {
        path: 'super-admin',
        canActivate: [AuthGuard],
        canActivateChild: [AuthGuard],
        component: DefaultLayoutComponent,
        resolve: {
            // initialData: InitialDataResolver,
        },
        children: [
          {path: 'password-policy', component: PasswordPolicyListComponent},
          {path: 'application-user', component: AppuserListComponent},

          {path: 'file-validator', component: FileValidatorListComponent},
          {path: 'menu-item', component: MenuItemListComponent},
          {path: 'user-role', component: UserRoleListComponent},
          {path: 'user-role-assign', component: UserRoleAssignListComponent},
          {path: 'notification', component: NotificationListComponent},


          {path: 'report-upload', component: ReportUploadListComponent},
          {path: 'sub-report-master', component: SubReportMasterListComponent},
          {path: 'parameter-master', component: ParameterMasterListComponent},
          {path: 'parameter-assign', component: ParameterAssignListComponent},
          {path: 'report', component: ReportConfigureComponent},

          {path: 'approval-team', component: ApprovalTeamListComponent},
          {path: 'approval-configuration', component: ApprovalConfigurationListComponent},
          {path: 'approval-history', component: ApprovalHistoryListComponent},

          {path: 'sms-factory', component: SmsFactoryListComponent}


        ]

    }
];


@NgModule({
    imports: [
        RouterModule.forChild(routes)
    ],
    exports: [
        RouterModule
    ],
    providers: []
})
export class SuperAdminRoutingModule {
}
