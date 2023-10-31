import {Component, EventEmitter, Inject, OnInit} from '@angular/core';
import {AbstractControl, FormArray, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {BehaviorSubject} from "rxjs";
import {UserRoleService} from "../../../../service/user-role.service";
import {DropdownModel} from "../../../../../../core/model/dropdown-model";
import {AppUtils} from "../../../../../../core/utils/app.utils";
import {UserRoleAssignModel} from "../../../../model/user-role-assign-model";
import {UserRoleAssignService} from "../../../../service/user-role-assign.service";
import {DATA_ALREADY_USED, DATA_ALREADY_USED_BN} from "../../../../../../core/constants/message";
import {UserRoleAssignDetails} from "../../../../model/user-role-assign-details";
import {UserRoleAssignMaster} from "../../../../model/user-role-assign-master";
import {AppUserService} from "../../../../service/app-user.service";


@Component({
    selector: 'user-role-assign-master-add-dialog',
    templateUrl: './user-role-assign-add-dialog.component.html',
    styleUrls: ['./user-role-assign-add-dialog.component.scss']
})
export class UserRoleAssignAddDialogComponent implements OnInit {

  /*property*/
  callBackMethod: EventEmitter<boolean> = new EventEmitter<boolean>();
  isEdit: boolean;
  isLoading: boolean = false;

  /*form*/
  frmGroup: FormGroup;
  model: UserRoleAssignModel = new UserRoleAssignModel();
  modelList: UserRoleAssignModel[] = new Array<UserRoleAssignModel>();


  /*master details*/
  displayColumnsDetails = ['userRoleName',  'action'];
  dataSourceDetails = new BehaviorSubject<AbstractControl[]>([]);
  rows: FormArray = this.formBuilder.array([]);
  frmGroupDetails: FormGroup = this.formBuilder.group({
    scope: this.rows
  });

  /*dropdownList*/
  appUserDropdownList: DropdownModel[] = new Array<DropdownModel>();
  userRoleDropdownList: DropdownModel[] = new Array<DropdownModel>();

  /*extra*/

  // -----------------------------------------------------------------------------------------------------
  // @ Init
  // -----------------------------------------------------------------------------------------------------
  constructor(
    public dialogRef: MatDialogRef<UserRoleAssignAddDialogComponent>,
    @Inject(MAT_DIALOG_DATA) data: any,
    private formBuilder: FormBuilder,
    private modelService: UserRoleAssignService,
    private appUserService: AppUserService,
    private userRoleService: UserRoleService,
    private appUtils: AppUtils,
  ) {
    this.model = data.model;
    this.isEdit = data.isEdit;
  }

  ngOnInit(): void {
    this.setFormInitValue();
    this.addRow();
    this.getAppUserList();
    this.getUserRoleList();
    if (this.isEdit){
      this.edit();
    }
  }

  // -----------------------------------------------------------------------------------------------------
  // @ API calling
  // -----------------------------------------------------------------------------------------------------

  getAppUserList(): any {
    this.appUserService.getDropdownList().subscribe(res => {
      this.appUserDropdownList = res.data;
      if (this.isEdit) {
        const objValue = this.model.master.appUserId ? this.appUserDropdownList.find(model => model.id === this.model.master.appUserId) : '';
        this.frmGroup.patchValue({
          appUser: objValue ? objValue : '',
        });

      }
    })
  }

  getUserRoleList(): any {
    this.userRoleService.getDropdownList().subscribe(res => {
      this.userRoleDropdownList = res.data;
      if (this.isEdit){
        this.model.detailsList.forEach((value, index) => {
          const userRoleValue = value.userRoleId ? this.userRoleDropdownList.find(model => model.id === value.userRoleId) : '';
          this.rows.at(index).patchValue({
            userRole: userRoleValue ? userRoleValue : '',
          });
        });
      }
    });
  }

  onSave(): any {
    if(this.isDataUsed(this.modelList, null, this.frmGroup.value.appUser.id, true)){
      this.appUtils.showErrorMessage(DATA_ALREADY_USED_BN, DATA_ALREADY_USED);
      return;
    }
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
    if(this.isDataUsed(this.modelList, this.model.master.appUserId, this.frmGroup.value.appUser.id, false)){
      this.appUtils.showErrorMessage(DATA_ALREADY_USED_BN, DATA_ALREADY_USED);
      return;
    }
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
    this.rows.clear();
    this.addRow();
  }

  /*master details*/
  addRow(value?: UserRoleAssignDetails, index?: number): any {
    const row = this.formBuilder.group({
      id: [value ? value.id : ''],
      userRole: ['', Validators.required],
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

  checkRoleItem(row: any): void{
    const selectValue = row.value.userRole;
    let count = 0;
    this.rows.getRawValue().forEach(e => {
      if (e.userRole.id === selectValue.id){ count ++; }
    });
    if (count > 1){
      this.appUtils.itemAlreadyTakenMsg();
      row.patchValue({ userRole: '' });
    }
  }


  // -----------------------------------------------------------------------------------------------------
  // @ Helper Method
  // -----------------------------------------------------------------------------------------------------

  setFormInitValue(): any {
    this.frmGroup = this.formBuilder.group({
      appUser: ['', Validators.required],
      active: [true]
    });
  }

  edit(): void {
    /*master*/
    this.frmGroup.patchValue({
      appUserName: this.model.master.appUserName,

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
      this.model.master = new UserRoleAssignMaster();
      this.model.master.id = undefined;
    }
    this.model.master.appUserId = this.frmGroup.value.appUser ? this.frmGroup.value.appUser.id : null;


    /*details*/
    const detailsList: UserRoleAssignDetails[] = [];
    this.rows.getRawValue().forEach(e => {
      e.userRoleId = e.userRole ? e.userRole.id : null;
      detailsList.push(e);
    });

    this.model.detailsList = detailsList;
  }

  /*master details*/
  updateView(): any {
    this.dataSourceDetails.next(this.rows.controls);
  }


// data validation
  isDataUsed(modelList: any, id: any,  appUserId: number, isCreate: boolean): boolean{
    for (const obj of modelList){
      // check for update
      if (!isCreate && id === obj.master.appUserId){
        continue;
      }
      console.log(appUserId  + '=' + obj.master.appUserId);
      if (appUserId === obj.master.appUserId){
        return true;
      }
    }
    return false;
  }

}
