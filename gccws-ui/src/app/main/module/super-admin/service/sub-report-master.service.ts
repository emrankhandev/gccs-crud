import {AppCrudRequestService} from "../../../core/service/app-crud-request.service";
import {HttpClient} from "@angular/common/http";
import {AppUtils} from "../../../core/utils/app.utils";
import {AppConstants} from "../../../core/constants/app.constants";
import {Injectable} from "@angular/core";
import {SubReportMaster} from "../model/sub-report-master";

/**
 * @Author		Emran Khan
 * @Since		  October 01, 2022
 * @version		1.0.0
 */

@Injectable({
  providedIn: 'root'
})
export class SubReportMasterService extends AppCrudRequestService<SubReportMaster> {

  constructor(
    private http: HttpClient,
    private appUtils: AppUtils,
    private appConstants: AppConstants,
  ) {
    super(http, appUtils.getPrivateBaseURL() + appConstants.moduleSuperAdmin + 'sub-report-master');
  }
}
