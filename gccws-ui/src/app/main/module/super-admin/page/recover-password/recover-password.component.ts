import {Component, EventEmitter, Inject, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AppUtils} from "../../../../core/utils/app.utils";
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material/dialog";
import {SnackbarHelper} from "../../../../core/utils/snackbar.utils";
import {Router} from "@angular/router";
import {ChangePasswordModel} from "../../model/change-password-model";
import {AppUserPublicService} from "../../service/app-user-public.service";
import {AppUser} from "../../model/app-user";
import {MailSendPublicService} from "../../service/mail-send-public.service";
import {RecoverPasswordOtpComponent} from "./recover-password-otp/recover-password-otp.component";



@Component({
    selector: 'app-recover-password',
    templateUrl: './recover-password.component.html',
    styleUrls: ['./recover-password.component.scss']
})
export class RecoverPasswordComponent implements OnInit , OnDestroy{

  /*property*/
  callBackMethod: EventEmitter<boolean> = new EventEmitter<boolean>();
  isLoading: boolean = false;
  otp : string ;
  value : string;
  type:number;


  sendMailBtnFlag: boolean = true;
  userId : number = 0;
  customerId : number | undefined;
  agentId : number | undefined;
  email : string | undefined;
  /*form*/
  frmGroup: FormGroup;
  model: ChangePasswordModel = new ChangePasswordModel();
  private userObject: AppUser = new AppUser();


  constructor(
    public dialogRef: MatDialogRef<RecoverPasswordComponent>,
    @Inject(MAT_DIALOG_DATA) data: any,
    private formBuilder: FormBuilder,
    private matDialog: MatDialog,
    private appUserPublicService: AppUserPublicService,
    private mailSendPublicService: MailSendPublicService,
    private appUtils: AppUtils,
    private snackbarHelper: SnackbarHelper,
    private router: Router,

  ) {
    this.userObject = data.model;
    this.type = data.type;
  }


  ngOnInit(): void {
    this.setFormInitValue();

    this.frmGroup.patchValue({
      email: this.userObject?.email ? this.userObject.email : '',
    });

  }

  ngOnDestroy(): void{

  }




  // -----------------------------------------------------------------------------------------------------
  // @ Api Calling
  // -----------------------------------------------------------------------------------------------------

  checkTypeForOtp():void{
     this.otpByEmail()
  }

  otpByEmail() :void{
    const email = this.userObject?.email;
    const userName = this.userObject?.displayName;
    this.isLoading = true;
    this.mailSendPublicService.sendEmailForOTP(email!, userName!).subscribe({
      next: (res) => {
        this.isLoading = false;
        if(res.data){
        const dialogConfig = this.appUtils.getDialogConfig()
        dialogConfig.data = {
          email: this.userObject.email,
          username: this.userObject.username,
          otp : ''+(+(res.data) / 13),
          type:1,//customer for 2, app user for 1
        };
          this.closeDialog();
          const dialogRef = this.matDialog.open(RecoverPasswordOtpComponent, dialogConfig);
          dialogRef.componentInstance.callBackMethod.subscribe(value => {
            this.callBackMethod.emit(true);
          });
        }else{
          this.snackbarHelper.openErrorSnackBar('No input was found in the Email field!', 'ok');
        }
      },
      error: (error) => {
        this.appUtils.onServerErrorResponse(error);
      }
    });
  }



  // -----------------------------------------------------------------------------------------------------
  // @ view method
  // -----------------------------------------------------------------------------------------------------

  closeDialog(): void {
    this.dialogRef.close();
  }


  // -----------------------------------------------------------------------------------------------------
  // @ Helper Method
  // -----------------------------------------------------------------------------------------------------

  setFormInitValue(): any {
    this.frmGroup = this.formBuilder.group({
      email: ['',[Validators.required,Validators.pattern('[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}')]],
    });
  }



}
