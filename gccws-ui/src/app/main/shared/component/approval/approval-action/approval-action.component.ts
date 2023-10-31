import {Component, EventEmitter, Inject, Input, OnInit, Output} from '@angular/core';
import {locale as lngEnglish} from './i18n/en';
import {locale as lngBangla} from './i18n/bn';
import {TranslationLoaderService} from "../../../../core/service/translation-loader.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {
  ApprovalTransactionType,
  ApprovalTransactionTypeService
} from "../../../../core/mock-data/approval-transaction-type.service";
import {AppUtils} from "../../../../core/utils/app.utils";
import {LocalStoreUtils} from "../../../../core/utils/local-store.utils";
import {ApprovalStatus, ApprovalStatusService} from "../../../../core/mock-data/approval-status.service";
import {SnackbarHelper} from "../../../../core/utils/snackbar.utils";

import {MatDialog} from "@angular/material/dialog";
import {ApprovalUser} from "../../../../module/super-admin/approve-control/model/approval-user";
import {ApprovalHistory} from "../../../../module/super-admin/approve-control/model/approval-history";
import {DropdownModel} from "../../../../core/model/dropdown-model";
import {ApprovalTeamDetails} from "../../../../module/super-admin/approve-control/model/approval-team-details";
import {
  ApprovalConfigurationService
} from "../../../../module/super-admin/approve-control/service/approval-configuration.service";
import {ApprovalTeamService} from "../../../../module/super-admin/approve-control/service/approval-team.service";
import {ApprovalHistoryService} from "../../../../module/super-admin/approve-control/service/approval-history.service";
import {ApprovalConfiguration} from "../../../../module/super-admin/approve-control/model/approval-configuration";

@Component({
    selector: 'app-approval-action',
    templateUrl: './approval-action.component.html',
    styleUrls: ['./approval-action.component.scss']
})
export class ApprovalActionComponent implements OnInit {

  @Input() moduleId: number;
  @Input() departmentId: number;
  @Input() approvalTransactionTypeId: number;
  @Input() approveUser: ApprovalUser;
  @Input() approvalTransactionType: ApprovalTransactionType;
  @Output() dialogCallBackMethod = new EventEmitter<void>();

  /*property*/
  isLoading: boolean = false;

  /*form*/
  frmGroup: FormGroup;
  model: ApprovalHistory = new ApprovalHistory();
  approvalConfigurationModel: ApprovalConfiguration = new ApprovalConfiguration();

  /*dropdownList*/
  approvalTeamDropdownList: DropdownModel[] = new Array<DropdownModel>();
  notifyUserDropdownList: ApprovalTeamDetails[] = new Array<ApprovalTeamDetails>();
  approvalStatusList: ApprovalStatus[] = new Array<ApprovalStatus>();

  /*extra*/
  userInfo: any;
  isWorkOnBack = false;
  isWorkOnReject = false;
  toAmount?:number;
  fromAmount?:number;
  totalAmount?:number;

  /*permission property*/
  isDisabled: boolean = false;
  hasBackPermission: boolean = true;

    constructor(
      private  matDialog: MatDialog,
      private  _fuseTranslationLoaderService: TranslationLoaderService,
      private  approvalConfigService: ApprovalConfigurationService,
      private  approvalTransactionTypeService: ApprovalTransactionTypeService,
      private  approvalTeamService: ApprovalTeamService,
      private  approvalHistoryService: ApprovalHistoryService,
      public   approvalStatusService: ApprovalStatusService,
      private  formBuilder: FormBuilder,
      private  appUtils: AppUtils,
      private  localStoreUtils: LocalStoreUtils,
      private  snackbarHelper: SnackbarHelper,
    ) {
        this._fuseTranslationLoaderService.loadTranslations(lngEnglish, lngBangla);
    }

    ngOnInit(): void {
      this.userInfo = this.localStoreUtils.getUserInfo();
      this.getApprovalStatusList();
      this.setFormInitValue();
      this.getNextTeamList();
      this.getAmountByToTeamAndModuleId();
      if(this.approvalTransactionType.approvalHistoryId){
        this.getApprovalHistoryById(this.approvalTransactionType.approvalHistoryId);
      }
      this.totalAmount = this.approvalTransactionType.totalAmount;
    }

  // -----------------------------------------------------------------------------------------------------
  // @ API calling
  // -----------------------------------------------------------------------------------------------------

  getApprovalStatusList(): any {
    this.approvalStatusList = this.approvalStatusService.getList();
  }

  getAmountByToTeamAndModuleId(): any {
    this.approvalConfigService.getByToTeamAndDepartmentId( this.approveUser.currentTeamId, this.departmentId, this.approvalTransactionType.id).subscribe(res => {
       this.approvalConfigurationModel = res.data;
       if(this.approvalConfigurationModel){
         this.hasBackPermission = this.approvalConfigurationModel.backPermission;
         this.isDisabled = this.approvalConfigurationModel.changePermission;
       }
    });
  }


  getNextTeamList(): any {
    this.approvalTeamService.getNextTeamByDepartmentAndCurrentTeamId(this.departmentId, this.approvalTransactionTypeId, this.approveUser.currentTeamId).subscribe(res => {
      this.approvalTeamDropdownList = res.data;
      if(this.approvalTeamDropdownList.length > 0){
        /*now set value in list*/
        const selectValue =  this.approvalTeamDropdownList.find(model => model.id === this.approveUser.toTeamId);
        this.frmGroup.patchValue({
          approvalTeam: selectValue,
        });
        this.onApproveTeamChange();

        this.approvalTeamField?.addValidators(Validators.required);
        this.notifyUserField?.addValidators(Validators.required);
      }else {
        this.approvalTeamField?.clearValidators();
        this.notifyUserField?.clearValidators();
      }

      this.approvalTeamField?.updateValueAndValidity();
      this.notifyUserField?.updateValueAndValidity();
    });
  }

  getPreviousTeamList(): any {
    this.approvalTeamService.getPreviousTeamByDepartmentAndCurrentTeamId(this.departmentId, this.approvalTransactionTypeId, this.approveUser.currentTeamId).subscribe(res => {
      this.approvalTeamDropdownList = res.data;
      if(this.approvalTeamDropdownList.length > 0){
        /*get previous submit team*/
        const previousSubmitTeam = this.approvalTeamDropdownList.find(model => model.id === this.model.fromTeamId);
        /*now set value in list*/
        this.frmGroup.patchValue({
          approvalTeam: previousSubmitTeam ? previousSubmitTeam : '',
        });
        this.onApproveTeamChange();
        this.approvalTeamField?.addValidators(Validators.required);
        this.notifyUserField?.addValidators(Validators.required);
      }else {
        this.appUtils.showErrorMessage(this.snackbarHelper.PERMISSION_MSG_BN,this.snackbarHelper.PERMISSION_MSG);
        this.frmGroup.patchValue({
          needToBack: false
        });
      }
      this.approvalTeamField?.updateValueAndValidity();
      this.notifyUserField?.updateValueAndValidity();
    });
  }

  getTeamById(teamId: number, notifyUserId:number): any {
    this.approvalTeamService.getObjectById(teamId).subscribe(res => {
      this.notifyUserDropdownList = res.data.detailsList.map(m => ({
        ...m,
        id: m.appUserId,
        name :m.appUserName + " - " + m.displayName,
      }));

      /*now set value in list*/
      let selectValue = null;
      if(!this.isWorkOnBack){
        selectValue =  this.notifyUserDropdownList.find(model => model.id === notifyUserId);
      }else {
        selectValue =  this.notifyUserDropdownList.find(model => model.id === this.model.fromAppUserId);
      }
      this.frmGroup.patchValue({
        notifyUser: selectValue ? selectValue : this.notifyUserDropdownList[0],
      });
    });
  }

  getApprovalHistoryById(historyId: any): any {
    this.approvalHistoryService.getObjectById(historyId).subscribe(res => {
      this.model = res.data;
    });
  }

  openSubmitConfirmationDialog(): void{
    this.appUtils.openConfirmationDialog("Do you want to Submit?", this.onSubmit.bind(this));
  }

  onSubmit(): any {
    this.generateModel();
    this.isLoading = true;
    console.log(this.model);
    this.approvalHistoryService.create(this.model).subscribe({
      next: (res) => {
        this.isLoading = false;
        this.appUtils.onServerResponse(res, this.dialogCallBackMethod.emit());
      },
      error: (error) => {
        this.isLoading = false;
        this.appUtils.onServerErrorResponse(error);
      }
    });
  }

  openForwardConfirmationDialog(): void{
    this.approvalHistoryService.getObjectById(Number(this.model.id)).subscribe(res => {
      console.log(res.data);
      if(res.data.isClose){
        this.appUtils.showErrorMessage("Transaction Already Completed", "Transaction Already Completed");
      }else {
        this.appUtils.openConfirmationDialog("Do you want to Forward?", this.onForward.bind(this));
      }
    });
  }

  onForward(): any {
    this.modifyModule(this.approvalStatusService.FORWARD)
    this.isLoading = true;
    this.approvalHistoryService.create(this.model).subscribe({
      next: (res) => {
        this.isLoading = false;
        this.appUtils.onServerResponse(res, this.dialogCallBackMethod.emit());
      },
      error: (error) => {
        this.isLoading = false;
        this.appUtils.onServerErrorResponse(error);
      }
    });
  }

  openApproveConfirmationDialog(): void{
    this.approvalHistoryService.getObjectById(Number(this.model.id)).subscribe(res => {
      if(res.data.isClose){
        this.appUtils.showErrorMessage("Transaction Already Completed", "Transaction Already Completed");
      }else {
        this.appUtils.openConfirmationDialog("Do you want to Approve?", this.onApprove.bind(this));
      }
    });
  }

  onApprove(): any {
    this.modifyModule(this.approvalStatusService.APPROVED);
    this.model.toTeamId =  undefined;
    this.model.defaultAppUserId = undefined;
    this.isLoading = true;
    this.approvalHistoryService.create(this.model).subscribe({
      next: (res) => {
        this.isLoading = false;
        this.appUtils.onServerResponse(res, this.dialogCallBackMethod.emit());
      },
      error: (error) => {
        this.isLoading = false;
        this.appUtils.onServerErrorResponse(error);
      }
    });
  }

  openBackConfirmationDialog(): void{
    this.approvalHistoryService.getObjectById(Number(this.model.id)).subscribe(res => {
      if(res.data.isClose){
        this.appUtils.showErrorMessage("Transaction Already Completed", "Transaction Already Completed");
      }else {
        this.appUtils.openConfirmationDialog("Do you want to Back?", this.onBack.bind(this));
      }
    });
  }

  onBack(): any {
    this.modifyModule(this.approvalStatusService.BACK);
    this.isLoading = true;
    this.approvalHistoryService.create(this.model).subscribe({
      next: (res) => {
        this.isLoading = false;
        this.appUtils.onServerResponse(res, this.dialogCallBackMethod.emit());
      },
      error: (error) => {
        this.isLoading = false;
        this.appUtils.onServerErrorResponse(error);
      }
    });
  }

  openRejectConfirmationDialog(): void{
    this.approvalHistoryService.getObjectById(Number(this.model.id)).subscribe(res => {
      if(res.data.isClose){
        this.appUtils.showErrorMessage("Transaction Already Completed", "Transaction Already Completed");
      }else {
        this.appUtils.openConfirmationDialog("Do you want to Reject?", this.onReject.bind(this));
      }
    });
  }

  onReject(): any {
    this.modifyModule(this.approvalStatusService.REJECT);
    this.model.toTeamId =  undefined;
    this.model.defaultAppUserId = undefined;
    this.isLoading = true;
    // console.log(this.model)
    this.approvalHistoryService.create(this.model).subscribe({
      next: (res) => {
        this.isLoading = false;
        this.appUtils.onServerResponse(res, this.dialogCallBackMethod.emit());
      },
      error: (error) => {
        this.isLoading = false;
        this.appUtils.onServerErrorResponse(error);
      }
    });
  }

  // -----------------------------------------------------------------------------------------------------
  // @ view method
  // -----------------------------------------------------------------------------------------------------
  onApproveTeamChange(): void{
    const selectedTeam = this.frmGroup.value.approvalTeam;
    if(!selectedTeam){
      return;
    }
    this.getTeamById(selectedTeam.id,selectedTeam.extra );
  }

  onChangeNeedToBack(): void{
    this.isWorkOnBack = this.frmGroup.value.needToBack;
    if(this.isWorkOnBack){
      this.getPreviousTeamList();
    }else {
      this.getNextTeamList();
    }
  }

  onChangeNeedToReject(): void{
    this.isWorkOnReject = this.frmGroup.value.needToReject;
  }

  onCheckValidAmount():void{
    if(this.isNeedToCheckAmount()){
      if(this.isValidForApprove()){
        this.openApproveConfirmationDialog();
      }
      else{
        this.appUtils.showErrorMessage("পরিমাণ পরিসীমা বৈধ নয়", "Amount range not valid");
      }
    }else{
      this.openApproveConfirmationDialog();
    }
  }

  isNeedToCheckAmount():boolean{
    if(this.totalAmount && this.totalAmount > 0 && this.approvalConfigurationModel &&
      this.approvalConfigurationModel.fromAmount && this.approvalConfigurationModel.fromAmount > 0  &&
      this.approvalConfigurationModel.toAmount && this.approvalConfigurationModel.toAmount > 0
    ){
      return  true;
    }else{
      return  false;
    }
  }

  isValidForApprove():boolean{
    if(this.approvalConfigurationModel &&
      Number(this.approvalConfigurationModel.fromAmount) <= Number(this.totalAmount)    &&
      Number(this.totalAmount)  <= Number(this.approvalConfigurationModel.toAmount)
    ){
      return true;
    }
    return  false;
  }



  // -----------------------------------------------------------------------------------------------------
  // @ Helper Method
  // -----------------------------------------------------------------------------------------------------
  setFormInitValue(): any {
    this.frmGroup = this.formBuilder.group({
      needToBack: [false, ''],
      needToReject: [false, ''],
      approvalTeam: ['', Validators.required],
      notifyUser: ['', Validators.required],
      approvalComment: ['', ''],
    });
  }

  get approvalTeamField(){
      return this.frmGroup.get('approvalTeam');
  }

  get notifyUserField(){
      return this.frmGroup.get('notifyUser');
  }

  generateModel(): any{
    this.model = new ApprovalHistory();
    this.model.id = undefined;

    this.model.departmentId = this.departmentId;
    // @ts-ignore
    const transactionType: ApprovalTransactionType = this.approvalTransactionTypeService.getList().find(model => model.id === this.approvalTransactionTypeId);
    this.model.approvalTransactionTypeId = transactionType.id;
    this.model.approvalTransactionTypeName = transactionType.name;
    this.model.transactionId = this.approvalTransactionType.transactionId;
    this.model.transactionTypeId = this.approvalTransactionType.id;
    this.model.transactionTypeName = this.approvalTransactionType.name;
    this.model.transactionTableName = this.approvalTransactionType.tableName;
    this.model.transactionDescription = this.approvalTransactionType.transactionDescription;
    // this.model.routerUrl = this.approvalTransactionType.routerUrl;
    this.model.fromTeamId = this.approveUser.currentTeamId;
    this.model.toTeamId =  this.frmGroup.value.approvalTeam ? this.frmGroup.value.approvalTeam.id : '';
    this.model.fromAppUserId = this.userInfo.id;
    this.model.defaultAppUserId = this.frmGroup.value.notifyUser ? this.frmGroup.value.notifyUser.id : '';
    // @ts-ignore
    const approvalStatus : ApprovalStatus =this.approvalStatusList.find(model=>model.id=== this.approvalStatusService.SUBMIT);
    this.model.approvalStatusId = approvalStatus.id;
    this.model.approvalStatusName = approvalStatus.name;

    this.model.totalAmount = this.approvalTransactionType.totalAmount;
    this.model.approvalComment = this.frmGroup.value.approvalComment;
    this.model.active = true;
  }

  modifyModule(approvalType: number){
    this.model.fromTeamId = this.approveUser.currentTeamId;
    this.model.fromAppUserId = this.userInfo.id;
    this.model.toTeamId =  this.frmGroup.value.approvalTeam ? this.frmGroup.value.approvalTeam.id : '';
    this.model.defaultAppUserId = this.frmGroup.value.notifyUser ? this.frmGroup.value.notifyUser.id : '';
    this.model.approvalComment = this.frmGroup.value.approvalComment;
    // @ts-ignore
    const approvalStatus : ApprovalStatus =this.approvalStatusList.find(model=>model.id=== approvalType);
    this.model.approvalStatusId = approvalStatus.id;
    this.model.approvalStatusName = approvalStatus.name;
  }


}
