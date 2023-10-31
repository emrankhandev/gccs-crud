import {Component, EventEmitter, Inject, OnInit} from '@angular/core';
import {AbstractControl, FormArray, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {BehaviorSubject} from "rxjs";
import {UserRoleService} from "../../../../service/user-role.service";
import {DropdownModel} from "../../../../../../core/model/dropdown-model";
import {AppUtils} from "../../../../../../core/utils/app.utils";
import {UserRoleAssignDetails} from "../../../../model/user-role-assign-details";
import {UserRoleAssignMaster} from "../../../../model/user-role-assign-master";
import {AppUserService} from "../../../../service/app-user.service";
import {NotificationModel} from "../../../../model/notification-model";
import {NotificationService} from "../../../../service/notification.service";
import {NotificationType, NotificationTypeService} from "../../../../../../core/mock-data/notification-type.service";
import {NotificationDetails} from "../../../../model/notification-details";
import {NotificationMaster} from "../../../../model/notification-master";
import {DATA_ALREADY_USED, DATA_ALREADY_USED_BN} from "../../../../../../core/constants/message";


@Component({
    selector: 'user-notification-add-dialog',
    templateUrl: './notification-add-dialog.component.html',
    styleUrls: ['./notification-add-dialog.component.scss']
})
export class NotificationAddDialogComponent implements OnInit {

  /*property*/
  callBackMethod: EventEmitter<boolean> = new EventEmitter<boolean>();
  isEdit: boolean;
  isLoading: boolean = false;

  /*form*/
  frmGroup: FormGroup;
  model: NotificationModel = new NotificationModel();
  modelList: NotificationModel[] = new Array<NotificationModel>();


  /*master details*/
  displayColumnsDetails = ['appUser',  'action'];
  dataSourceDetails = new BehaviorSubject<AbstractControl[]>([]);
  rows: FormArray = this.formBuilder.array([]);
  frmGroupDetails: FormGroup = this.formBuilder.group({
    scope: this.rows
  });

  /*dropdownList*/
  notificationTypeDropdownList: NotificationType[] = new Array<NotificationType>();
  appUserDropdownList: DropdownModel[] = new Array<DropdownModel>();

  /*extra*/

  // -----------------------------------------------------------------------------------------------------
  // @ Init
  // -----------------------------------------------------------------------------------------------------
  constructor(
    public dialogRef: MatDialogRef<NotificationAddDialogComponent>,
    @Inject(MAT_DIALOG_DATA) data: any,
    private formBuilder: FormBuilder,
    private modelService: NotificationService,
    private appUserService: AppUserService,
    private userRoleService: UserRoleService,
    private notificationTypeService: NotificationTypeService,
    private appUtils: AppUtils,
  ) {
    this.model = data.model;
    this.isEdit = data.isEdit;
  }

  ngOnInit(): void {
    this.setFormInitValue();
    this.addRow();
    this.getNotificationTypeList();
    this.getAppUserList();
    if (this.isEdit){
      this.edit();
    }
  }

  // -----------------------------------------------------------------------------------------------------
  // @ API calling
  // -----------------------------------------------------------------------------------------------------

  getNotificationTypeList(): any {
    this.notificationTypeDropdownList = this.notificationTypeService.getList();
    if (this.isEdit) {
      const objValue = this.model.master.notificationTypeId ? this.notificationTypeDropdownList.find(model => model.id === this.model.master.notificationTypeId) : '';
      this.frmGroup.patchValue({
        notificationType: objValue ? objValue : '',
      });
    }
  }

  getAppUserList(): any {
    this.appUserService.getDropdownList().subscribe(res => {
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
    if(this.isDataUsed(this.modelList, null, this.frmGroup.value.notificationType.id, true)){
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
    if(this.isDataUsed(this.modelList, this.model.master.notificationTypeId, this.frmGroup.value.notificationType.id, false)){
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
  addRow(value?: NotificationDetails, index?: number): any {
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

  checkAppUser(row: any): void{
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
      notificationType: ['', Validators.required],
      active: [true]
    });
  }

  edit(): void {
    /*master*/
    this.frmGroup.patchValue({
      //appUserName: this.model.master.appUserName,

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
      this.model.master = new NotificationMaster();
      this.model.master.id = undefined;
    }
    this.model.master.notificationTypeId = this.frmGroup.value.notificationType ? this.frmGroup.value.notificationType.id : null;
    this.model.master.notificationTypeName = this.frmGroup.value.notificationType ? this.frmGroup.value.notificationType.name : null;


    /*details*/
    const detailsList: NotificationDetails[] = [];
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


// data validation
  isDataUsed(modelList: any, id: any, notificationTypeId: number, isCreate: boolean): boolean{
    for (const obj of modelList){
      // check for update
      if (!isCreate && id === obj.master.notificationTypeId){
        continue;
      }
      console.log(notificationTypeId  + '=' + obj.master.notificationTypeId);
      if (notificationTypeId === obj.master.notificationTypeId){
        return true;
      }
    }
    return false;
  }

}
