import {Component, EventEmitter, Inject, OnInit} from '@angular/core';
import {AbstractControl, FormArray, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {BehaviorSubject} from "rxjs";
import {DropdownModel} from "../../../../../../core/model/dropdown-model";
import {AppUtils} from "../../../../../../core/utils/app.utils";
import {ParameterAssignModel} from "../../../../model/parameter-assign-model";
import {ParameterAssignService} from "../../../../service/parameter-assign.service";
import {ParameterAssignDetails} from "../../../../model/parameter-assign-details";
import {ParameterAssignMaster} from "../../../../model/parameter-assign-master";
import {MenuItemService} from "../../../../service/menu-item.service";
import {ParameterMasterService} from "../../../../service/parameter-master.service";
import {MenuTypeService} from "../../../../../../core/mock-data/menu-type.service";
import {MenuItem} from "../../../../model/menu-item";


@Component({
    selector: 'app-parameter-assign-master-add-dialog',
    templateUrl: './parameter-assign-add-dialog.component.html',
    styleUrls: ['./parameter-assign-add-dialog.component.scss']
})
export class ParameterAssignAddDialogComponent implements OnInit {

  /*property*/
  callBackMethod: EventEmitter<boolean> = new EventEmitter<boolean>();
  isEdit: boolean;
  isLoading: boolean = false;

  /*form*/
  frmGroup: FormGroup;
  model: ParameterAssignModel = new ParameterAssignModel();


  /*master details*/
  displayColumnsDetails = ['parameterMaster', 'isDependent', 'serialNo', 'isRequired', 'action'];
  dataSourceDetails = new BehaviorSubject<AbstractControl[]>([]);
  rows: FormArray = this.formBuilder.array([]);
  frmGroupDetails: FormGroup = this.formBuilder.group({
    scope: this.rows
  });

  /*dropdownList*/
  menuItemDropDownList: MenuItem[] = new Array<MenuItem>();
  parameterMasterDropdownList: DropdownModel[] = new Array<DropdownModel>();

  /*extra*/

  // -----------------------------------------------------------------------------------------------------
  // @ Init
  // -----------------------------------------------------------------------------------------------------
  constructor(
    public dialogRef: MatDialogRef<ParameterAssignAddDialogComponent>,
    @Inject(MAT_DIALOG_DATA) data: any,
    private formBuilder: FormBuilder,
    private modelService: ParameterAssignService,
    private menuItemService: MenuItemService,
    private menuTypeService: MenuTypeService,
    private parameterMasterService: ParameterMasterService,
    private appUtils: AppUtils,
  ) {
    this.model = data.model;
    this.isEdit = data.isEdit;
  }

  ngOnInit(): void {
    this.setFormInitValue();
    this.addRow();
    this.getMenuItemList();
    this.getParameterMasterList();
    if (this.isEdit){
      this.edit();
    }
  }

  // -----------------------------------------------------------------------------------------------------
  // @ API calling
  // -----------------------------------------------------------------------------------------------------
  getMenuItemList(): any {
    const menuType: any = this.menuTypeService.REPORT_ID;
    this.menuItemService.getByMenuType(menuType).subscribe(res => {
      this.menuItemDropDownList = res.data;
      if (this.isEdit){
        const menuItemValue = this.model.master.menuItemId ? this.menuItemDropDownList.find(model => model.id === this.model.master.menuItemId) : '';
        this.frmGroup.patchValue({
          menuItem: menuItemValue ? menuItemValue : '',
        });
      }
    });
  }

  getParameterMasterList(): any {
    this.parameterMasterService.getDropdownList().subscribe(res => {
      this.parameterMasterDropdownList = res.data;
      if (this.isEdit){
        //details
        this.model.detailsList.forEach((value, index) => {
          const parameterMasterValue = value.parameterMasterId ? this.parameterMasterDropdownList.find(model => model.id === value.parameterMasterId) : '';
          this.rows.at(index).patchValue({
            parameterMaster: parameterMasterValue ? parameterMasterValue : '',
          });
        });
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
  addRow(value?: ParameterAssignDetails, index?: number): any {
    const row = this.formBuilder.group({
      id: [value ? value.id : ''],
      parameterMaster: ['', Validators.required],
      isRequired: [value?.isRequired ? value?.isRequired : false],
      serialNo: [value?.serialNo ? value?.serialNo : '', Validators.required],
      isDependent: [value?.isDependent ? value?.isDependent : false],
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
    const selectValue = row.value.parameterMaster;
    let count = 0;
    this.rows.getRawValue().forEach(e => {
      if (e.parameterMaster.id === selectValue.id){ count ++; }
    });
    if (count > 1){
      this.appUtils.itemAlreadyTakenMsg();
      row.patchValue({ parameterMaster: '' });
    }
  }


  // -----------------------------------------------------------------------------------------------------
  // @ Helper Method
  // -----------------------------------------------------------------------------------------------------

  setFormInitValue(): any {
    this.frmGroup = this.formBuilder.group({
      menuItem: ['', Validators.required],
    });
  }

  edit(): void {
    /*details*/
    this.rows.clear();
    this.model.detailsList.forEach((value, index) => {
      this.addRow(value, index);
    });
  }

  generateModel(isCreate: boolean): any{
    /*master*/
    if (isCreate) {
      this.model.master = new ParameterAssignMaster();
      this.model.master.id = undefined;
    }
    this.model.master.menuItemId = this.frmGroup.value.menuItem ? this.frmGroup.value.menuItem.id : null;


    /*details*/
    const detailsList: ParameterAssignDetails[] = [];
    this.rows.getRawValue().forEach(e => {
      e.parameterMasterId = e.parameterMaster ? e.parameterMaster.id : null;
      detailsList.push(e);
    });

    this.model.detailsList = detailsList;
  }

  /*master details*/
  updateView(): any {
    this.dataSourceDetails.next(this.rows.controls);
  }

}
