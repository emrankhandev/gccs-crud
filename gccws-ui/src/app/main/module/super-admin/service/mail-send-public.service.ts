import {Injectable} from "@angular/core";
import {AppCrudRequestService} from "../../../core/service/app-crud-request.service";
import {HttpClient} from "@angular/common/http";
import {AppUtils} from "../../../core/utils/app.utils";
import {AppConstants} from "../../../core/constants/app.constants";
import {Observable} from "rxjs";
import {CommonResponseObject} from "../../../core/model/common-response";
import {AppUser} from "../model/app-user";

@Injectable({
  providedIn: 'root'
})
export class MailSendPublicService  extends AppCrudRequestService<AppUser>{

  constructor(
    private http: HttpClient,
    private appUtils: AppUtils,
    private appConstants: AppConstants,
  ) {
    super(http, appUtils.getPublicBaseURL() + appConstants.moduleSuperAdmin + 'mail-sender');
  }

  sendEmailForPasswordRecovery(appUserId: number): Observable<CommonResponseObject<AppUser>> {
    return this.http.get<CommonResponseObject<AppUser>>( this._BASE_URL + '/' + 'password-recovery' + '/' + appUserId);
  }

  sendEmailForOTP(email: string, userName: string): Observable<CommonResponseObject<AppUser>> {
    return this.http.get<CommonResponseObject<AppUser>>( this._BASE_URL + '/' + 'send-mail-for-otp' + '/' + email+ '/' + userName);
  }

}
