import {AppCrudRequestService} from "../../../core/service/app-crud-request.service";
import {ReportUpload} from "../model/report-upload";
import {HttpClient} from "@angular/common/http";
import {AppUtils} from "../../../core/utils/app.utils";
import {AppConstants} from "../../../core/constants/app.constants";
import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {CommonResponseList} from "../../../core/model/common-response";
import {DropdownModel} from "../../../core/model/dropdown-model";



@Injectable({
  providedIn: 'root'
})
export class ReportUploadService extends AppCrudRequestService<ReportUpload> {

  constructor(
    private http: HttpClient,
    private appUtils: AppUtils,
    private appConstants: AppConstants,
  ) {
    super(http, appUtils.getPrivateBaseURL() + appConstants.moduleSuperAdmin + 'report-upload');
  }

  getSubreportDropdownList(): Observable<CommonResponseList<DropdownModel>> {
    return this.http.get<CommonResponseList<DropdownModel>>( this._BASE_URL + '/' + 'subreport-dropdown-list');
  }

  getMasterReportDropdownList(): Observable<CommonResponseList<DropdownModel>> {
    return this.http.get<CommonResponseList<DropdownModel>>( this._BASE_URL + '/' + 'master-report-dropdown-list');
  }

  getAllActiveSubreportList(): Observable<CommonResponseList<ReportUpload>> {
    return this.http.get<CommonResponseList<ReportUpload>>( this._BASE_URL + '/subreport');
  }

  getAllActiveMasterReportList(): Observable<CommonResponseList<ReportUpload>> {
    return this.http.get<CommonResponseList<ReportUpload>>( this._BASE_URL + '/master-report');
  }

  downloadFile(filename: any): any {
    return this.http.get(this._BASE_URL + '/download/' + filename, {responseType: 'blob'});
  }

}
