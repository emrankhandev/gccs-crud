import {Component, EventEmitter, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {locale as lngEnglish} from "../../i18n/en";
import {locale as lngBangla} from "../../i18n/bn";
import {TranslationLoaderService} from "../../../../../../core/service/translation-loader.service";
import {PasswordPolicyService} from "../../../../service/password-policy.service";
import {AppUtils} from "../../../../../../core/utils/app.utils";
import {PasswordPolicy} from "../../../../model/password-policy";

@Component({
    selector: 'app-password-policy-add-dialog',
    templateUrl: './password-policy-add-dialog.component.html',
    styleUrls: ['./password-policy-add-dialog.component.scss']
})
export class PasswordPolicyAddDialogComponent implements OnInit {

  /*property*/
  callBackMethod: EventEmitter<boolean> = new EventEmitter<boolean>();
  isEdit: boolean;
  isLoading: boolean = false;

  /*form*/
  frmGroup: FormGroup;
  model: PasswordPolicy = new PasswordPolicy();

  /*dropdownList*/

  /*extra*/


  // -----------------------------------------------------------------------------------------------------
  // @ Init
  // -----------------------------------------------------------------------------------------------------
  constructor(
    public dialogRef: MatDialogRef<PasswordPolicyAddDialogComponent>,
    @Inject(MAT_DIALOG_DATA) data: any,
    private translationLoaderService: TranslationLoaderService,
     private formBuilder: FormBuilder,
     private modelService: PasswordPolicyService,
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
       minLength: ['', Validators.required],
       sequential: ['', ''],
       specialChar: ['', ''],
       alphanumeric: ['', ''],
       upperLower: ['', ''],
       passwordAge: ['', Validators.required],
       matchUsername: ['', ''],
       devCode: ['', ''],
       active: [true],
     });
  }

  edit(): void {
    this.frmGroup.patchValue({
      name: this.model.name,
      minLength: this.model.minLength,
      sequential: this.model.sequential,
      specialChar: this.model.specialChar,
      alphanumeric: this.model.alphanumeric,
      upperLower: this.model.upperLower,
      passwordAge: this.model.passwordAge,
      matchUsername: this.model.matchUsername,
      devCode: this.model.devCode,
    });

  }

  generateModel(isCreate: boolean): any {
       if (isCreate){this.model.id = undefined; }
       this.model.name = this.frmGroup.value.name;
       this.model.minLength = this.frmGroup.value.minLength;
       this.model.sequential = this.frmGroup.value.sequential;
       this.model.specialChar = this.frmGroup.value.specialChar;
       this.model.alphanumeric = this.frmGroup.value.alphanumeric;
       this.model.upperLower = this.frmGroup.value.upperLower;
       this.model.passwordAge = this.frmGroup.value.passwordAge;
       this.model.matchUsername = this.frmGroup.value.matchUsername;
       this.model.devCode = this.frmGroup.value.devCode;
       this.model.active = this.frmGroup.value.active;
  }
}
