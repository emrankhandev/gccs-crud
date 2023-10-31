import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {AppCrudRequestService} from "../../../../core/service/app-crud-request.service";
import {AppUtils} from "../../../../core/utils/app.utils";
import {AppConstants} from "../../../../core/constants/app.constants";
import {Observable} from "rxjs";
import {CommonResponseList, CommonResponseObject} from "../../../../core/model/common-response";
import {ApprovalHistory} from "../model/approval-history";

@Injectable({
    providedIn: 'root'
})
export class ApprovalHistoryService extends AppCrudRequestService<ApprovalHistory> {

    constructor(
      private http: HttpClient,
      private appUtils: AppUtils,
      private appConstants: AppConstants,
    ) {
        super(http, appUtils.getPrivateBaseURL() + appConstants.moduleSuperAdmin + 'approval-history');
    }


  getNotificationListByUserId(): Observable<CommonResponseList<ApprovalHistory>> {
    return this.httpClient.get<CommonResponseList<ApprovalHistory>>( this._BASE_URL + '/' + 'get-notification-list-by-user-id');
  }

  getApprovalPendingList(userId: number, fromDate: Date, toDate: Date, transactionTypeId: number): Observable<CommonResponseList<ApprovalHistory>> {
    return this.httpClient.get<CommonResponseList<ApprovalHistory>>( this._BASE_URL + '/' + 'get-pending-list-by-user-id' + '/' + userId+ '/' + fromDate + '/' + toDate + '/' + transactionTypeId);
  }

  getByApprovalHistoryId(approvalHistoryId: number,): Observable<CommonResponseList<ApprovalHistory>> {
    return this.httpClient.get<CommonResponseList<ApprovalHistory>>( this._BASE_URL + '/' + 'get-by-approval-history-id' + '/' + approvalHistoryId);
  }

  getLoadListData(userId: number, formDate: Date, toDate:Date): Observable<CommonResponseList<ApprovalHistory>> {
    return this.httpClient.get<CommonResponseList<ApprovalHistory>>( this._BASE_URL +'/' + userId + '/' + formDate + '/' + toDate  );
  }

}

