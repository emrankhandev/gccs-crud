import {Component, OnInit} from '@angular/core';
import {MatTableDataSource} from "@angular/material/table";
import {locale as lngEnglish} from '../i18n/en';
import {locale as lngBangla} from '../i18n/bn';
import {ApprovalHistory} from "../../../model/approval-history";
import {TranslationLoaderService} from "../../../../../../core/service/translation-loader.service";
import {ApprovalHistoryService} from "../../../service/approval-history.service";
import {AppUtils} from "../../../../../../core/utils/app.utils";
import {LocalStoreUtils} from "../../../../../../core/utils/local-store.utils";
import {
  ApprovalTransactionType,
  ApprovalTransactionTypeService
} from "../../../../../../core/mock-data/approval-transaction-type.service";
import {ApprovalSubmitUtils} from "../../../../../../core/utils/approval-submit.utils";
import {ApprovalConfigurationService} from "../../../service/approval-configuration.service";
import {ApprovalUser} from "../../../model/approval-user";
import {ApprovalStatusService} from "../../../../../../core/mock-data/approval-status.service";
import {SnackbarHelper} from "../../../../../../core/utils/snackbar.utils";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AppUser} from "../../../../model/app-user";
import {ApprovalConfiguration} from "../../../model/approval-configuration";
import {
  ApprovalConfigurationAddDialogComponent
} from "../../approval-configuration/dialog/approval-configuration-add-dialog/approval-configuration-add-dialog.component";
import {MatDialog} from "@angular/material/dialog";
import {ApprovalHistoryDialogComponent} from "../dialog/approval-history-dialog/approval-history-dialog.component";


@Component({
  selector: 'app-account-transaction-list',
  templateUrl: './approval-history-list.component.html',
  styleUrls: ['./approval-history-list.component.scss'],
})
export class ApprovalHistoryListComponent implements OnInit{

  /*property*/
  frmGroup: FormGroup;
  isShowLoadingData:boolean = false;
  isLoaderLoading: boolean = false;

  /*dataTable*/
  dataSource = new MatTableDataSource(new Array<ApprovalHistory>());
  displayedColumns: string[] = ['sl', 'time',  'transactionType',  'fromAppUser',  'transactionDescription', 'status',  'action'];

  /*list*/
  approvalHistoryList: ApprovalHistory[] = new Array<ApprovalHistory>();

  /*dropdownlist*/
  approvalTransactionTypeDropdownList: ApprovalTransactionType[] = new Array<ApprovalTransactionType>();

  /*extra*/
  appUser: AppUser;
  isLoadingLoad: boolean = false;

  constructor(
    private formBuilder: FormBuilder,
    private matDialog: MatDialog,
    private translationLoaderService: TranslationLoaderService,
    private approvalHistoryService: ApprovalHistoryService,
    private approvalTransactionTypeService: ApprovalTransactionTypeService,
    private approvalConfigService: ApprovalConfigurationService,
    private approvalStatusService: ApprovalStatusService,
    public appUtils : AppUtils,
    public localStoreUtils : LocalStoreUtils,
    public approvalSubmitUtils : ApprovalSubmitUtils,
    private snackbarHelper: SnackbarHelper,
  ) {
    this.translationLoaderService.loadTranslations(lngEnglish, lngBangla);
  }

  // -----------------------------------------------------------------------------------------------------
  // @ Init
  // -----------------------------------------------------------------------------------------------------

  ngOnInit(): void {
    this.appUser = this.localStoreUtils.getUserInfo();
    this.setFormInitValue();
    this.getApprovalTransactionTypeList();
    this.getApprovalHistory();
  }

  // -----------------------------------------------------------------------------------------------------
  // @ API calling
  // -----------------------------------------------------------------------------------------------------

  getApprovalTransactionTypeList(): any{
    this.approvalTransactionTypeDropdownList = this.approvalTransactionTypeService.getList();
  }

  getApprovalHistory(): any {
    const fromDate = this.frmGroup.value.fromDate ;
    const toDate = this.frmGroup.value.toDate ;
    const approvalTransactionTypeValue = this.frmGroup.value.approvalTransactionType ? this.frmGroup.value.approvalTransactionType.id : 0;
    if(!fromDate || !toDate){
      return;
    }
    this.isLoaderLoading = true;
    this.approvalHistoryService.getApprovalPendingList(Number(this.appUser.id), fromDate, toDate, approvalTransactionTypeValue).subscribe(res => {
      this.isLoaderLoading = false;
      this.approvalHistoryList = res.data;
      this.dataSource = new MatTableDataSource( this.approvalHistoryList);
    });
  }

  onClickSubmit(approvalHistory: ApprovalHistory): void{
    this.approvalHistoryService.getObjectById(Number(approvalHistory.id)).subscribe(res => {
      res.data.isSeen = true;
      this.approvalHistoryService.update(res.data).subscribe();
    });
    if(approvalHistory.approvalStatusId === this.approvalStatusService.BACK){
      // loader start here
      this.isLoadingLoad = true;
      this.approvalConfigService.getSubmitUserByDepartmentAndAppUserId(Number(approvalHistory.departmentId), Number(this.appUser.id), Number(approvalHistory.transactionTypeId)).subscribe(res => {
        // loader start end
        this.isLoadingLoad = false;
        const approveUser: ApprovalUser = res.data;
        if(approveUser){
          this.openSubmitDialog(approvalHistory, approveUser);
        }else {
          this.appUtils.showErrorMessage(this.snackbarHelper.PERMISSION_MSG_BN,this.snackbarHelper.PERMISSION_MSG);
        }
      });
    }else {
      // loader start here
      this.isLoadingLoad = true;
      this.approvalConfigService.getApproveAndForwardUserByDepartmentAndAppUserId(Number(approvalHistory.departmentId), Number(this.appUser.id), Number(approvalHistory.transactionTypeId)).subscribe(res => {
        // loader start end
        this.isLoadingLoad = false;
        const approveUser: ApprovalUser = res.data;
        if(approveUser){
          this.openSubmitDialog(approvalHistory, approveUser);
        }else {
          this.appUtils.showErrorMessage(this.snackbarHelper.PERMISSION_MSG_BN,this.snackbarHelper.PERMISSION_MSG);
        }
      });
    }
  }

  // -----------------------------------------------------------------------------------------------------
  // @ view method
  // -----------------------------------------------------------------------------------------------------

  openAddDialog( model?: ApprovalHistory, ): void{
    model = model ? model : new ApprovalHistory();
    const dialogConfig = this.appUtils.getDialogConfig('80%', 'auto');
    dialogConfig.data = {

    };
    const dialogRef = this.matDialog.open(ApprovalHistoryDialogComponent, dialogConfig);
    dialogRef.componentInstance.callBackMethod.subscribe(value => {
     // this.getPageableModelList();
    });
  }


  openSubmitDialog(approvalHistory: ApprovalHistory, approveUser: ApprovalUser): void{
    const transactionData = {
      transactionId : approvalHistory.transactionId,
      approvalHistoryId : approvalHistory.id,
      totalAmount : approvalHistory.totalAmount,
      transactionDescription : approvalHistory.transactionDescription,
    }
    if(approvalHistory.transactionTypeId === this.approvalTransactionTypeService.JOURNAL_ENTRY){
      this.approvalSubmitUtils.openAccountTransactionApprovalSubmitDialog(transactionData, Number(approvalHistory.departmentId), Number(approvalHistory.approvalTransactionTypeId), approveUser, this.getApprovalHistory.bind(this));
    }
    else if(approvalHistory.transactionTypeId === this.approvalTransactionTypeService.SALARY_APPROVAL){
      this.approvalSubmitUtils.openSalaryApprovalSubmitDialog(transactionData, Number(approvalHistory.departmentId), Number(approvalHistory.approvalTransactionTypeId), approveUser, this.getApprovalHistory.bind(this));
    }
    else if(approvalHistory.transactionTypeId === this.approvalTransactionTypeService.BONUS_APPROVAL){
      this.approvalSubmitUtils.openBonusApprovalSubmitDialog(transactionData, Number(approvalHistory.departmentId), Number(approvalHistory.approvalTransactionTypeId), approveUser, this.getApprovalHistory.bind(this));
    }


  }

  refresh() {
    this.frmGroup.patchValue({
      fromDate: this.appUtils.getFirstDateOfCurrentMonth(),
      toDate: this.appUtils.getCurrentDate(),
      approvalTransactionType: ''
    });
    this.dataSource.data = [];
  }

  // -----------------------------------------------------------------------------------------------------
  // @ Utils
  // -----------------------------------------------------------------------------------------------------

  setFormInitValue(): any {
    this.frmGroup = this.formBuilder.group({
      fromDate: [this.appUtils.getFirstDateOfCurrentMonth(), Validators.required],
      toDate: [this.appUtils.getCurrentDate(), Validators.required],
      approvalTransactionType: ['', ''],
    });
  }


}
