import {Component, EventEmitter, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {SnackbarHelper} from "../../../../../core/utils/snackbar.utils";
import {AppUserPublicService} from "../../../service/app-user-public.service";
import {AppUser} from "../../../model/app-user";
import {MailSendPublicService} from "../../../service/mail-send-public.service";


@Component({
  selector: 'app-otp-page',
  templateUrl: './otp-page.component.html',
  styleUrls: ['./otp-page.component.scss']
})
export class OtpPageComponent implements OnInit{


  /*property*/
  callBackMethod: EventEmitter<boolean> = new EventEmitter<boolean>();
  isLoadingSubmit: boolean = false;
  isLoadingResend: boolean = false;
  isBack:boolean = false;

  /*form*/
  frmGroup: FormGroup;
  model: AppUser = new AppUser();

  otp: string;
  countDownTime : number = /*Time in Minutes*/ 10*60;
  timeString: string = '10:00';
  timeLimitInSeconds = this.countDownTime;
  timerElement : any;
  myInterval: number;


  constructor(
    public dialogRef: MatDialogRef<OtpPageComponent>,
    @Inject(MAT_DIALOG_DATA) data: any,
    private formBuilder: FormBuilder,
    private appUserPublicService : AppUserPublicService,
    private snackbarHelper: SnackbarHelper,
    private mailSendPublicService: MailSendPublicService,
  ) {
    this.model = data.model;
    this.otp = data.otp;
  }


  ngOnInit(): void {
    this.isBack = false;
    this.setFormInitValue();
    this.timerElement = document.getElementById('timer');
    this.myInterval = setInterval(() => {
      this.startTimer();
    }, 1000);
  }

  // -----------------------------------------------------------------------------------------------------
  // @ Api Calling
  // -----------------------------------------------------------------------------------------------------

  onSave(): void {
    this.isLoadingSubmit = true;
    this.appUserPublicService.create(this.model).subscribe(res => {
      this.isLoadingSubmit = false;
      {res.data ?
        this.snackbarHelper.openSuccessSnackBar('User Successfully Saved','OK')
        :
        this.snackbarHelper.openErrorSnackBar('Sign Up Failed','');
      }
      this.closeDialog();
    }, err => {
      this.isLoadingSubmit = false;
      console.log(err);
      this.snackbarHelper.openErrorSnackBar('Sign Up Error','');
    });
  }

  sendMail(){
    this.isLoadingResend = true;
    this.mailSendPublicService.sendEmailForOTP(this.model.email||'',this.model.username||'').subscribe(
      res => {
        if(res.data) {
          this.isLoadingResend = false;
          this.otp = String(+res.data / 13);
          this.timeLimitInSeconds = this.countDownTime;
          this.timerElement.textContent = this.timeString;
        }
      },
      err => {
        console.log(err);
        this.isLoadingResend = false;
      }
    );
  }

  // -----------------------------------------------------------------------------------------------------
  // @ view method
  // -----------------------------------------------------------------------------------------------------

  startTimer() {
    this.timeLimitInSeconds--;
    let minutes: number = Math.floor(this.timeLimitInSeconds / 60);
    let seconds = this.timeLimitInSeconds % 60;

    if (this.timeLimitInSeconds <= 0) {
      // @ts-ignore
      this.timerElement.textContent = this.timeString;
      clearInterval(this.myInterval);
      this.closeDialog();
      return;
    }
    // @ts-ignore
    this.timerElement.textContent = (minutes < 10 ? '0' + minutes : minutes) + ':' + (seconds < 10?'0' + seconds: seconds);
  }

  closeDialog(): void {
   this.isBack==true ? this.dialogRef.close() : this.callBackMethod.emit(true) ;
    this.dialogRef.close();
  }

  checkOTP(){
    if(this.frmGroup.value.otp != this.otp){
      return;
    }
    this.onSave();
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
