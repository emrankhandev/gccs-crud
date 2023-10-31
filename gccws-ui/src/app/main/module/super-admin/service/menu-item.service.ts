import {AppCrudRequestService} from "../../../core/service/app-crud-request.service";
import {HttpClient} from "@angular/common/http";
import {AppUtils} from "../../../core/utils/app.utils";
import {AppConstants} from "../../../core/constants/app.constants";
import {Injectable} from "@angular/core";
import {MenuItem} from "../model/menu-item";
import {Observable} from "rxjs";
import {CommonResponseList} from "../../../core/model/common-response";
import {DropdownModel} from "../../../core/model/dropdown-model";


@Injectable({
  providedIn: 'root'
})
export class MenuItemService extends AppCrudRequestService<MenuItem> {

  constructor(
    private http: HttpClient,
    private appUtils: AppUtils,
    private appConstants: AppConstants,
  ) {
    super(http, appUtils.getPrivateBaseURL() + appConstants.moduleSuperAdmin + 'menu-item');
  }

  getByMenuItemId(menuItemId: any): Observable<CommonResponseList<MenuItem>> {
    return this.http.get<CommonResponseList<MenuItem>>(this._BASE_URL + '/' + 'get-by-menu-item-id' + '/' + menuItemId);
  }

  getByMenuType(menuType: any): Observable<CommonResponseList<MenuItem>> {
    return this.http.get<CommonResponseList<MenuItem>>(this._BASE_URL + '/' + 'dropdown-list-by-menu-type' + '/' + menuType);
  }

  getPageByAppUserId(): Observable<CommonResponseList<MenuItem>> {
    return this.http.get<CommonResponseList<MenuItem>>(this._BASE_URL + '/' + 'page-by-app-user-id');
  }

  getAuthorizedReportList(moduleId: number): Observable<CommonResponseList<MenuItem>> {
    return this.http.get<CommonResponseList<MenuItem>>( this._BASE_URL + '/' + 'authorized-report' + '/' + moduleId);
  }

  getModuleList(): Observable<CommonResponseList<DropdownModel>> {
    return this.httpClient.get<CommonResponseList<DropdownModel>>( this._BASE_URL + '/' + 'get-module-list');
  }

  getReportModuleByAppUser(): Observable<CommonResponseList<MenuItem>> {
    return this.http.get<CommonResponseList<MenuItem>>(this._BASE_URL + '/' + 'report-module-by-appUser');
  }

  getReportByModuleIdAndAppUser(moduleId: number): Observable<CommonResponseList<MenuItem>> {
    return this.http.get<CommonResponseList<MenuItem>>(this._BASE_URL + '/' + 'report-by-moduleId-and-appUser' + '/' + moduleId);
  }

}
