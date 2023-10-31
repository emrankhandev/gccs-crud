import {Injectable} from '@angular/core';
import {CookieService} from "ngx-cookie-service";
import {AuthUtils} from "./auth.utils";

@Injectable({
    providedIn: 'root'
})
export class LocalStoreUtils {

  /*store key*/
  KEY_MENU_ITEM_URL = 'menuItemUrls';
  KEY_ACCESS_TOKEN = 'access_token';
  KEY_SELECTED_LANG = 'selected_lang';
  KEY_PASSWORD_HISTORY = 'password_history';
  KEY_PASSWORD_POLICY = 'password_policy';


  constructor(
    private cookieService: CookieService,
  ) {}

  // -----------------------------------------------------------------------------------------------------
  // @ localStorage
  // -----------------------------------------------------------------------------------------------------
  setToLocalStore(key: string, value: any){
    localStorage.setItem(key, JSON.stringify(value));
  }

  getFromLocalStore(key: string): any{
    return JSON.parse(localStorage.getItem(key) || '{}');
  }


  // -----------------------------------------------------------------------------------------------------
  // @ cookie
  // -----------------------------------------------------------------------------------------------------
  setToCookie(key: string, value: any){
    this.cookieService.set(key, value)
  }

  getFromCookie(key: string): any{
    return this.cookieService.get(key);
  }


  // -----------------------------------------------------------------------------------------------------
  // @ Get User Info From Token
  // -----------------------------------------------------------------------------------------------------

  getUserInfo(): any{
    const decodedToken = AuthUtils._decodeToken(this.getFromCookie(this.KEY_ACCESS_TOKEN).toString());
    return decodedToken ? decodedToken.userInfo : null;
  }


}
