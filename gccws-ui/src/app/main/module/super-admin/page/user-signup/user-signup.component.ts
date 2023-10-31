import {Component, EventEmitter, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AppUtils} from "../../../../core/utils/app.utils";
import {CommonValidator} from "../../../../core/validator/common.validator";
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material/dialog";
import {AppUserPublicService} from "../../service/app-user-public.service";
import {PasswordPolicyPublicService} from "../../service/password-policy-public.service";
import {TranslationLoaderService} from "../../../../core/service/translation-loader.service";
import {AppUser} from "../../model/app-user";
import {OtpPageComponent} from "./otp-page/otp-page.component";
import {MailSendPublicService} from "../../service/mail-send-public.service";

@Component({
  selector: 'app-user-signup',
  templateUrl: './user-signup.component.html',
  styleUrls: ['./user-signup.component.scss']
})
export class UserSignupComponent implements OnInit{

  /*property*/
  callBackMethod: EventEmitter<boolean> = new EventEmitter<boolean>();
  isLoading: boolean = false;
  public showPassword: boolean;
  public showConfirmPassword: boolean;

  /*form*/
  frmGroup: FormGroup;
  model: AppUser = new AppUser();

  /*DropdownList*/

  /*extra*/
  passwordPolicy: any;
  policyColor: any;
  COLOR_ACCEPT = 'green';
  COLOR_REJECT = 'red';

  constructor(
    public dialogRef: MatDialogRef<UserSignupComponent>,
    @Inject(MAT_DIALOG_DATA) data: any,
    private translationLoaderService: TranslationLoaderService,
    private formBuilder: FormBuilder,
    private matDialog: MatDialog,
    private appUtils: AppUtils,
    private appUserPublicService: AppUserPublicService,
    private passwordPolicyPublicService: PasswordPolicyPublicService,
    private commonValidator: CommonValidator,
    private mailSendPublicService: MailSendPublicService,
  ) {}


  ngOnInit(): void {
    this.setFormInitValue();
    this.getAgentPolicy();
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
  // @ Api Calling
  // -----------------------------------------------------------------------------------------------------

  getAgentPolicy(): void {
    this.passwordPolicyPublicService.getAgentPolicy().subscribe({
      next: (res) => {
        console.log(res.data);
        this.passwordPolicy = res.data;
      },
      error: (error) => {
        this.appUtils.onServerErrorResponse(error);
      }
    });
  }

  checkEmail(): void {
    const email: string = this.frmGroup.value.email;
    if(!email){
      return;
    }
    this.appUserPublicService.checkUserFromAppUserAndEmailFromCustomer(email).subscribe({
      next: (res) => {
        if(res.data){
          this.appUtils.showErrorMessage('Data Already Used!', 'Data Already Used!');
          this.frmGroup.patchValue({
            email: ''
          });
        }
      },
      error: (error) => {
        this.appUtils.onServerErrorResponse(error);
      }
    });
  }


  OtpDialog(): void {
    this.generateModel();
    /*Return if the form is invalid*/
    if ( !this.checkPassword()) {
      this.appUtils.showErrorMessage('All password policy not match !', 'All password policy not match !');
      return;
    }
    this.isLoading = true;
    this.mailSendPublicService.sendEmailForOTP(this.model.email||'',this.model.username||'').subscribe(
      res => {
        this.isLoading = false;
        if(res.data) {
          console.log(res.data);
          const dialogConfig = this.appUtils.getDialogConfig();
          dialogConfig.data = {
            model: this.model,
            otp: (Number(res.data)) / 13
          };
          const dialogRef = this.matDialog.open(OtpPageComponent, dialogConfig);
          dialogRef.componentInstance.callBackMethod.subscribe(value => {
            this.closeDialog();
          });
        }
      },
      err => console.log(err)
    );
  }

  // -----------------------------------------------------------------------------------------------------
  // @ view method
  // -----------------------------------------------------------------------------------------------------
  closeDialog(): void {
    this.dialogRef.close();
  }

  checkPassword(): boolean {
    const passwordValue = this.frmGroup.value.password;

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

    /*check matchUsername*/
    if (this.passwordPolicy.matchUsername){
      if (passwordValue === this.frmGroup.value.userName){
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
      name: ['',Validators.required],
      email: ['', {
        validators: [
          Validators.required,
          Validators.pattern('^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$')
        ]}],
      password: ['', Validators.required],
      confirmPassword: ['', Validators.required],
      active: [true],
    } ,{
      validators:this.commonValidator.mustMatch('password', 'confirmPassword')
    });
  }

  generateModel(){
    this.model.displayName = this.frmGroup.value.name;
    this.model.username = this.frmGroup.value.email;
    this.model.email = this.frmGroup.value.email;
    this.model.password = this.frmGroup.value.password;
    this.model.userTypeId = 2;
    this.model.passwordPolicyId   = this.passwordPolicy.id;
    this.model.passwordPolicyName = this.passwordPolicy.name;
    this.model.active = this.frmGroup.value.active;
  }

}
