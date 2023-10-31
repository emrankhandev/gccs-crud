import {Component, EventEmitter, Inject, OnInit} from '@angular/core';
import {FormBuilder} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {locale as lngEnglish} from "./i18n/en";
import {locale as lngBangla} from "./i18n/bn";
import {TranslationLoaderService} from "../../../../core/service/translation-loader.service";

import {ApprovalStatusService} from "../../../../core/mock-data/approval-status.service";
import {ApprovalHistory} from "../../../../module/super-admin/approve-control/model/approval-history";
import {ApprovalHistoryService} from "../../../../module/super-admin/approve-control/service/approval-history.service";


@Component({
  selector: 'app-approval-monitor-dialog',
  templateUrl: './approval-monitor-dialog.component.html',
  styleUrls: ['./approval-monitor-dialog.component.scss']
})
export class ApprovalMonitorDialogComponent implements OnInit {

  /*property*/
  callBackMethod: EventEmitter<boolean> = new EventEmitter<boolean>();
  approvalHistoryId: any;
  approvalHistoryList: ApprovalHistory[] = new Array<ApprovalHistory>();


  // -----------------------------------------------------------------------------------------------------
  // @ Init
  // -----------------------------------------------------------------------------------------------------
  constructor(
    public dialogRef: MatDialogRef<ApprovalMonitorDialogComponent>,
    @Inject(MAT_DIALOG_DATA) data: any,
    private formBuilder: FormBuilder,
    private translationLoaderService: TranslationLoaderService,
    private approvalHistoryService: ApprovalHistoryService,
    public approvalStatusService: ApprovalStatusService,

  ) {
    this.translationLoaderService.loadTranslations(lngEnglish, lngBangla);
    this.approvalHistoryId = data.approvalHistoryId;
  }

  ngOnInit(): void {
    this.getHistoryList();
  }

  // -----------------------------------------------------------------------------------------------------
  // @ API calling
  // -----------------------------------------------------------------------------------------------------

  getHistoryList(): any {
    this.approvalHistoryService.getByApprovalHistoryId(this.approvalHistoryId).subscribe(res => {
      this.approvalHistoryList = res.data;
      const tempList = new Array<ApprovalHistory>();
      res.data.forEach(value => {
        tempList.push(this.approvalStatus(value));
      });
    });
  }


  // -----------------------------------------------------------------------------------------------------
  // @ view method
  // -----------------------------------------------------------------------------------------------------

  closeDialog(): void {
    this.dialogRef.close();
  }

  // @ts-ignore
  approvalStatus(approvalHistory: ApprovalHistory): ApprovalHistory{
    if(approvalHistory.approvalStatusId === this.approvalStatusService.SUBMIT ){
      approvalHistory.fromHeading = 'from';
      approvalHistory.iconColor = 'notify-img';
    }
    else if(approvalHistory.approvalStatusId === this.approvalStatusService.FORWARD){
      approvalHistory.fromHeading = 'from';
      approvalHistory.iconColor = 'forward';
    }
    else if(approvalHistory.approvalStatusId === this.approvalStatusService.BACK){
      approvalHistory.fromHeading = 'from';
      approvalHistory.iconColor = 'back';
    }
    else if(approvalHistory.approvalStatusId === this.approvalStatusService.REJECT){
      approvalHistory.fromHeading = 'reject';
      approvalHistory.iconColor = 'back';
    }
    else if( approvalHistory.approvalStatusId === this.approvalStatusService.APPROVED){
      approvalHistory.fromHeading = 'approve';
      approvalHistory.iconColor = 'approve';
    }
    return approvalHistory;
  }

  // -----------------------------------------------------------------------------------------------------
  // @ Helper Method
  // -----------------------------------------------------------------------------------------------------



}
