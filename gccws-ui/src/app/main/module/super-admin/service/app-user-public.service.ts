import {Injectable} from "@angular/core";
import {AppCrudRequestService} from "../../../core/service/app-crud-request.service";
import {HttpClient} from "@angular/common/http";
import {AppUtils} from "../../../core/utils/app.utils";
import {AppConstants} from "../../../core/constants/app.constants";
import {Observable} from "rxjs";
import {CommonResponseObject} from "../../../core/model/common-response";
import {ChangePasswordModel} from "../model/change-password-model";
import {AppUser} from "../model/app-user";




@Injectable({
  providedIn: 'root'
})
export class AppUserPublicService  extends AppCrudRequestService<AppUser>{

  constructor(
    private http: HttpClient,
    private appUtils: AppUtils,
    private appConstants: AppConstants,
  ) {
    super(http, appUtils.getPublicBaseURL() +  appConstants.moduleSuperAdmin + 'app-user');
  }

  getUserByName(username: string): Observable<CommonResponseObject<AppUser>> {
    return this.http.get<CommonResponseObject<AppUser>>( this._BASE_URL + '/' + 'get-user-by-name' + '/' + username);
  }

  changePassword(body: ChangePasswordModel): Observable<CommonResponseObject<AppUser>> {
    return this.http.post<CommonResponseObject<AppUser>>( this._BASE_URL + '/' + 'change-password', body);
  }

  getUserByEmail(email: string): Observable<CommonResponseObject<AppUser>> {
    return this.http.get<CommonResponseObject<AppUser>>( this._BASE_URL + '/' + 'get-user-by-email' + '/' + email);
  }

  checkUserFromAppUserAndEmailFromCustomer(email: string): Observable<CommonResponseObject<AppUser>> {
    return this.http.get<CommonResponseObject<AppUser>>( this._BASE_URL + '/' + 'get-user-by-email' + '/' + email);
  }

}
