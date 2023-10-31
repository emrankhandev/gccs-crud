import {AppCrudRequestService} from "../../../core/service/app-crud-request.service";
import {HttpClient} from "@angular/common/http";
import {AppUtils} from "../../../core/utils/app.utils";
import {AppConstants} from "../../../core/constants/app.constants";
import {Injectable} from "@angular/core";
import {UserRoleModel} from "../model/user-role-model";
import {Observable} from "rxjs";
import {CommonResponseList} from "../../../core/model/common-response";
import {MenuItem} from "../model/menu-item";
import {UserRoleMaster} from "../model/user-role-master";


@Injectable({
    providedIn: 'root'
})
export class UserRoleService extends AppCrudRequestService<UserRoleModel> {

    constructor(
      private http: HttpClient,
      private appUtils: AppUtils,
      private appConstants: AppConstants,
    ) {
        super(http, appUtils.getPrivateBaseURL() + appConstants.moduleSuperAdmin + 'user-role');
    }

  getRoleListByAppUserId(): Observable<CommonResponseList<UserRoleMaster>> {
    return this.http.get<CommonResponseList<UserRoleMaster>>(this._BASE_URL + '/' + 'role-list-by');
  }
}
