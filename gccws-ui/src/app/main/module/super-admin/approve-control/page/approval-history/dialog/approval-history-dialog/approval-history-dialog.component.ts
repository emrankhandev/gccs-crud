import {Component, EventEmitter, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {locale as lngEnglish} from "../../i18n/en";
import {locale as lngBangla} from "../../i18n/bn";
import {TranslationLoaderService} from "../../../../../../../core/service/translation-loader.service";
import {AppUtils} from "../../../../../../../core/utils/app.utils";
import {ApprovalConfiguration} from "../../../../model/approval-configuration";
import {ApprovalConfigurationService} from "../../../../service/approval-configuration.service";
import {DropdownModel} from "../../../../../../../core/model/dropdown-model";
import {ApprovalTeamService} from "../../../../service/approval-team.service";
import {ApprovalTeamDetails} from "../../../../model/approval-team-details";
import {MenuItemService} from "../../../../../service/menu-item.service";
import {AppUserService} from "../../../../../service/app-user.service";

import {ApprovalTeamModel} from "../../../../model/approval-team-model";
import {StatusType, StatusTypeService} from "../../../../../../../core/mock-data/status-type.service";
import {
  ApprovalTransactionType,
  ApprovalTransactionTypeService
} from "../../../../../../../core/mock-data/approval-transaction-type.service";
import {ApprovalHistory} from "../../../../model/approval-history";
import {MatTableDataSource} from "@angular/material/table";
import {ApprovalHistoryService} from "../../../../service/approval-history.service";
import {LocalStoreUtils} from "../../../../../../../core/utils/local-store.utils";
import {AppUser} from "../../../../../model/app-user";
import {ApprovalSubmitUtils} from "../../../../../../../core/utils/approval-submit.utils";

@Component({
    selector: 'app-approval-history-dialog',
    templateUrl: './approval-history-dialog.component.html',
    styleUrls: ['./approval-history-dialog.component.scss']
})
export class ApprovalHistoryDialogComponent implements OnInit {

  /*property*/
  callBackMethod: EventEmitter<boolean> = new EventEmitter<boolean>();
  isEdit: boolean;
  isModuleId: boolean = true;
  isLoading: boolean = false;

  /*form*/
  frmGroup: FormGroup;
  isShowLoadingData:boolean = false;
  /*dataTable*/
  dataSource = new MatTableDataSource(new Array<ApprovalHistory>());
  displayedColumns: string[] = ['sl', 'time', 'transactionType',  'fromAppUser',  'transactionDescription', 'status',  'action'];

  /*list*/
  approvalHistoryList: ApprovalHistory[] = new Array<ApprovalHistory>();

  /*extra*/
  appUser: AppUser;
  isLoadingLoad: boolean = false;


  // -----------------------------------------------------------------------------------------------------
  // @ Init
  // -----------------------------------------------------------------------------------------------------
  constructor(
    public dialogRef: MatDialogRef<ApprovalHistoryDialogComponent>,
    @Inject(MAT_DIALOG_DATA) data: any,
    private translationLoaderService: TranslationLoaderService,
    private formBuilder: FormBuilder,
    private approvalHistoryService: ApprovalHistoryService,
    public appUtils : AppUtils,
    public localStoreUtils : LocalStoreUtils,
    public approvalSubmitUtils : ApprovalSubmitUtils,
  ) {
    this.translationLoaderService.loadTranslations(lngEnglish, lngBangla);
  }

  ngOnInit(): void {
    this.appUser = this.localStoreUtils.getUserInfo();
    this.setFormInitValue();

  }

  // -----------------------------------------------------------------------------------------------------
  // @ API calling
  // -----------------------------------------------------------------------------------------------------
  getLoadListData() {
    const fromDateValue = this.frmGroup.value.fromDate ? this.frmGroup.value.fromDate : this.appUtils.COMPARE_DATE;
    const toDateValue = this.frmGroup.value.toDate ? this.frmGroup.value.toDate : this.appUtils.COMPARE_DATE;
    this.isLoadingLoad = true;

    this.approvalHistoryService.getLoadListData(Number(this.appUser.id),fromDateValue,toDateValue).subscribe(res => {
      this.isLoadingLoad = false;
      this.approvalHistoryList = res.data;
      if(this.approvalHistoryList.length > 0){
        this.isShowLoadingData = true;
      }
      this.dataSource = new MatTableDataSource( this.approvalHistoryList);
    });
  }

  // -----------------------------------------------------------------------------------------------------
  // @ view method
  // -----------------------------------------------------------------------------------------------------

  closeDialog(): void {
      this.dialogRef.close();
  }

  clearFormData(): any {
    this.setFormInitValue();
    this.isEdit = false;
  }

  refresh() {
    this.approvalHistoryList = [];
    this.isShowLoadingData = false;
  }
  // -----------------------------------------------------------------------------------------------------
  // @ Helper Method
  // -----------------------------------------------------------------------------------------------------

  // -----------------------------------------------------------------------------------------------------
  // @ Utils
  // -----------------------------------------------------------------------------------------------------

  setFormInitValue(): any {
    this.frmGroup = this.formBuilder.group({
      fromDate: [this.appUtils.getCurrentDate(), Validators.required],
      toDate: [this.appUtils.getCurrentDate(), Validators.required],
    });
  }
  }

