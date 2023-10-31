import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {AppUtils} from "../../../core/utils/app.utils";

@Injectable({
    providedIn: 'root'
})
export class LoginService {

   constructor(
     private http: HttpClient,
     private appUtils: AppUtils
   ) {}

    loginProcess(username: any, password: any): Observable<any> {
      const header = {
        headers: new HttpHeaders()
          .set('Content-Type', 'application/json')
      };
      const LoginRequest = {username: username, password: password};
      return this.http.post<any>(this.appUtils.getBaseURL() + 'auth/signin' , LoginRequest, header);
    }

  resetPassword(passwordHistory: any): Observable<any> {
    const header = {
      headers: new HttpHeaders()
        .set('Content-Type', 'application/json')
    };
    return this.http.post<any>(this.appUtils.getBaseURL() + 'auth/reset-password' , passwordHistory, header);
  }
}
