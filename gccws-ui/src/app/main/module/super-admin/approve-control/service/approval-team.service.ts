import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {AppCrudRequestService} from "../../../../core/service/app-crud-request.service";
import {AppUtils} from "../../../../core/utils/app.utils";
import {AppConstants} from "../../../../core/constants/app.constants";
import {ApprovalTeamModel} from "../model/approval-team-model";
import {Observable} from "rxjs";
import {CommonResponseList} from "../../../../core/model/common-response";
import {ApprovalConfiguration} from "../model/approval-configuration";
import {DropdownModel} from "../../../../core/model/dropdown-model";
import {ApprovalTeamMaster} from "../model/approval-team-master";

@Injectable({
    providedIn: 'root'
})
export class ApprovalTeamService extends AppCrudRequestService<ApprovalTeamModel> {

    constructor(
      private http: HttpClient,
      private appUtils: AppUtils,
      private appConstants: AppConstants,
    ) {
        super(http, appUtils.getPrivateBaseURL() + appConstants.moduleSuperAdmin + 'approval-team');
    }
  getNextTeamByDepartmentAndCurrentTeamId(departmentId: number, transactionTypeId: number, currentTeamId: number): Observable<CommonResponseList<DropdownModel>> {
    return this.httpClient.get<CommonResponseList<DropdownModel>>(this._BASE_URL + '/' + 'get-next-team-by-department-and-current-team-id' + '/' + departmentId + '/' + transactionTypeId + '/' + currentTeamId);
  }


  getPreviousTeamByDepartmentAndCurrentTeamId(departmentId: number, transactionTypeId: number, currentTeamId: number): Observable<CommonResponseList<DropdownModel>> {
    return this.httpClient.get<CommonResponseList<DropdownModel>>(this._BASE_URL + '/' + 'get-previous-team-by-department-and-current-team-id' + '/' + departmentId + '/' + transactionTypeId + '/' + currentTeamId);
  }

  getTeamByDepartmentId(departmentId: number): Observable<CommonResponseList<ApprovalTeamModel>> {
    return this.httpClient.get<CommonResponseList<ApprovalTeamModel>>(this._BASE_URL + '/' + 'get-team-by-department-id' + '/' + departmentId );
  }

}
