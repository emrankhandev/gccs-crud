import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {AppCrudRequestService} from "../../../../core/service/app-crud-request.service";
import {AppUtils} from "../../../../core/utils/app.utils";
import {AppConstants} from "../../../../core/constants/app.constants";
import {ApprovalConfiguration} from "../model/approval-configuration";
import {Observable} from "rxjs";
import { CommonResponseObject} from "../../../../core/model/common-response";
import {ApprovalUser} from "../model/approval-user";

@Injectable({
    providedIn: 'root'
})
export class ApprovalConfigurationService extends AppCrudRequestService<ApprovalConfiguration> {

    constructor(
      private http: HttpClient,
      private appUtils: AppUtils,
      private appConstants: AppConstants,
    ) {
        super(http, appUtils.getPrivateBaseURL() + appConstants.moduleSuperAdmin + 'approval-configuration');
    }

  getSubmitUserByDepartmentAndAppUserId(departmentId: number, appUserId: number, transactionTypeId: number, ): Observable<CommonResponseObject<ApprovalUser>> {
    return this.httpClient.get<CommonResponseObject<ApprovalUser>>(this._BASE_URL + '/' + 'get-submit-user-by-department-appuser-transaction-type-id' + '/' + departmentId + '/' + appUserId + '/' + transactionTypeId);
  }

  getApproveAndForwardUserByDepartmentAndAppUserId(departmentId: number, appUserId: number, transactionTypeId: number,): Observable<CommonResponseObject<ApprovalUser>> {
    return this.httpClient.get<CommonResponseObject<ApprovalUser>>(this._BASE_URL + '/' + 'get-approve-forward-user-by-department-and-appuser-id' + '/' + departmentId + '/' + appUserId + '/' + transactionTypeId);
  }

  getByToTeamAndDepartmentId(toTeamId: number, departmentId: number, transactionTypeId: number,): Observable<CommonResponseObject<ApprovalConfiguration>> {
    return this.httpClient.get<CommonResponseObject<ApprovalConfiguration>>(this._BASE_URL + '/' + 'get-by-toTeam-and-department-id' + '/' + toTeamId + '/' + departmentId + '/' + transactionTypeId);
  }


}
