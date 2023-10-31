import { NgModule } from '@angular/core';
import { DashboardHomeRouting } from './dashboard-home.routing.module';
import { DashboardHomeComponent } from './page/dashboard-home/dashboard-home.component';
import {AppSharedModule} from "../../shared/app-shared.module";
import {
  AvatarModule,
  ButtonGroupModule,
  ButtonModule,
  CardModule,
  FormModule,
  GridModule,
  NavModule,
  ProgressModule,
  TableModule,
  TabsModule, WidgetModule
} from '@coreui/angular';
import { IconModule } from '@coreui/icons-angular';
import { ChartjsModule } from '@coreui/angular-chartjs';
import {ReactiveFormsModule} from "@angular/forms";
import {CommonModule} from "@angular/common";
import {NgChartsModule} from "ng2-charts";

@NgModule({
  declarations: [
    DashboardHomeComponent,
    // ChartSample,
  ],
    imports: [
        AppSharedModule,
        DashboardHomeRouting,


        CardModule,
        NavModule,
        IconModule,
        TabsModule,
        CommonModule,
        GridModule,
        ProgressModule,
        ReactiveFormsModule,
        ButtonModule,
        FormModule,
        ButtonModule,
        ButtonGroupModule,
        ChartjsModule,
        AvatarModule,
        TableModule,
        WidgetModule,
        NgChartsModule,
        //MatSidenavModule
    ]
})
export class DashboardHomeModule { }
