import {Component, EventEmitter, Inject, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AppUtils} from "../../../../../core/utils/app.utils";
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material/dialog";
import {SnackbarHelper} from "../../../../../core/utils/snackbar.utils";
import {Router} from "@angular/router";
import {AppUserPublicService} from "../../../service/app-user-public.service";
import {ChangePasswordModel} from "../../../model/change-password-model";
import {CommonValidator} from "../../../../../core/validator/common.validator";
import {RecoverSuccessPageComponent} from "../recover-success-page/recover-success-page.component";
import {PasswordPolicyPublicService} from "../../../service/password-policy-public.service";



@Component({
    selector: 'app-new-password',
    templateUrl: './new-password.component.html',
    styleUrls: ['./new-password.component.scss']
})
export class NewPasswordComponent implements OnInit , OnDestroy{

  /*property*/
  callBackMethod: EventEmitter<boolean> = new EventEmitter<boolean>();
  isLoading: boolean = false;
  public showNewPassword: boolean;
  public showConfirmPassword: boolean;

  /*extra*/
  passwordPolicy: any;
  policyColor: any;
  COLOR_ACCEPT = 'green';
  COLOR_REJECT = 'red';

  /*form*/
  frmGroup: FormGroup;
  model: ChangePasswordModel = new ChangePasswordModel();
  username: string;
  otp: string;

  constructor(
    public dialogRef: MatDialogRef<NewPasswordComponent>,
    //public dialogRef2: MatDialogRef<RecoverPasswordOtpComponent>,
    @Inject(MAT_DIALOG_DATA) data: any,
    private formBuilder: FormBuilder,
    private matDialog: MatDialog,
    private appUtils: AppUtils,
    private snackbarHelper: SnackbarHelper,
    private appUserPublicService: AppUserPublicService,
    private router: Router,
    private passwordPolicyPublicService: PasswordPolicyPublicService,
    private commonValidator: CommonValidator,
  ) {

    this.username = data.username;

  }


  ngOnInit(): void {
    this.setFormInitValue();
    this.policyColor = {
      minLength: this.COLOR_REJECT,
      alphanumeric: this.COLOR_REJECT,
      sequential: this.COLOR_REJECT,
      specialChar: this.COLOR_REJECT,
      upperLower: this.COLOR_REJECT,
    };
  }

  ngOnDestroy(): void{

  }

  // -----------------------------------------------------------------------------------------------------
  // @ Api Calling
  // -----------------------------------------------------------------------------------------------------

  changePassword(): void {

    if(this.frmGroup.value.newPassword != this.frmGroup.value.confirmPassword){
      this.snackbarHelper.openErrorSnackBar('Password is not match', 'ok');
      return;
    }

    this.model.username = this.username;
    this.model.password = this.frmGroup.value.newPassword;
    this.isLoading = true;
    this.appUserPublicService.changePassword(this.model).subscribe({
      next: (res) => {
        this.isLoading = false;
        if(res.data){
          this.router.navigate(['/user/login']);
          this.callBackMethod.emit(true);
          this.closeDialog();
          this.openRecoverPasswordChangeSuccessDialog()
        }else{
          this.snackbarHelper.openErrorSnackBar('Change unsuccessfull', 'ok');
        }
      },
      error: (error) => {
        this.isLoading = false;
        this.appUtils.onServerErrorResponse(error);
      }
    });

  }



  openRecoverPasswordChangeSuccessDialog(): void{
    const dialogConfig = this.appUtils.getDialogConfig();
    this.matDialog.open(RecoverSuccessPageComponent, dialogConfig);
  }


  // -----------------------------------------------------------------------------------------------------
  // @ view method
  // -----------------------------------------------------------------------------------------------------

  closeDialog(): void {
    this.dialogRef.close();
  }

  /*closeRecoverPasswordOtpComponentDialog(): void {
    this.dialogRef2.close();
  }*/

  checkPassword(): boolean {
    const passwordValue = this.frmGroup.value.newPassword;

    if (!this.passwordPolicy){
      return false;
    }

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

    return res;
  }


  // -----------------------------------------------------------------------------------------------------
  // @ Helper Method
  // -----------------------------------------------------------------------------------------------------

  setFormInitValue(): any {
    this.frmGroup = this.formBuilder.group({
      newPassword: ['', Validators.required],
      confirmPassword: ['', Validators.required],
    });
    validators:this.commonValidator.mustMatch('newPassword', 'confirmPassword')
  }



}
