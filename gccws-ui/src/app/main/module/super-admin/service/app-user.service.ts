import {Injectable} from "@angular/core";
import {AppCrudRequestService} from "../../../core/service/app-crud-request.service";
import {AppUser} from "../model/app-user";
import {HttpClient} from "@angular/common/http";
import {AppUtils} from "../../../core/utils/app.utils";
import {AppConstants} from "../../../core/constants/app.constants";
import {Observable} from "rxjs";
import {CommonResponseList, CommonResponseObject, CommonResponsePageable} from "../../../core/model/common-response";
import {DropdownModel} from "../../../core/model/dropdown-model";
import {PageableData} from "../../../core/model/pageable-data";


@Injectable({
  providedIn: 'root'
})
export class AppUserService extends AppCrudRequestService<AppUser> {

    constructor(
      private http: HttpClient,
      private appUtils: AppUtils,
      private appConstants: AppConstants,
    ) {
        super(http, appUtils.getPrivateBaseURL() + appConstants.moduleSuperAdmin + 'app-user');
    }

  getPageableListDataForAgentProfile(pageableData : PageableData): Observable<CommonResponsePageable<AppUser>> {
    return this.httpClient.put<CommonResponsePageable<AppUser>>( this._BASE_URL + '/' + 'get-pageable-list-data-for-agent-profile' , pageableData);
  }

  getEmpId(empId: number): Observable<CommonResponseObject<AppUser>> {
    return this.httpClient.get<CommonResponseObject<AppUser>>( this._BASE_URL + '/' + 'get-by-emp-id' + '/' + empId);
  }

}
