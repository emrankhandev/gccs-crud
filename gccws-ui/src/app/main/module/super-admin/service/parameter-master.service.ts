import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {AppCrudRequestService} from "../../../core/service/app-crud-request.service";
import {AppUtils} from "../../../core/utils/app.utils";
import {AppConstants} from "../../../core/constants/app.constants";
import {ParameterMaster} from "../model/parameter-master";


@Injectable({
  providedIn: 'root'
})
export class ParameterMasterService extends AppCrudRequestService<ParameterMaster> {

  constructor(
    private http: HttpClient,
    private appUtils: AppUtils,
    private appConstants: AppConstants,
  ) {
    super(http, appUtils.getPrivateBaseURL() + appConstants.moduleSuperAdmin + 'parameter-master');
  }
}
