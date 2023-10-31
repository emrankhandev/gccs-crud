import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {AppCrudRequestService} from "../../core/service/app-crud-request.service";
import {AppUtils} from "../../core/utils/app.utils";
import {AppConstants} from "../../core/constants/app.constants";
import {MessageHistory} from "../model/message-history";
import {Observable} from "rxjs";
import {CommonResponseList} from "../../core/model/common-response";

/**
 * @author	Md. Mizanur Rahman
 * @Email	  kmmizanurrahmanjp@gmail.com
 * @since	  March 14,2023
 * @version 1.0
 */

@Injectable({
  providedIn: 'root'
})
export class MessageHistoryService extends AppCrudRequestService<MessageHistory>{

  constructor(
    private http: HttpClient,
    private appUtils: AppUtils,
    private appConstants: AppConstants,
  ) {
    super(http, appUtils.getPrivateBaseURL() + appConstants.moduleCommon + 'message-history');
  }

  getByUserId(): Observable<CommonResponseList<MessageHistory>> {
    return this.http.get<CommonResponseList<MessageHistory>>( this._BASE_URL + '/get-message-list-by-user-id');

  }
}
