import {Component, ElementRef, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../../../../core/service/auth.service";
import {LocalStoreUtils} from "../../../../core/utils/local-store.utils";
import {ActivatedRoute, Router} from "@angular/router";
import {AppUtils} from "../../../../core/utils/app.utils";
import {LoginService} from "../../service/login.service";
import {SnackbarHelper} from "../../../../core/utils/snackbar.utils";
import {AppUserPublicService} from "../../service/app-user-public.service";
import {MatDialog} from "@angular/material/dialog";
import {RecoverPasswordComponent} from "../recover-password/recover-password.component";
import {UserSignupComponent} from "../user-signup/user-signup.component";

@Component({
  selector: 'app-user-login',
  templateUrl: './user-login.component.html',
  styleUrls: ['./user-login.component.scss']
})
export class UserLoginComponent implements OnInit , OnDestroy{

  /*property*/
  isLoading: boolean = false;
  public showNewPassword: boolean;

  /*object*/
  frmGroup: FormGroup;

  @ViewChild('userName') userName: ElementRef;

  // -----------------------------------------------------------------------------------------------------
  // @ init
  // -----------------------------------------------------------------------------------------------------

  constructor(
    private matDialog: MatDialog,
    private formBuilder: FormBuilder,
    private modelService: LoginService,
    private authService: AuthService,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private localStoreUtils : LocalStoreUtils,
    private snackbarHelper: SnackbarHelper,
    private appUserPublicService: AppUserPublicService,
    private appUtils : AppUtils,
  ) {}

  ngOnInit(): void {
    this.setFormInitValue();
  }

  ngOnDestroy(): void{

  }

  // -----------------------------------------------------------------------------------------------------
  // @ api calling
  // -----------------------------------------------------------------------------------------------------
  recoverPasswordDialog(): void {
    let username = this.frmGroup.value.userName;
    console.log(username);
    if (username == '') {
      this.snackbarHelper.openErrorSnackBar('User Name is empty', 'ok');
      this.userName.nativeElement.focus();
      return;
    }
    this.appUserPublicService.getUserByName(username).subscribe(
      res => {
        if(res.data){
          let dialogConfig = this.appUtils.getDialogConfig();
          dialogConfig.data = {
            model: res.data,
            type:1,//customer for 2, app user for 1
          };
          //dialogConfig.data = res.data;
          const dialogRef = this.matDialog.open(RecoverPasswordComponent, dialogConfig);
          dialogRef.componentInstance.callBackMethod.subscribe(value => {
          });
        }else{
          this.snackbarHelper.openErrorSnackBar('User name dose not match', 'ok');
        }
      },
      error => {
        console.log(error.data);
      }
    );
  }

  signIn(): void {
    const userName = this.frmGroup.value.userName;
    const password = this.frmGroup.value.password;
    this.isLoading = true;
    this.modelService.loginProcess(userName, password).subscribe({
      next: (res) => {
        this.isLoading = false;
        // console.log(res);
        if (res.status){
          const passwordHistory = res.data.passwordHistory;
          const passwordPolicy = res.data.passwordPolicy;
          const isExpire = passwordHistory && passwordPolicy ? this.isPasswordExpire(passwordHistory.entryDate, passwordPolicy) : false;

          let redirectURL = '';

            /*auth service store*/
            this.authService.authenticated = true;
            this.authService.accessToken = res.data.token;

            /*work with local storage*/
            this.localStoreUtils.setToCookie(this.localStoreUtils.KEY_ACCESS_TOKEN,  res.data.token);

            /*store for reset password form inside*/
            this.localStoreUtils.setToLocalStore(this.localStoreUtils.KEY_PASSWORD_HISTORY, passwordHistory);
            this.localStoreUtils.setToLocalStore(this.localStoreUtils.KEY_PASSWORD_POLICY, passwordPolicy);

            redirectURL = this.activatedRoute.snapshot.queryParamMap.get('redirectURL') || '/signed-in-redirect';

          this.router.navigateByUrl( redirectURL,
            {
              state: {
                passwordHistory: passwordHistory,
                passwordPolicy: passwordPolicy,
                isExpire: isExpire
              }
            }
          ).then(r => {});
        }else {
          this.appUtils.onServerResponse(res);
        }
      },
      error: (error) => {
        this.isLoading = false;
        this.appUtils.onServerErrorResponse(error);
      }
    });
  }



  openRegistrationStatusDialog(): void{
    const dialogConfig = this.appUtils.getDialogConfig("60%", );
    dialogConfig.data = {
      type:2,//customer for 2, app user for 1
    };
    const dialogRef = this.matDialog.open(RecoverPasswordComponent, dialogConfig);
    dialogRef.componentInstance.callBackMethod.subscribe(value => {
    });
  }

  // -----------------------------------------------------------------------------------------------------
  // @ Helper Method
  // -----------------------------------------------------------------------------------------------------
  setFormInitValue(): any {
    this.frmGroup = this.formBuilder.group({
      userName: ['', Validators.required],
      password: ['', Validators.required],
    });
  }

  isPasswordExpire(dateDate: any, passwordPolicy: any): boolean{
    const date = new Date(dateDate);
    const currentDate = new Date();
    const month = Math.floor((currentDate.getTime() - date.getTime()) / 1000 / 60 / 60 / 24 / 30);
    const res = month >= passwordPolicy.passwordAge;
    return res;
  }

}
