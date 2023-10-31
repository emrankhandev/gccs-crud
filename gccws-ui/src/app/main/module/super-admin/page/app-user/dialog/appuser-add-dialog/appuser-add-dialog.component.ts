import {Component, EventEmitter, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {AppUser} from "../../../../model/app-user";
import {locale as lngEnglish} from "../../i18n/en";
import {locale as lngBangla} from "../../i18n/bn";
import {DropdownModel} from "../../../../../../core/model/dropdown-model";
import {TranslationLoaderService} from "../../../../../../core/service/translation-loader.service";
import {AppUserService} from "../../../../service/app-user.service";
import {AppUtils} from "../../../../../../core/utils/app.utils";
import {PasswordPolicyService} from "../../../../service/password-policy.service";
import {UserNameValidator} from "../../username.validator";
import {CommonValidator} from "../../../../../../core/validator/common.validator";
import {PasswordPolicy} from "../../../../model/password-policy";

@Component({
  selector: 'appuser-add-dialog',
  templateUrl: './appuser-add-dialog.component.html',
  styleUrls: ['./appuser-add-dialog.component.scss']
})
export class AppuserAddDialogComponent implements OnInit {

  /*property*/
  callBackMethod: EventEmitter<boolean> = new EventEmitter<boolean>();
  isEdit: boolean;
  employeeId: number | undefined;
  isLoading: boolean = false;

  /*form*/
  frmGroup: FormGroup;
  model: AppUser = new AppUser();


  /*dropdownList*/
  passwordPolicyDropdownList : DropdownModel[] = new Array<DropdownModel>();
  passwordPolicy : PasswordPolicy = new PasswordPolicy();
  empDropdownList: DropdownModel[] = new Array<DropdownModel>();



  /*extra*/
  passwordPolicyId: number;
  policyColor: any;
  COLOR_ACCEPT = 'green';
  COLOR_REJECT = 'red';
  isValid:boolean = false;

  // -----------------------------------------------------------------------------------------------------
  // @ Init
  // -----------------------------------------------------------------------------------------------------
  constructor(
    public dialogRef: MatDialogRef<AppuserAddDialogComponent>,
    @Inject(MAT_DIALOG_DATA) data: any,
    private translationLoaderService: TranslationLoaderService,
    private formBuilder: FormBuilder,
    private modelService: AppUserService,
    private passwordPolicyService: PasswordPolicyService,
    private appUtils: AppUtils,
  ) {
    this.translationLoaderService.loadTranslations(lngEnglish, lngBangla);
    this.model = data.model;
    this.isEdit = data.isEdit;
  }

  ngOnInit(): void {
    this.setFormInitValue();
    this.getPasswordPolicyActiveList();
    this.policyColor = {
      minLength: this.COLOR_REJECT,
      alphanumeric: this.COLOR_REJECT,
      sequential: this.COLOR_REJECT,
      specialChar: this.COLOR_REJECT,
      upperLower: this.COLOR_REJECT,
      matchUsername: this.COLOR_REJECT,
    };
    if (this.isEdit) {
      this.edit();
    }
  }

  // -----------------------------------------------------------------------------------------------------
  // @ API calling
  // -----------------------------------------------------------------------------------------------------

  getPasswordPolicyActiveList(): any {
    this.passwordPolicyService.getDropdownList().subscribe(res => {
      this.passwordPolicyDropdownList = res.data;
      if (this.isEdit) {
        const passwordPolicyValue = this.model.passwordPolicyId ? this.passwordPolicyDropdownList.find(model => model.id === this.model.passwordPolicyId) : '';
        this.frmGroup.patchValue({
          passwordPolicy: passwordPolicyValue ? passwordPolicyValue : '',
        });
        this.getPasswordPolicyById();
      }
    });
  }


  checkEmp():any {
    const empId = this.frmGroup.value.employee.id;
    this.modelService.getEmpId(empId).subscribe(res => {
      if(res.data?.empPersonalInfoId == empId){
        //dropdown empty
        this.frmGroup.patchValue({
          employee:  '',
        });
        this.appUtils.showErrorMessage("কর্মচারী ইতিমধ্যেই বিদ্যমান", "Employee Already Exist");
      }
    });
  }

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

  getPasswordPolicyById():void{
    this.passwordPolicyId =  this.frmGroup.value.passwordPolicy.id;
    this.passwordPolicyService.getObjectById(this.passwordPolicyId).subscribe(res => {
      this.passwordPolicy = res.data;
      if (this.isEdit) {
        const passwordPolicyValue = this.model.passwordPolicyId ? this.passwordPolicyDropdownList.find(model => model.id === this.passwordPolicyId) : '';
        this.frmGroup.patchValue({
          passwordPolicy: passwordPolicyValue ? passwordPolicyValue : '',
        });
      }
    });
    //this.checkMethodType(row);
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
      if (passwordValue == this.frmGroup.value.userName){
        res = false;
        this.policyColor.matchUsername = this.COLOR_REJECT;
      }else {
        this.policyColor.matchUsername = this.COLOR_ACCEPT;
      }
    }

    return res;
  }

  // -----------------------------------------------------------------------------------------------------
  // @ Helper Method
  // -----------------------------------------------------------------------------------------------------

  unamePattern= "[a-zA-Z0-9]*";

  setFormInitValue(): any {
    this.frmGroup = this.formBuilder.group({
      passwordPolicy: ['', Validators.required],
      userName: ['', [Validators.required, Validators.pattern(this.unamePattern), UserNameValidator.cannotContainSpace]],
      password: ['', Validators.required],
      displayName: ['', Validators.required],
      email: ['',  ''],
      mobile: ['', ''],
      employee: ['',''],
      active: [true],
    });
  }

  edit(): void {
    this.frmGroup.patchValue({
      userName: this.model.username,
      password: '',
      displayName: this.model.displayName,
      email: this.model.email,
      mobile: this.model.mobile,
      active: this.model.active,
    });
  }

  generateModel(isCreate: boolean): any {
    if (isCreate) {
      this.model.id = undefined;
    }
    this.model.passwordPolicyId = this.frmGroup.value.passwordPolicy ? this.frmGroup.value.passwordPolicy.id : null;
    this.model.username = this.frmGroup.value.userName;
    this.model.password = this.frmGroup.value.password;
    this.model.displayName = this.frmGroup.value.displayName;
    this.model.email = this.frmGroup.value.email;
    this.model.mobile = this.frmGroup.value.mobile;
    this.model.empPersonalInfoId = this.frmGroup.value.employee? this.frmGroup.value.employee.id : null;
    this.model.active = this.frmGroup.value.active;
  }

}
