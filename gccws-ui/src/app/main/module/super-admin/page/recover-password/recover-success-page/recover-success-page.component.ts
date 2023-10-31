import {Component, EventEmitter, Inject, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AppUtils} from "../../../../../core/utils/app.utils";
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material/dialog";
import {SnackbarHelper} from "../../../../../core/utils/snackbar.utils";
import {Router} from "@angular/router";
import {AppUserPublicService} from "../../../service/app-user-public.service";

@Component({
    selector: 'app-recover-success-page',
    templateUrl: './recover-success-page.component.html',
    styleUrls: ['./recover-success-page.component.scss']
})
export class RecoverSuccessPageComponent implements OnInit , OnDestroy{

  /*property*/
  isLoading: boolean = false;

  /*form*/
  frmGroup: FormGroup;

  constructor(
    public dialogRef: MatDialogRef<RecoverSuccessPageComponent>,
    @Inject(MAT_DIALOG_DATA) data: any,
    private formBuilder: FormBuilder,
    private matDialog: MatDialog,
    private appUserPublicService: AppUserPublicService,
    private appUtils: AppUtils,
    private snackbarHelper: SnackbarHelper,
    private router: Router,

  ) {

  }


  ngOnInit(): void {

  }

  ngOnDestroy(): void{

  }

  // -----------------------------------------------------------------------------------------------------
  // @ Api Calling
  // -----------------------------------------------------------------------------------------------------

  onSave(): void {

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


}
