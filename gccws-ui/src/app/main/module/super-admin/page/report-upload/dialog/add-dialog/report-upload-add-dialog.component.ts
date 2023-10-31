import {Component, EventEmitter, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {ReportUpload} from "../../../../model/report-upload";
import {ReportUploadService} from "../../../../service/report-upload.service";
import {locale as lngEnglish} from "../../i18n/en";
import {locale as lngBangla} from "../../i18n/bn";
import {TranslationLoaderService} from "../../../../../../core/service/translation-loader.service";
import {AppConstants} from "../../../../../../core/constants/app.constants";
import {AppUtils} from "../../../../../../core/utils/app.utils";
import {HttpClient} from "@angular/common/http";
import {FileValidator} from "../../../../model/file-validator";
import {FileValidatorService} from "../../../../service/file-validator.service";
import {CommonValidator} from "../../../../../../core/validator/common.validator";

/**
 * @Author		Emran Khan
 * @Since		  February 8, 2023
 * @version		1.0.0
 */

@Component({
  selector: 'app-report-upload-add-dialog',
  templateUrl: './report-upload-add-dialog.component.html',
  styleUrls: ['./report-upload-add-dialog.component.scss']
})

export class ReportUploadAddDialogComponent implements OnInit {

  /*property*/
  callBackMethod: EventEmitter<boolean> = new EventEmitter<boolean>();
  isEdit: boolean;
  isLoading: boolean = false;

  /*form*/
  frmGroup: FormGroup;
  model: ReportUpload = new ReportUpload();

  /*file*/
  URL = this.appUtils.getPrivateBaseURL() + this.appConstants.moduleSuperAdmin + 'report-upload';

  fileTypeFile : string = 'file';
  fileValidatorFile: FileValidator = new FileValidator();
  BYTE_TO_KB: number = 1024;
  // -----------------------------------------------------------------------------------------------------
  // @ Init
  // -----------------------------------------------------------------------------------------------------
  constructor(
    public dialogRef: MatDialogRef<ReportUploadAddDialogComponent>,
    @Inject(MAT_DIALOG_DATA) data: any,
    private translationLoaderService: TranslationLoaderService,
    private formBuilder: FormBuilder,
    private modelService: ReportUploadService,
    private appConstants: AppConstants,
    public fileValidatorService: FileValidatorService,
    public commonValidator: CommonValidator,
    private appUtils: AppUtils,
    private http: HttpClient,
  ) {
    this.translationLoaderService.loadTranslations(lngEnglish, lngBangla);
    this.model = data.model;
    this.isEdit = data.isEdit;
  }

  ngOnInit(): void {
    this.setFormInitValue();
    this. getFileValidator();
    if (this.isEdit){
      this.edit();
    }
  }

  // -----------------------------------------------------------------------------------------------------
  // @ API calling
  // -----------------------------------------------------------------------------------------------------

  getFileValidator(): void {
    this.fileValidatorService.getByDevCode(this.appUtils.DEV_CODE_REPORT_UPLOAD_VALIDATOR).subscribe(res => {
      this.fileValidatorFile = res.data;
      this.fileValidatorFile.fileSize = res.data.fileSize * this.BYTE_TO_KB;
      this.fileValidatorFile.fileExtensions = res.data.fileExtensions.split(',');
    });
  }

  onSave(): any {
    this.generateModel(true);
    // @ts-ignore
    const file = this.frmGroup.get('file').value;
    let reportObj = {
      id: this.model.id,
      code: this.model.code,
      remarks: this.model.remarks,
      active: this.model.active,
      isSubreport: this.model.isSubreport
    }

    //form data
    const formData = new FormData();
    formData.append('reportObj', new Blob([JSON.stringify(reportObj)], {
      type: "application/json",
    }));
    formData.append('file', file);

    //save
    this.isLoading = true;
    this.http.post<any>(this.URL, formData).subscribe(
      (res => {
        this.isLoading = false;
        this.appUtils.onServerResponse(res, this.closeDialog.bind(this));
        this.callBackMethod.emit(true);
      }),
      (error =>{
        this.isLoading = false;
        this.appUtils.onServerErrorResponse(error);
      })
    );
    console.log(this.model);
  }

  onUpdate(): any {
    this.generateModel(false);
    // @ts-ignore
    const file = this.frmGroup.get('file').value;
    let reportObj = {
      id: this.model.id,
      code: this.model.code,
      remarks: this.model.remarks,
      active: this.model.active,
      isSubreport: this.model.isSubreport
    }

    //form data
    const formData = new FormData();
    formData.append('reportObj', new Blob([JSON.stringify(reportObj)], {
      type: "application/json",
    }));
    formData.append('file', file);

    //save
    this.isLoading = true;
    this.http.put<any>(this.URL, formData).subscribe(
      (res => {
        this.isLoading = false;
        this.appUtils.onServerResponse(res, this.closeDialog.bind(this));
        this.callBackMethod.emit(true);
      }),
      (error =>{
        this.isLoading = false;
        this.appUtils.onServerErrorResponse(error);
      })
    );
  }

  // -----------------------------------------------------------------------------------------------------
  // @ view method
  // -----------------------------------------------------------------------------------------------------

  closeDialog(): void {
      this.dialogRef.close();
  }

  clearFormData(): any {
    this.setFormInitValue();
    this.isEdit = false;
  }

  onFileSelect(event: any, formControll: string, fileType: string): any {
    if (event.target.files.length > 0) {
      const file = event.target.files[0];
      let fileValidator: FileValidator = new FileValidator();
      switch (fileType) {
        case this.fileTypeFile:
          fileValidator = this.fileValidatorFile;
          break;
      }
      if (file && this.commonValidator.isValidImage(file, fileValidator)){
        if (fileValidator.fileHeight && fileValidator.fileWidth){
          this.commonValidator.checkWidthHeight(file, fileValidator).then((isValidFile) => {
            if (isValidFile){
              this.frmGroup.get(formControll)?.setValue(file);
            }else {
              event.target.value = null;
            }
          });
        }else {
          this.frmGroup.get(formControll)?.setValue(file);
        }
      }else {
        event.target.value = null;
      }
    }
  }

  // -----------------------------------------------------------------------------------------------------
  // @ Helper Method
  // -----------------------------------------------------------------------------------------------------

  setFormInitValue(): any {
    this.frmGroup = this.formBuilder.group({
      code: ['', Validators.required],
      file: ['', Validators.required],
      fileNameControl: ['', ''],
      remarks: ['', ''],
      isSubreport: [false],
      active: [true]
    });
  }

  edit(): void {
    this.frmGroup.patchValue({
      code: this.model.code,
      file: '',
      remarks: this.model.remarks,
      isSubreport: this.model.isSubreport,
      active: this.model.active,
    });
  }

  generateModel(isCreate: boolean): any{
    if (isCreate){this.model.id = undefined; }
    this.model.code = this.frmGroup.value.code;
    this.model.remarks = this.frmGroup.value.remarks;
    this.model.isSubreport = this.frmGroup.value.isSubreport;
    this.model.active = this.frmGroup.value.active;
  }

}
