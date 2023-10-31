import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {MatDialog} from "@angular/material/dialog";
import {AppUtils} from "../../../../core/utils/app.utils";
import {LocalStoreUtils} from "../../../../core/utils/local-store.utils";
import {UserRoleService} from "../../../super-admin/service/user-role.service";
import {DashboardService} from "../../service/dashboard.service";
import {locale as lngEnglish} from "./i18n/en";
import {locale as lngBangla} from "./i18n/bn";
import {TranslationLoaderService} from "../../../../core/service/translation-loader.service";
import {FileService} from "../../../../shared/service/file.service";
import {AgentApproveType, AgentApproveTypeService} from "../../../../core/mock-data/agent-approve-type.service";

@Component({
  selector: 'app-dashboard-home',
  templateUrl: './dashboard-home.component.html',
  styleUrls: ['./dashboard-home.component.scss']
})
export class DashboardHomeComponent implements OnInit {

  /*property*/

  userInfo: any;
  isCustomerFromShow:boolean = false;
  isLoading: boolean = false;
  isActive: boolean | undefined = false;
  isEdit: boolean;
  isAgentProfile: boolean;

  /*extra*/

  constructor(
    private matDialog: MatDialog,
    private userRoleService: UserRoleService,
    private dashboardService: DashboardService,
    private agentApproveTypeService: AgentApproveTypeService,
    private translationLoaderService: TranslationLoaderService,
    public localStoreUtils : LocalStoreUtils,
    public appUtils : AppUtils,
  ) {
    this.translationLoaderService.loadTranslations(lngEnglish, lngBangla);

  }

  ngOnInit(): void {
    this.userInfo = this.localStoreUtils.getUserInfo();
    // if(this.userInfo.empPersonalInfoId){
    //   this.getEmpInfoById();
    // }

  }

  // -----------------------------------------------------------------------------------------------------
  // @ API calling
  // -----------------------------------------------------------------------------------------------------




  // -----------------------------------------------------------------------------------------------------
  // @ view method
  // -----------------------------------------------------------------------------------------------------


  // -----------------------------------------------------------------------------------------------------
  // @ Helper Method
  // -----------------------------------------------------------------------------------------------------






}
