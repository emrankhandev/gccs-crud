import {NgModule} from '@angular/core';
import {CommonModule, DatePipe} from '@angular/common';
import {
  AvatarModule,
  BadgeModule,
  BreadcrumbModule,
  ButtonGroupModule,
  ButtonModule,
  CardModule,
  DropdownModule,
  FooterModule,
  FormModule,
  GridModule,
  HeaderModule,
  ListGroupModule, ModalModule,
  NavModule,
  ProgressModule,
  SharedModule,
  SidebarModule, SpinnerModule,
  TabsModule,
  UtilitiesModule
} from "@coreui/angular";
import {IconModule} from "@coreui/icons-angular";
import {PerfectScrollbarModule} from "ngx-perfect-scrollbar";
import {ReactiveFormsModule} from "@angular/forms";
import {HttpClientModule} from '@angular/common/http';
import {MatSelectSearchComponent} from "./component/mat-select-search/mat-select-search.component";
import {MatDialogModule} from "@angular/material/dialog";
import {MatTableModule} from "@angular/material/table";
import {MatPaginatorModule} from "@angular/material/paginator";
import {MatSelectModule} from "@angular/material/select";
import {NgxMatSelectSearchModule} from "ngx-mat-select-search";
import {MatSnackBarModule} from "@angular/material/snack-bar";
import {ConfirmationDialogComponent} from "./component/confirmation-dialog/confirmation-dialog.component";
import {TranslateModule} from "@ngx-translate/core";
import {MatIconModule} from "@angular/material/icon";
import {AddiesErrorComponent} from "./component/addies-error/addies-error.component";
import {MatTooltipModule} from "@angular/material/tooltip";
import {MatStepperModule} from "@angular/material/stepper";
import {MatExpansionModule} from "@angular/material/expansion";
import { RouterModule} from '@angular/router';
import {MatListModule} from "@angular/material/list";
import {ApprovalActionComponent} from "./component/approval/approval-action/approval-action.component";
import {
  ApprovalMonitorDialogComponent
} from "./component/approval/approval-monitor-dialog/approval-monitor-dialog.component";
import {DateAsAgoPipe} from "./pipes/date-as-ago.pipe";
import {NoticeShowDialogComponent} from "./component/notice/notice-show-dialog.component";


const _materialModule = [

  ModalModule,
  MatDialogModule,
  MatTableModule,
  MatPaginatorModule,
  MatSelectModule,
  NgxMatSelectSearchModule,
  MatSnackBarModule,
  MatIconModule,
  MatTooltipModule,
  MatStepperModule,
  MatExpansionModule,
  MatListModule,

];

const _allModule = [
  CommonModule,
  CardModule,
  ButtonModule,
  GridModule,
  IconModule,
  FormModule,
  HttpClientModule,
  AvatarModule,
  BreadcrumbModule,
  FooterModule,
  DropdownModule,
  HeaderModule,
  SidebarModule,
  PerfectScrollbarModule,
  NavModule,
  UtilitiesModule,
  ButtonGroupModule,
  ReactiveFormsModule,
  SharedModule,
  TabsModule,
  ListGroupModule,
  ProgressModule,
  BadgeModule,
  SpinnerModule,
  TranslateModule,
  RouterModule,
  _materialModule,
];

const _allComponent = [
  MatSelectSearchComponent,
  ConfirmationDialogComponent,
  AddiesErrorComponent,
  ApprovalActionComponent,
  ApprovalMonitorDialogComponent,
  NoticeShowDialogComponent,
];

@NgModule({
    providers: [DatePipe],
    imports: [
        _allModule,
    ],
    exports: [
        _allModule,
        _allComponent,
      DateAsAgoPipe
    ],
    declarations: [
        _allComponent,
      DateAsAgoPipe

    ],

})
export class AppSharedModule {
}
