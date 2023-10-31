import {Component, EventEmitter, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {locale as lngEnglish} from "../../i18n/en";
import {locale as lngBangla} from "../../i18n/bn";
import {TranslationLoaderService} from "../../../../../../../core/service/translation-loader.service";
import {AppUtils} from "../../../../../../../core/utils/app.utils";
import {ApprovalConfiguration} from "../../../../model/approval-configuration";
import {ApprovalConfigurationService} from "../../../../service/approval-configuration.service";
import {DropdownModel} from "../../../../../../../core/model/dropdown-model";
import {ApprovalTeamService} from "../../../../service/approval-team.service";
import {ApprovalTeamDetails} from "../../../../model/approval-team-details";
import {MenuItemService} from "../../../../../service/menu-item.service";
import {AppUserService} from "../../../../../service/app-user.service";

import {ApprovalTeamModel} from "../../../../model/approval-team-model";
import {
  ApprovalTransactionType,
  ApprovalTransactionTypeService
} from "../../../../../../../core/mock-data/approval-transaction-type.service";
import {SetupDetails} from "../../../../../model/setup-details";

@Component({
    selector: 'app-approval-configuration-add-dialog',
    templateUrl: './approval-configuration-add-dialog.component.html',
    styleUrls: ['./approval-configuration-add-dialog.component.scss']
})
export class ApprovalConfigurationAddDialogComponent implements OnInit {

  /*property*/
  callBackMethod: EventEmitter<boolean> = new EventEmitter<boolean>();
  isEdit: boolean;
  isModuleId: boolean = true;
  isLoading: boolean = false;

  /*form*/
  frmGroup: FormGroup;
  model: ApprovalConfiguration = new ApprovalConfiguration();

  /*dropdownList*/
  appUserDropdownList: ApprovalTeamDetails[] = new Array<ApprovalTeamDetails>();
  formTeamList: ApprovalTeamDetails[] = new Array<ApprovalTeamDetails>();
  TeamByDepartmentList: ApprovalTeamModel[] = new Array<ApprovalTeamModel>();
  departmentDropdownList: SetupDetails[] = new Array<SetupDetails>();
  moduleDropdownList: DropdownModel[] = new Array<DropdownModel>();
  approvalTeamDropdownList: DropdownModel[] = new Array<DropdownModel>();
  transactionTypeDropdownList: ApprovalTransactionType[]= new Array<ApprovalTransactionType>();

  /*extra*/


  // -----------------------------------------------------------------------------------------------------
  // @ Init
  // -----------------------------------------------------------------------------------------------------
  constructor(
    public dialogRef: MatDialogRef<ApprovalConfigurationAddDialogComponent>,
    @Inject(MAT_DIALOG_DATA) data: any,
    private translationLoaderService: TranslationLoaderService,
    private formBuilder: FormBuilder,
    //private setupDetailsService: SetupDetailsService,
    private modelService: ApprovalConfigurationService,
    private appUserService: AppUserService,
    private menuItemService: MenuItemService,
    private approvalTeamService: ApprovalTeamService,
    private approvalTransactionTypeService: ApprovalTransactionTypeService,
    private appUtils: AppUtils,
  ) {
    this.translationLoaderService.loadTranslations(lngEnglish, lngBangla);
    this.model = data.model;
    this.isEdit = data.isEdit;
  }

  ngOnInit(): void {
    this.setFormInitValue();
    this.getDepartmentList();
    this.getTransactionTypeList();
    this.getModuleList();
    this.getApprovalTeamList();
    if (this.isEdit){
      this.edit();
    }
  }

  // -----------------------------------------------------------------------------------------------------
  // @ API calling
  // -----------------------------------------------------------------------------------------------------
  getDepartmentList(): any {
    // this.setupDetailsService.getByDevCode(this.appUtils.DEPARTMENT_ID).subscribe(res => {
    //   this.departmentDropdownList = res.data;
    //   console.log(res.data)
    //   if (this.isEdit){
    //     const objValue = this.model.departmentId ? this.departmentDropdownList.find(model => model.id === this.model.departmentId) : '';
    //     this.frmGroup.patchValue({
    //       department: objValue ? objValue : '',
    //     });
    //   }
    // });
  }

  getTransactionTypeList(): any {
    this.transactionTypeDropdownList = this.approvalTransactionTypeService.getList();
  }

  getTeamListByDepartment():any{
    const department = this.frmGroup.value.department;
    this.TeamByDepartmentList = [];
    if(!department){
      return;
    }
    this.approvalTeamService.getTeamByDepartmentId(department.id).subscribe(res => {
      console.log(res.data)
      this.TeamByDepartmentList = res.data
    });
  }

  getModuleList(): any {
    this.menuItemService.getModuleList().subscribe(res => {
      this.moduleDropdownList = res.data;
      if (this.isEdit) {
        const moduleValue = this.model.moduleId ? this.moduleDropdownList.find(model => model.id === this.model.moduleId) : '';
        this.frmGroup.patchValue({
          module: moduleValue ? moduleValue : '',
        });
      }
    });
  }

  getApprovalTeamList(): any {
    this.approvalTeamService.getDropdownList().subscribe(res => {
      this.approvalTeamDropdownList = res.data;
      if (this.isEdit) {
        const fromTeamValue = this.model.fromTeamId ? this.approvalTeamDropdownList.find(model => model.id === this.model.fromTeamId) : '';
        this.frmGroup.patchValue({
          fromTeam: fromTeamValue ? fromTeamValue : '',
        });
      }

      if (this.isEdit) {
        const toTeamValue = this.model.toTeamId ? this.approvalTeamDropdownList.find(model => model.id === this.model.toTeamId) : '';
        this.frmGroup.patchValue({
          toTeam: toTeamValue ? toTeamValue : '',
        });
        this.getNotifyAppUserList();
      }
    });
  }

  getFromTeamList():any{
    const fromTeam = this.frmGroup.value.fromTeam;
    this.formTeamList = [];
    if(!fromTeam){
      return;
    }
    this.approvalTeamService.getObjectById(Number(fromTeam.id)).subscribe(res => {
      this.formTeamList = res.data.detailsList.map(m => ({
        ...m,
        id: m.appUserId,
        name :"("+ (m.displayName)+") "+ m.appUserName,
      }));
      this.checkCommonTeamMember();
    });
  }


  getNotifyAppUserList(): any {
    const toTeam = this.frmGroup.value.toTeam;
    this.appUserDropdownList= [];
    if(!toTeam){
      return;
    }
    this.approvalTeamService.getObjectById(toTeam.id).subscribe(res => {
      this.appUserDropdownList = res.data.detailsList.map(m => ({
        ...m,
        id: m.appUserId,
        name :"("+ (m.displayName)+") "+ m.appUserName,
      }));

     this.checkCommonTeamMember();

      if (this.isEdit) {
        const notifyAppUserValue = this.model.notifyAppUserId ? this.appUserDropdownList.find(model => model.id === this.model.notifyAppUserId) : '';
        this.frmGroup.patchValue({
          notifyAppUser: notifyAppUserValue ? notifyAppUserValue : '',
        });
      }
    });
  }

  checkCommonTeamMember(){
    let isPresent: boolean = false;
    const commonUserList = this.formTeamList.filter(obj1 => this.appUserDropdownList.find(obj2 => obj2.id === obj1.id));
    if(commonUserList.length > 0)  {
      isPresent = true;
    }

    if(isPresent){
      this.appUtils.showErrorMessage("উভয় টিমে একই এমপ্লই পাওয়া গেছে", "Common Employee Found In Both Team!");
      this.frmGroup.patchValue({
        fromTeam: '',
        toTeam: '',
      });
      this.formTeamList = [];
      this.appUserDropdownList = [];
    }
  }

  onSave(): any {
    this.generateModel(true);
    this.isLoading = true;
    console.log(this.model);
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
      department: ['', Validators.required],
      transactionType: ['', Validators.required],
      module: ['', ''],
      serialNo: ['', Validators.required],
      fromTeam: ['', Validators.required],
      toTeam: ['', Validators.required],
      notifyAppUser: ['', Validators.required],
      fromAmount: ['', ''],
      toAmount: ['',''],
      approvalPermission: [true],
      backPermission: [true],
      changePermission: [true],

    });
  }

  edit(): void {
    const transactionTypeValue = this.model.transactionTypeId ? this.transactionTypeDropdownList.find(model => model.id === this.model.transactionTypeId) : '';

    this.frmGroup.patchValue({
      transactionType: transactionTypeValue ? transactionTypeValue : '',
      serialNo: this.model.serialNo,
      fromAmount: this.model.fromAmount,
      toAmount: this.model.toAmount,
      approvalPermission: this.model.approvalPermission,
      backPermission: this.model.backPermission,
      changePermission: this.model.changePermission,
    });

  }

  generateModel(isCreate: boolean): any{
    if (isCreate){this.model.id = undefined; }
    this.model.departmentId = this.frmGroup.value.department ? this.frmGroup.value.department.id : null;
    this.model.transactionTypeId = this.frmGroup.value.transactionType? this.frmGroup.value.transactionType.id : null;
    this.model.transactionTypeName = this.frmGroup.value.transactionType ? this.frmGroup.value.transactionType.name : null;
    this.model.moduleId = this.frmGroup.value.module ? this.frmGroup.value.module.id : null;
    this.model.serialNo = this.frmGroup.value.serialNo;
    this.model.fromTeamId = this.frmGroup.value.fromTeam ? this.frmGroup.value.fromTeam.id : null;
    this.model.toTeamId = this.frmGroup.value.toTeam ? this.frmGroup.value.toTeam.id : null;
    this.model.notifyAppUserId = this.frmGroup.value.notifyAppUser ? this.frmGroup.value.notifyAppUser.id : null;
    this.model.fromAmount = this.frmGroup.value.fromAmount;
    this.model.toAmount = this.frmGroup.value.toAmount;
    this.model.approvalPermission = this.frmGroup.value.approvalPermission;
    this.model.backPermission = this.frmGroup.value.backPermission;
    this.model.changePermission = this.frmGroup.value.changePermission;
  }
}
