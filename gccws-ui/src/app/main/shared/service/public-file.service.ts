import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {AppCrudRequestService} from "../../core/service/app-crud-request.service";
import {AppUtils} from "../../core/utils/app.utils";
import {AppConstants} from "../../core/constants/app.constants";

/**
 * @author	Md. Mizanur Rahman
 * @Email	  kmmizanurrahmanjp@gmail.com
 * @since	  March 09,2023
 * @version 1.0
 */

@Injectable({
  providedIn: 'root'
})
export class PublicFileService extends AppCrudRequestService<any>{

  constructor(
    private http: HttpClient,
    private appUtils: AppUtils,
    private appConstants: AppConstants,
  ) {
    super(http, appUtils.getPublicBaseURL() + appConstants.moduleCommon + 'file');
  }

  viewUrl(fileName?: any): string{
    return fileName ?  this._BASE_URL + '/view/' + fileName : '';
  }

  viewImageUrl(fileName?: any): string{
    return fileName ?  this._BASE_URL + '/view/image/' + fileName : './assets/img/avatars/user.png';
  }

  viewPdfUrl(fileName?: string): string{
    return fileName ?  this._BASE_URL + '/view/pdf/' + fileName : '';
  }

}
