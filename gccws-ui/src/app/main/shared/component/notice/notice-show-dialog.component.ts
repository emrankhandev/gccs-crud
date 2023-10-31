import {Component, Inject, OnInit} from '@angular/core';
import {TranslationLoaderService} from "../../../core/service/translation-loader.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AppUtils} from "../../../core/utils/app.utils";
import {LocalStoreUtils} from "../../../core/utils/local-store.utils";
import {SnackbarHelper} from "../../../core/utils/snackbar.utils";
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material/dialog";
import {MessageHistory} from "../../model/message-history";
import {locale as lngEnglish} from "./i18n/en";
import {locale as lngBangla} from "./i18n/bn";
import {MessageHistoryService} from "../../service/message-history.service";
import {FileService} from "../../service/file.service";

@Component({
    selector: 'app-notice-show',
    templateUrl: './notice-show-dialog.component.html',
    styleUrls: ['./notice-show-dialog.component.scss']
})
export class NoticeShowDialogComponent implements OnInit {



  /*property*/
  isLoading: boolean = false;

  /*form*/
  frmGroup: FormGroup;
  model: MessageHistory = new MessageHistory();

  /*dropdownList*/


  /*extra*/
  userInfo: any;
  validDate:Date;

    constructor(
      public dialogRef: MatDialogRef<NoticeShowDialogComponent>,
      @Inject(MAT_DIALOG_DATA) data: any,
      private matDialog: MatDialog,
      private formBuilder: FormBuilder,
      private translationLoaderService: TranslationLoaderService,
      private modelService: MessageHistoryService,
      private appUtils: AppUtils,
      private localStoreUtils: LocalStoreUtils,
      public fileService: FileService,

    ) {
      this.translationLoaderService.loadTranslations(lngEnglish, lngBangla);
      this.model = data.model;
    }

    ngOnInit(): void {
      this.userInfo = this.localStoreUtils.getUserInfo();
      this.setFormInitValue();
      this.getValidDate();
    }

  // -----------------------------------------------------------------------------------------------------
  // @ API calling
  // -----------------------------------------------------------------------------------------------------
 getValidDate(){
      this.validDate= new Date(this.model.attachment)
   const aYearFromNow = new Date(this.model.publishDate);
   this.validDate = new Date(aYearFromNow.setFullYear(aYearFromNow.getFullYear() + 1));
 }



  // -----------------------------------------------------------------------------------------------------
  // @ view method
  // -----------------------------------------------------------------------------------------------------
  closeDialog(): void {
    this.dialogRef.close();
  }

  setFormInitValue(): any {
    this.frmGroup = this.formBuilder.group({
    });
  }


}
