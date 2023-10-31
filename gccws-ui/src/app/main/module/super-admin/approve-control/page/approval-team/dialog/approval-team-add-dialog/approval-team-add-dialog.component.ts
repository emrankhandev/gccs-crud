import {Component, EventEmitter, Inject, OnInit} from '@angular/core';
import {AbstractControl, FormArray, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {BehaviorSubject} from "rxjs";
import {AppUtils} from "../../../../../../../core/utils/app.utils";
import {ApprovalTeamModel} from "../../../../model/approval-team-model";
import {ApprovalTeamService} from "../../../../service/approval-team.service";
import {ApprovalTeamDetails} from "../../../../model/approval-team-details";
import {ApprovalTeamMaster} from "../../../../model/approval-team-master";
import {DropdownModel} from "../../../../../../../core/model/dropdown-model";
import {AppUserService} from "../../../../../service/app-user.service";

@Component({
    selector: 'app-role-assign-add-dialog',
    templateUrl: './approval-team-add-dialog.component.html',
    styleUrls: ['./approval-team-add-dialog.component.scss']
})
export class ApprovalTeamAddDialogComponent implements OnInit {

  /*property*/
  callBackMethod: EventEmitter<boolean> = new EventEmitter<boolean>();
  isEdit: boolean;
  isLoading: boolean = false;

  /*form*/
  frmGroup: FormGroup;
  model: ApprovalTeamModel = new ApprovalTeamModel();

  /*master details*/
  displayColumnsDetails = ['appUserName',  'action'];
  dataSourceDetails = new BehaviorSubject<AbstractControl[]>([]);
  rows: FormArray = this.formBuilder.array([]);
  frmGroupDetails: FormGroup = this.formBuilder.group({
    scope: this.rows
  });

  /*dropdownList*/
  appUserDropdownList: DropdownModel[] = new Array<DropdownModel>();

  /*extra*/

  // -----------------------------------------------------------------------------------------------------
  // @ Init
  // -----------------------------------------------------------------------------------------------------
  constructor(
    public dialogRef: MatDialogRef<ApprovalTeamAddDialogComponent>,
    @Inject(MAT_DIALOG_DATA) data: any,
    private formBuilder: FormBuilder,
    private modelService: ApprovalTeamService,
    private appUserService: AppUserService,
    private appUtils: AppUtils,
  ) {
    this.model = data.model;
    this.isEdit = data.isEdit;
  }

  ngOnInit(): void {
    this.setFormInitValue();
    this.addRow();
    this.getAppUserList();
    if (this.isEdit){
      this.edit();
    }
  }

  // -----------------------------------------------------------------------------------------------------
  // @ API calling
  // -----------------------------------------------------------------------------------------------------

  getAppUserList(): any {
    this.appUserService.getDropdownList().subscribe(res => {
      console.log(res.data)
      this.appUserDropdownList = res.data;
      if (this.isEdit){
        this.model.detailsList.forEach((value, index) => {
          const appUserValue = value.appUserId ? this.appUserDropdownList.find(model => model.id === value.appUserId) : '';
          this.rows.at(index).patchValue({
            appUser: appUserValue ? appUserValue : '',
          });
        });
      }
    });
  }

  onSave(): any {
    // //Object validation
    // this.frmGroup.value.appUser
    // if(this.isDataUsed(this.modelList, null, this.frmGroup.value.appUser.id, true)){
    //   const message = this.appUtils.isLocalActive() ? DATA_ALREADY_USED_BN : DATA_ALREADY_USED;
    //   const ok = this.appUtils.isLocalActive() ? this.snackbarHelper.TEXT_OK_BN :this.snackbarHelper.TEXT_OK;
    //   this.snackbarHelper.openErrorSnackBar(message, ok);
    //   return;
    // }
    this.generateModel(true);
    this.isLoading = true;
    console.log(this.model)
    this.modelService.create(this.model).subscribe({
      next: (res) => {
        console.log(this.model);
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
    //Object validation
    // if(this.isDataUsed(this.modelList, this.model.master.appUserId, this.frmGroup.value.appUser.id, false)){
    //   const message = this.appUtils.isLocalActive() ? DATA_ALREADY_USED_BN : DATA_ALREADY_USED;
    //   const ok = this.appUtils.isLocalActive() ? this.snackbarHelper.TEXT_OK_BN :this.snackbarHelper.TEXT_OK;
    //   this.snackbarHelper.openErrorSnackBar(message, ok);
    //   return;
    // }

    this.generateModel(false);
    console.log(this.model);
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

  /*master details*/
  addRow(value?: ApprovalTeamDetails, index?: number): any {
    const row = this.formBuilder.group({
      id: [value ? value.id : ''],
      appUser: ['', Validators.required],
    });
    this.rows.push(row);
    this.updateView();
  }

  deleteRow(index: any): any {
    if (this.rows.length === 1) {
      this.appUtils.detailsLastEntryDeleteMsg();
      return false;
    } else {
      this.rows.removeAt(index);
      this.updateView();
      return true;
    }
  }

  checkUserItem(row: any): void{
    const selectValue = row.value.appUser;
    let count = 0;
    this.rows.getRawValue().forEach(e => {
      if (e.appUser.id === selectValue.id){ count ++; }
    });
    if (count > 1){
      this.appUtils.itemAlreadyTakenMsg();
      row.patchValue({ appUser: '' });
    }
  }

  // -----------------------------------------------------------------------------------------------------
  // @ Helper Method
  // -----------------------------------------------------------------------------------------------------

  setFormInitValue(): any {
    this.frmGroup = this.formBuilder.group({
      name: ['', Validators.required],
      remarks: ['', ''],
      active: [true]
    });
  }

  edit(): void {
    /*master*/
    this.frmGroup.patchValue({
      name: this.model.master.name,
      remarks: this.model.master.remarks,

    });

    /*details*/
    this.rows.clear();
    this.model.detailsList.forEach((value, index) => {
      this.addRow(value, index);
    });
  }

  generateModel(isCreate: boolean): any{
    /*master*/
    if (isCreate) {
      this.model.master = new ApprovalTeamMaster();
      this.model.master.id = undefined;
    }
    this.model.master.name = this.frmGroup.value.name;
    this.model.master.remarks = this.frmGroup.value.remarks;

    this.model.master.active = this.frmGroup.value.active;
    /*details*/
    const detailsList: ApprovalTeamDetails[] = [];
    this.rows.getRawValue().forEach(e => {
      e.appUserId = e.appUser ? e.appUser.id : null;
      detailsList.push(e);
    });
    this.model.detailsList = detailsList;
  }

  /*master details*/
  updateView(): any {
    this.dataSourceDetails.next(this.rows.controls);
  }

}
