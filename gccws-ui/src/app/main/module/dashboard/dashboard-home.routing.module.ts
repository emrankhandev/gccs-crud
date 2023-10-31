import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardHomeComponent } from './page/dashboard-home/dashboard-home.component';
import {DefaultLayoutComponent} from "../../layout";
import {AuthGuard} from "../../core/auth/guards/auth.guard";
import {ReportConfigureComponent} from "../super-admin/page/report/report-configure.component";
// import {ReportConfigureComponent} from "../system-admin/report/page/report/list/report-configure.component";

const routes: Routes = [
    {
        path: 'dashboard-home',
        canActivate: [AuthGuard],
        canActivateChild: [AuthGuard],
        component: DefaultLayoutComponent,
        resolve: {
            // initialData: InitialDataResolver,
        },
        children: [
            {path: '', component: DashboardHomeComponent, data: {title: $localize`Dashboard`}},
            {path: 'report', component: ReportConfigureComponent},


          // report
          // {path: 'report-control/report', component: ReportConfigureComponent},
          // {path: '', component: DashboardHomeComponent},
          // {path: 'report', component: ReportConfigureComponent},
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
export class DashboardHomeRouting {
}
