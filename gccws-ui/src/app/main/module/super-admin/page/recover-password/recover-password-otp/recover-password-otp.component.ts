import {Component, EventEmitter, Inject, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AppUtils} from "../../../../../../main/core/utils/app.utils";
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material/dialog";
import {SnackbarHelper} from "../../../../../../main/core/utils/snackbar.utils";
import {Router} from "@angular/router";
import {NewPasswordComponent} from "../new-password/new-password.component";
import {AppUserPublicService} from "../../../service/app-user-public.service";
import {ChangePasswordModel} from "../../../model/change-password-model";

@Component({
    selector: 'app-recover-password-otp',
    templateUrl: './recover-password-otp.component.html',
    styleUrls: ['./recover-password-otp.component.scss']
})
export class RecoverPasswordOtpComponent implements OnInit , OnDestroy{

  /*property*/
  callBackMethod: EventEmitter<boolean> = new EventEmitter<boolean>();
  isLoading: boolean = false;

  /*form*/
  frmGroup: FormGroup;
  model: ChangePasswordModel = new ChangePasswordModel();
  customerInfoId:number;
  username: string;
  email: string;
  otp: string;
  type:number;
  agentType:number;



  timeout = /* 1 minute in millis */ 300000;
  constructor(
    public dialogRef: MatDialogRef<RecoverPasswordOtpComponent>,
    @Inject(MAT_DIALOG_DATA) data: any,
    private formBuilder: FormBuilder,
    private matDialog: MatDialog,
    private appUtils: AppUtils,
    private appUserPublicService: AppUserPublicService,
    private snackbarHelper: SnackbarHelper,
    private router: Router,

  ) {

    this.customerInfoId = data.customerInfoId;
    this.email = data.email;
    this.username = data.username;
    this.otp = data.otp;
    this.type = data.type;
    this.agentType = data.agentType;
  }




  ngOnInit(): void {
    this.setFormInitValue();
    this.closeOtpDialog();
  }

  closeOtpDialog(){
    this.dialogRef.afterOpened().subscribe(_ => {
      setTimeout(() => {
        this.dialogRef.close();
      }, this.timeout)
    })
  }

  ngOnDestroy(): void{

  }


  // -----------------------------------------------------------------------------------------------------
  // @ Api Calling
  // -----------------------------------------------------------------------------------------------------

  newPasswordDialog(): void {
    const otpFromInput = this.frmGroup.value.otp;
    if(otpFromInput == this.otp){
      this.isLoading = true;
      const dialogConfig = this.appUtils.getDialogConfig();
      dialogConfig.data = {
        username: this.username
      };
      this.closeDialog();
      const dialogRef = this.matDialog.open(NewPasswordComponent, dialogConfig);
      dialogRef.componentInstance.callBackMethod.subscribe(value => {
        this.callBackMethod.emit(true);
      });
    }else {
      this.snackbarHelper.openErrorSnackBar('Sorry! OTP Does Not Match!!', 'ok');
    }
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
      otp: ['', Validators.required],
    });
  }



}
