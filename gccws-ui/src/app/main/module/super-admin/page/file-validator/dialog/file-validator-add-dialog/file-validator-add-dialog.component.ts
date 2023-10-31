import {Component, EventEmitter, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {locale as lngEnglish} from "../../i18n/en";
import {locale as lngBangla} from "../../i18n/bn";
import {TranslationLoaderService} from "../../../../../../core/service/translation-loader.service";
import {AppUtils} from "../../../../../../core/utils/app.utils";
import {FileValidator} from "../../../../model/file-validator";
import {FileValidatorService} from "../../../../service/file-validator.service";

@Component({
    selector: 'app-file-validator-add-dialog',
    templateUrl: './file-validator-add-dialog.component.html',
    styleUrls: ['./file-validator-add-dialog.component.scss']
})
export class FileValidatorAddDialogComponent implements OnInit {

  /*property*/
  callBackMethod: EventEmitter<boolean> = new EventEmitter<boolean>();
  isEdit: boolean;
  isLoading: boolean = false;

  /*form*/
  frmGroup: FormGroup;
  model: FileValidator = new FileValidator();

  /*dropdownList*/

  /*extra*/


  // -----------------------------------------------------------------------------------------------------
  // @ Init
  // -----------------------------------------------------------------------------------------------------
  constructor(
    public dialogRef: MatDialogRef<FileValidatorAddDialogComponent>,
    @Inject(MAT_DIALOG_DATA) data: any,
    private translationLoaderService: TranslationLoaderService,
     private formBuilder: FormBuilder,
     private modelService: FileValidatorService,
     private appUtils: AppUtils,
  ) {
    this.translationLoaderService.loadTranslations(lngEnglish, lngBangla);
    this.model = data.model;
    this.isEdit = data.isEdit;
  }

  ngOnInit(): void {
    this.setFormInitValue();
    if (this.isEdit){
      this.edit();
    }
  }

  // -----------------------------------------------------------------------------------------------------
  // @ API calling
  // -----------------------------------------------------------------------------------------------------
  onSave(): any {
    this.generateModel(true);
    this.isLoading = true;
    this.modelService.create(this.model).subscribe({
      next: (res) => {
        this.isLoading = false;
        this.appUtils.onServerResponse(res, this.closeDialog.bind(this));
        this.callBackMethod.emit(true);
      },
      error: (error) => {
        this.isLoading = false;
        this.appUtils.onServerErrorResponse(error);
      }
    });
  }

  onUpdate(): any {
    this.generateModel(false);
    this.isLoading = true;
    this.modelService.update(this.model).subscribe({
      next: (res) => {
        this.isLoading = false;
        this.appUtils.onServerResponse(res, this.closeDialog.bind(this));
        this.callBackMethod.emit(true);
      },
      error: (error) => {
        this.isLoading = false;
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

  clearFormData(): any {
    this.setFormInitValue();
    this.isEdit = false;
  }

  // -----------------------------------------------------------------------------------------------------
  // @ Helper Method
  // -----------------------------------------------------------------------------------------------------

  setFormInitValue(): any {
     this.frmGroup = this.formBuilder.group({
       name: ['', Validators.required],
       fileExtensions: ['', ''],
       fileSize: ['', ''],
       fileHeight: ['', ''],
       fileWidth: ['', ''],
       isFixed: [false, ''],
       active: [true],
     });
  }

  edit(): void {
    this.frmGroup.patchValue({
      name: this.model.name,
      fileExtensions: this.model.fileExtensions,
      fileSize: this.model.fileSize,
      fileHeight: this.model.fileHeight,
      fileWidth: this.model.fileWidth,
      isFixed: this.model.isFixed,
    });

  }

  generateModel(isCreate: boolean): any {
       if (isCreate){this.model.id = undefined; }
       this.model.name = this.frmGroup.value.name;
       this.model.fileExtensions = this.frmGroup.value.fileExtensions;
       this.model.fileSize = this.frmGroup.value.fileSize;
       this.model.fileHeight = this.frmGroup.value.fileHeight;
       this.model.fileWidth = this.frmGroup.value.fileWidth;
       this.model.isFixed = this.frmGroup.value.isFixed;
       this.model.active = this.frmGroup.value.active;
  }
}
