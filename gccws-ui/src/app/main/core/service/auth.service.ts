import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable, of, throwError} from 'rxjs';
import {catchError, map, switchMap} from 'rxjs/operators';
import { CookieService } from 'ngx-cookie-service';
import {AuthUtils} from "../utils/auth.utils";


@Injectable()
export class AuthService
{
    private _authenticated: boolean = false;

    /**
     * Constructor
     */
    constructor(
        private _httpClient: HttpClient,
        private cookieService: CookieService
    ) { }

    // -----------------------------------------------------------------------------------------------------
    // @ Accessors
    // -----------------------------------------------------------------------------------------------------
    set accessToken(token: string)
    {
        // this.cookieService.set('access_token', token);
    }


  set authenticated(value: boolean) {
    this._authenticated = value;
  }

  get accessToken(): string
    {
        return this.cookieService.get('access_token') ?? '';
    }

    checkAcessToken(): boolean
    {
        return this.cookieService.check('access_token');
    }

    // -----------------------------------------------------------------------------------------------------
    // @ Public methods
    // -----------------------------------------------------------------------------------------------------
    signInUsingToken(): Observable<any> {
        // Renew token
        return this._httpClient.post('api/auth/refresh-access-token', {
            access_token: this.accessToken
        }).pipe(
            catchError(() => {

                // Return false
                return of(false);
            }),
            switchMap((response: any) => {

                // Store the access token in the local storage
                this.accessToken = response.access_token;
              //console.log('access token', this.accessToken );

                // Set the authenticated flag to true
                this._authenticated = true;

                // Store the user on the user service
                // this._userService.user = response.user;

                // Return true
                return of(true);
            })
        );
    }

    /**
     * Sign out
     */
    signOut(): Observable<any> {
        // Remove the access token from the local storage
        // this.cookieService.delete('access_token');

        this.cookieService.delete('access_token');

        console.log('after delete : ' + this.cookieService.get('access_token'));

        // Set the authenticated flag to false
        this._authenticated = false;

        // Return the observable
        return of(true);
    }



    /**
     * Check the authentication status
     */
    check(): Observable<boolean> {

      // console.log('check : '+this._authenticated);

        // Check if the user is logged in
        if ( this._authenticated ) {
            return of(true);
        }

        // Check the access token availability
        if ( !this.checkAcessToken ) {
            return of(false);
        }

        // Check the access token expire date
        if ( AuthUtils.isTokenExpired(this.accessToken) ) {
            return of(false);
        }

        // If the access token exists and it didn't expire, sign in using it
        // return this.signInUsingToken();
        return of(true);;
    }
}
