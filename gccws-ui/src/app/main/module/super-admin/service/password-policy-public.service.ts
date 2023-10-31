import {Injectable} from "@angular/core";
import {AppCrudRequestService} from "../../../core/service/app-crud-request.service";
import {HttpClient} from "@angular/common/http";
import {AppUtils} from "../../../core/utils/app.utils";
import {AppConstants} from "../../../core/constants/app.constants";
import {Observable} from "rxjs";
import {CommonResponseObject} from "../../../core/model/common-response";
import {ChangePasswordModel} from "../model/change-password-model";
import {PasswordPolicy} from "../../super-admin/model/password-policy";
import {AppUser} from "../model/app-user";

@Injectable({
  providedIn: 'root'
})
export class PasswordPolicyPublicService  extends AppCrudRequestService<AppUser>{

  constructor(
    private http: HttpClient,
    private appUtils: AppUtils,
    private appConstants: AppConstants,
  ) {
    super(http, appUtils.getPublicBaseURL() + appConstants.moduleSuperAdmin + 'password-policy');
  }

  getAgentPolicy(): Observable<CommonResponseObject<PasswordPolicy>> {
    return this.httpClient.get<CommonResponseObject<PasswordPolicy>>( this._BASE_URL + '/' + 'get-agent-policy');
  }

}
