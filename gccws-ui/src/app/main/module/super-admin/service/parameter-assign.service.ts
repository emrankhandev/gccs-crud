import {AppCrudRequestService} from "../../../core/service/app-crud-request.service";
import {HttpClient} from "@angular/common/http";
import {AppUtils} from "../../../core/utils/app.utils";
import {AppConstants} from "../../../core/constants/app.constants";
import {Injectable} from "@angular/core";
import {ParameterAssignModel} from "../model/parameter-assign-model";


@Injectable({
  providedIn: 'root'
})
export class ParameterAssignService extends AppCrudRequestService<ParameterAssignModel> {

  constructor(
    private http: HttpClient,
    private appUtils: AppUtils,
    private appConstants: AppConstants,
  ) {
    super(http, appUtils.getPrivateBaseURL() + appConstants.moduleSuperAdmin + 'parameter-assign');
  }
}
