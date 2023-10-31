import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {CommonValidator} from "../../../../core/validator/common.validator";
import {LoginService} from "../../service/login.service";
import {AppUtils} from "../../../../core/utils/app.utils";
import {LocalStoreUtils} from "../../../../core/utils/local-store.utils";

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.scss']
})
export class ResetPasswordComponent implements OnInit{

  /*property*/
  isLoading: boolean = false;
  public showOldPassword: boolean;
  public showNewPassword: boolean;
  public showConfirmPassword: boolean;
  passwordHistory: any;
  passwordPolicy: any;
  isExpire: boolean;

  private COLOR_ACCEPT = 'green';
  private COLOR_REJECT = 'red';
  policyColor: any;

  /*object*/
  frmGroup: FormGroup;

  // -----------------------------------------------------------------------------------------------------
  // @ init
  // -----------------------------------------------------------------------------------------------------
  constructor(
    private formBuilder: FormBuilder,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private modelService: LoginService,
    private commonValidator: CommonValidator,
    private appUtils : AppUtils,
    private localStoreUtils : LocalStoreUtils,
  ) {

  }

  ngOnInit(): void {
    this.passwordHistory = this.localStoreUtils.getFromLocalStore(this.localStoreUtils.KEY_PASSWORD_HISTORY);
    this.passwordPolicy = this.localStoreUtils.getFromLocalStore(this.localStoreUtils.KEY_PASSWORD_POLICY);
    this.isExpire = history.state.isExpire;

    if (!this.passwordHistory){
      this.router.navigateByUrl( 'sign-in' );
    }

    this.setFormInitValue();

    this.policyColor = {
      minLength: this.COLOR_REJECT,
      alphanumeric: this.COLOR_REJECT,
      sequential: this.COLOR_REJECT,
      specialChar: this.COLOR_REJECT,
      upperLower: this.COLOR_REJECT,
      matchUsername: this.COLOR_REJECT,
    };
  }

  // -----------------------------------------------------------------------------------------------------
  // @ View Method
  // -----------------------------------------------------------------------------------------------------
  resetPassword(): void {
    const newPassword = this.frmGroup.value.newPassword;
    const oldPassword = this.frmGroup.value.oldPassword;

    /*Return if the form is invalid*/
    if ( !this.checkPassword()) {
      this.appUtils.showErrorMessage('All password policy not match !', 'All password policy not match !');
      return;
    }

    /*check old password*/
    if ( oldPassword !==  this.passwordHistory.password) {
      this.appUtils.showErrorMessage('Old password not match !', 'Old password not match !');
      return;
    }

    // generate value
    this.passwordHistory.password = newPassword;
    this.isLoading = true;
    this.modelService.resetPassword( this.passwordHistory).subscribe({
      next: (res) => {
        this.isLoading = false;
        if (res.status){
          /*show message*/
          this.appUtils.showSuccessMessage(res.message, res.message);
          /*Navigate to the redirect url*/
          this.router.navigateByUrl( '/sign-in');
        }else {
          this.appUtils.showErrorMessage(res.message, res.message);
        }
      },
      error: (error) => {
        this.isLoading = false;
        this.appUtils.onServerErrorResponse(error);
      }
    });
  }





  // -----------------------------------------------------------------------------------------------------
  // @ View Method
  // -----------------------------------------------------------------------------------------------------
  checkPassword(): boolean {
    const passwordValue = this.frmGroup.value.newPassword;
    let res = true;

    /*check length*/
    if (passwordValue.length < this.passwordPolicy.minLength ){
      res = false;
      this.policyColor.minLength = this.COLOR_REJECT;
    }else {
      // res = true;
      this.policyColor.minLength = this.COLOR_ACCEPT;
    }

    /*check alphanumeric*/
    if (this.passwordPolicy.alphanumeric){
      if (!CommonValidator.isContainsAlpha(passwordValue) || !CommonValidator.isContainsNumeric(passwordValue)){
        res = false;
        this.policyColor.alphanumeric = this.COLOR_REJECT;
      }else {
        // res = true;
        this.policyColor.alphanumeric = this.COLOR_ACCEPT;
      }
    }

    /*check sequential*/
    if (this.passwordPolicy.sequential){
      if (!CommonValidator.isContainsSequential(passwordValue)){
        res = false;
        this.policyColor.sequential = this.COLOR_REJECT;
      }else {
        // res = true;
        this.policyColor.sequential = this.COLOR_ACCEPT;
      }
    }

    /*check specialChar*/
    if (this.passwordPolicy.specialChar){
      if (!CommonValidator.isContainsAnySpecialChar(passwordValue)){
        res = false;
        this.policyColor.specialChar = this.COLOR_REJECT;
      }else {
        // res = true;
        this.policyColor.specialChar = this.COLOR_ACCEPT;
      }
    }

    /*check upperLower*/
    if (this.passwordPolicy.upperLower){
      if (!CommonValidator.isContainsUpper(passwordValue) || !CommonValidator.isContainsLower(passwordValue)){
        res = false;
        this.policyColor.upperLower = this.COLOR_REJECT;
      }else {
        // res = true;
        this.policyColor.upperLower = this.COLOR_ACCEPT;
      }
    }

    /*check matchUsername*/
    if (this.passwordPolicy.matchUsername){
      if (passwordValue === this.passwordHistory.appUser.username){
        res = false;
        this.policyColor.matchUsername = this.COLOR_REJECT;
      }else {
        // res = true;
        this.policyColor.matchUsername = this.COLOR_ACCEPT;
      }
    }

    return res;
  }

  // -----------------------------------------------------------------------------------------------------
  // @ Helper Method
  // -----------------------------------------------------------------------------------------------------
  setFormInitValue(): any {
    this.frmGroup = this.formBuilder.group({
      oldPassword: ['', Validators.required],
      newPassword: ['', Validators.required],
      confirmPassword: ['', Validators.required],
    },{
      validators: this.commonValidator.mustMatch('newPassword', 'confirmPassword')
    });
  }

}


