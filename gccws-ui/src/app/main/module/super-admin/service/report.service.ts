import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from "rxjs";
import {AppCrudRequestService} from "../../../core/service/app-crud-request.service";
import {AppUtils} from "../../../core/utils/app.utils";
import {AppConstants} from "../../../core/constants/app.constants";
import {Report} from "../model/report";


/**
 * @Author		Md. Mizanur Rahman
 * @Since		  August 30, 2022
 * @version		1.0.0
 */
@Injectable({
    providedIn: 'root'
})
export class ReportService extends AppCrudRequestService<Report> {

    constructor(
      private http: HttpClient,
      private appUtils: AppUtils,
      private appConstants: AppConstants,
    ) {
        super(http, appUtils.getPrivateBaseURL() + appConstants.moduleSuperAdmin + 'report-configure');
    }

  getParamDataByDevCode(reportMasterDevCode: number): Observable<any> {
    return this.http.get<any>( this._BASE_URL + '/params/' + reportMasterDevCode);
  }

  getParamChildListData(parentId: number, paramId: number): Observable<any> {
    return this.http.get<any>( this._BASE_URL + '/child-list-params/' + parentId + '/' + paramId);
  }

  downloadReport(params: any | undefined): Observable<Blob> {
    const headers = new HttpHeaders().set('Content-Type', 'application/json; charset=utf-8');

    return this.http.post(
      this._BASE_URL +'/generate-report/download',
      params,
      { headers, responseType: 'blob'}
    );
  }


  printReport(params: any | undefined): Observable<Blob> {
    const headers = new HttpHeaders().set('Content-Type', 'application/json; charset=utf-8');

    return this.http.post(
      this._BASE_URL+'/generate-report/print',
      params,
      { headers, responseType: 'blob'}
    );
  }
}
