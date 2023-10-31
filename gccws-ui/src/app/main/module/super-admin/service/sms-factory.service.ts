import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {AppCrudRequestService} from "../../../core/service/app-crud-request.service";
import {SmsFactoryModel} from "../model/sms-factory-model";
import {AppUtils} from "../../../core/utils/app.utils";
import {AppConstants} from "../../../core/constants/app.constants";

@Injectable({
    providedIn: 'root'
})
export class SmsFactoryService extends AppCrudRequestService<SmsFactoryModel> {
    constructor(
      private http: HttpClient,
      private appUtils: AppUtils,
      private appConstants: AppConstants,
    ) {
      super(http, appUtils.getPrivateBaseURL() + appConstants.moduleCommon + 'sms-factory');
    }
}
