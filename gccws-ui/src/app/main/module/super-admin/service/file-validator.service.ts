import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {AppCrudRequestService} from "../../../core/service/app-crud-request.service";
import {AppUtils} from "../../../core/utils/app.utils";
import {AppConstants} from "../../../core/constants/app.constants";
import {FileValidator} from "../model/file-validator";
import {CommonResponseObject} from "../../../core/model/common-response";
import {Observable} from "rxjs";


@Injectable({
  providedIn: 'root'
})
export class FileValidatorService extends AppCrudRequestService<FileValidator> {

  constructor(
    private http: HttpClient,
    private appUtils: AppUtils,
    private appConstants: AppConstants,
  ) {
    super(http, appUtils.getPrivateBaseURL() + appConstants.moduleSuperAdmin + 'file-validator');
  }

  getByDevCode(devCode: number): Observable<CommonResponseObject<FileValidator>> {
    return this.httpClient.get<CommonResponseObject<FileValidator>>( this._BASE_URL + '/' + 'get-by-dev-code' + '/' + devCode);
  }

}
