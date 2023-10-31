import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {AppCrudRequestService} from "../../../core/service/app-crud-request.service";
import {AppUtils} from "../../../core/utils/app.utils";
import {AppConstants} from "../../../core/constants/app.constants";
import {Observable} from "rxjs";
import {CommonResponseList, CommonResponseObject} from "../../../core/model/common-response";
import {DashboardBasicModel} from "../model/dashboard-basic-model";



@Injectable({
  providedIn: 'root'
})
export class DashboardService extends AppCrudRequestService<DashboardBasicModel> {

  constructor(
    private http: HttpClient,
    private appUtils: AppUtils,
    private appConstants: AppConstants,
  ) {
    super(http, appUtils.getPrivateBaseURL() + appConstants.moduleCommon + 'dashboard');
  }

  getBasicData(): Observable<CommonResponseObject<DashboardBasicModel>> {
    return this.http.get<CommonResponseObject<DashboardBasicModel>>(this._BASE_URL + '/' + 'basic');
  }

}
