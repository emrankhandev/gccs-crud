import {Injectable} from '@angular/core';
import {MatDialog, MatDialogConfig} from "@angular/material/dialog";
import {AppUtils} from "./app.utils";
import {ApprovalUser} from "../../module/super-admin/approve-control/model/approval-user";
import {
  ApprovalMonitorDialogComponent
} from "../../shared/component/approval/approval-monitor-dialog/approval-monitor-dialog.component";
import {ApprovalHistoryService} from "../../module/super-admin/approve-control/service/approval-history.service";
import {
  ApprovalConfigurationService
} from "../../module/super-admin/approve-control/service/approval-configuration.service";
import {ApprovalHistory} from "../../module/super-admin/approve-control/model/approval-history";
import {LocalStoreUtils} from "./local-store.utils";
import {SnackbarHelper} from "./snackbar.utils";
import {ApprovalTransactionTypeService} from "../mock-data/approval-transaction-type.service";

@Injectable({
    providedIn: 'root'
})
export class ApprovalSubmitUtils {

  isLoadingLoad: boolean = false;

  constructor(
    private matDialog: MatDialog,
    private appUtils: AppUtils,
    public localStoreUtils : LocalStoreUtils,
    private snackbarHelper: SnackbarHelper,
    private approvalTransactionTypeService: ApprovalTransactionTypeService,
  ) {}



  openAccountTransactionApprovalSubmitDialog(transactionData:any, departmentId:number,approvalTransactionTypeId: number, approveUser: ApprovalUser, callbackFunction?: any){
    // const dialogConfig: MatDialogConfig = this.appUtils.getDialogConfig('60%', 'auto');
    // dialogConfig.data = {
    //   transactionData: transactionData,
    //   departmentId: departmentId,
    //   approveUser: approveUser,
    //   approvalTransactionTypeId: approvalTransactionTypeId,
    // };
    // const dialogRef = this.matDialog.open(AccountTransactionApproveDialogComponent,dialogConfig );
    // dialogRef.componentInstance.callBackMethod.subscribe(value => {
    //   if (callbackFunction) { callbackFunction();}
    // });
  }

  openSalaryApprovalSubmitDialog(transactionData:any, departmentId:number,approvalTransactionTypeId: number, approveUser: ApprovalUser, callbackFunction?: any){
    // const dialogConfig: MatDialogConfig = this.appUtils.getDialogConfig('60%', 'auto');
    // dialogConfig.data = {
    //   transactionData: transactionData,
    //   departmentId: departmentId,
    //   approveUser: approveUser,
    //   approvalTransactionTypeId: approvalTransactionTypeId,
    // };
    // const dialogRef = this.matDialog.open(SalaryApproveDialogComponent,dialogConfig );
    // dialogRef.componentInstance.callBackMethod.subscribe(value => {
    //   if (callbackFunction) { callbackFunction();}
    // });
  }

  openBonusApprovalSubmitDialog(transactionData:any, departmentId:number,approvalTransactionTypeId: number, approveUser: ApprovalUser, callbackFunction?: any){
    // const dialogConfig: MatDialogConfig = this.appUtils.getDialogConfig('60%', 'auto');
    // dialogConfig.data = {
    //   transactionData: transactionData,
    //   departmentId: departmentId,
    //   approveUser: approveUser,
    //   approvalTransactionTypeId: approvalTransactionTypeId,
    // };
    // const dialogRef = this.matDialog.open(BonusApproveDialogComponent,dialogConfig );
    // dialogRef.componentInstance.callBackMethod.subscribe(value => {
    //   if (callbackFunction) { callbackFunction();}
    // });
  }


  openApprovalMonitorDialog(approvalHistoryId: any): void{
    console.log(approvalHistoryId)
    const dialogConfig: MatDialogConfig = this.appUtils.getDialogConfig('60%', 'auto');
    dialogConfig.data = {
      approvalHistoryId:  approvalHistoryId,
    };
    const dialogRef = this.matDialog.open(ApprovalMonitorDialogComponent,dialogConfig );
    dialogRef.componentInstance.callBackMethod.subscribe(value => {
      // do something
    });
  }





  // -----------------------------------------------------------------------------------------------------
  // @ submit form edit page
  // -----------------------------------------------------------------------------------------------------
  onClickSubmit(approvalHistoryService: ApprovalHistoryService, approvalConfigService: ApprovalConfigurationService, historyId: number, callbackFunction?: any, updatedAmount?: number): void{
    approvalHistoryService.getObjectById(historyId).subscribe(res => {
      console.log(res.data)
      const approvalHistory : ApprovalHistory = res.data;
      approvalHistory.isSeen = true;
      approvalHistoryService.update(approvalHistory).subscribe();
      /*loader start here*/
      this.isLoadingLoad = true;
      approvalConfigService.getSubmitUserByDepartmentAndAppUserId(Number(approvalHistory.departmentId), Number(this.localStoreUtils.getUserInfo().id), Number(approvalHistory.transactionTypeId)).subscribe(res => {
        /*loader start end*/
        this.isLoadingLoad = false;
        const approveUser: ApprovalUser = res.data;
        if(approveUser){
          this.openSubmitDialog(approvalHistory, approveUser, callbackFunction);
        }else {
          this.appUtils.showErrorMessage(this.snackbarHelper.PERMISSION_MSG_BN,this.snackbarHelper.PERMISSION_MSG);
        }
      });
    });
  }


  openSubmitDialog(approvalHistory: ApprovalHistory, approveUser: ApprovalUser, callbackFunction?: any, updatedAmount?: number): void{
    const transactionData = {
      transactionId : approvalHistory.transactionId,
      approvalHistoryId : approvalHistory.id,
      totalAmount : updatedAmount ? updatedAmount : approvalHistory.totalAmount,
      transactionDescription : approvalHistory.transactionDescription,
    }
    console.log(approvalHistory.totalAmount)
     if(approvalHistory.transactionTypeId === this.approvalTransactionTypeService.JOURNAL_ENTRY){
      this.openAccountTransactionApprovalSubmitDialog(transactionData, Number(approvalHistory.departmentId), Number(approvalHistory.approvalTransactionTypeId), approveUser, callbackFunction);
    }

  }


}
