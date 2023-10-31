import {Component, EventEmitter, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {AppUtils} from "../../../../../../core/utils/app.utils";
import {DropdownModel} from "../../../../../../core/model/dropdown-model";
import {MenuItem} from "../../../../model/menu-item";
import {MenuItemService} from "../../../../service/menu-item.service";
import {MenuType, MenuTypeService} from "../../../../../../core/mock-data/menu-type.service";
import {ReportUploadService} from "../../../../service/report-upload.service";


@Component({
    selector: 'app-menu-item-add-dialog',
    templateUrl: './menu-item-add-dialog.component.html',
    styleUrls: ['./menu-item-add-dialog.component.scss']
})
export class MenuItemAddDialogComponent implements OnInit {

  /*property*/
  callBackMethod: EventEmitter<boolean> = new EventEmitter<boolean>();
  isEdit: boolean;
  isLoading: boolean = false;
  isDevCode: boolean = false;

  /*form*/
  frmGroup: FormGroup;
  model: MenuItem = new MenuItem();

  /*dropdownList*/
  parentDropdownList: DropdownModel[] = new Array<DropdownModel>();
  menuTypeDropdownList: MenuType[] = new Array<MenuType>();
  reportDropdownList: DropdownModel[] = new Array<DropdownModel>();

  /*extra*/
  menuId:number;


  // -----------------------------------------------------------------------------------------------------
  // @ Init
  // -----------------------------------------------------------------------------------------------------
  constructor(
    public dialogRef: MatDialogRef<MenuItemAddDialogComponent>,
    @Inject(MAT_DIALOG_DATA) data: any,
    private formBuilder: FormBuilder,
    private modelService: MenuItemService,
    public menuTypeService: MenuTypeService,
    public reportUploadService: ReportUploadService,
    private appUtils: AppUtils,
  ) {
    this.model = data.model;
    this.isEdit = data.isEdit;
  }

  ngOnInit(): void {
    this.setFormInitValue();
    this.getMenuTypeList();
    this.getReportActiveList();
    if (this.isEdit){
      this.edit();
    }
  }

  // -----------------------------------------------------------------------------------------------------
  // @ API calling
  // -----------------------------------------------------------------------------------------------------

  getParentList(menuId:number): any {
    this.modelService.getDropdownList().subscribe(res => {
      this.parentDropdownList = res.data;
      // report
      if(menuId === this.menuTypeService.REPORT_ID){
        this.parentDropdownList = this.parentDropdownList.filter(e => e.extra ==  this.menuTypeService.MODULE_ID);
      }
      // menu
      if(menuId === this.menuTypeService.MENU_ID){
        this.parentDropdownList = this.parentDropdownList.filter(e => e.extra == this.menuTypeService.MODULE_ID) ;
      }

      if(menuId == this.menuTypeService.MODULE_ID){
        this.parentDropdownList = [];
      }

      if (this.isEdit) {
        const parentValue = this.model.parentId ? this.parentDropdownList.find(model => model.id === this.model.parentId) : '';
        this.frmGroup.patchValue({
          parent: parentValue ? parentValue : '',
        });
      }
    });
  }

  getReportActiveList(): any {
    this.reportUploadService.getMasterReportDropdownList().subscribe(res => {
      this.reportDropdownList = res.data;

      if (this.isEdit) {
        const reportValue = this.model.reportUploadId ? this.reportDropdownList.find(model => model.id === this.model.reportUploadId) : '';
        this.frmGroup.patchValue({
          report: reportValue ? reportValue : '',
        });
      }
    });
  }

  getMenuTypeList(): any {
    this.menuTypeDropdownList = this.menuTypeService.getList();
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
  getMenuTypeById():void{
    this.menuId = this.frmGroup.value.menuType.id;
    this.getParentList(this.menuId);
    this.selectDevCode();
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

  getMenuTypeField(): any{
    return this.frmGroup.get('menuType')?.value;
  }

  // -----------------------------------------------------------------------------------------------------
  // @ Helper Method
  // -----------------------------------------------------------------------------------------------------

  setFormInitValue(): any {
    this.frmGroup = this.formBuilder.group({
      parent:['', ''],
      report:['', ''],
      name: ['', Validators.required],
      banglaName: ['', Validators.required],
      menuType: ['', Validators.required],
      serialNo: ['', Validators.required],
      devCode: ['', ''],
      url: ['', ''],
      icon: ['', ''],
      insert: ['', ''],
      edit: ['', ''],
      delete: ['', ''],
      approve: ['', ''],
      view: [true],
      active: [true]
    });
  }

  edit(): void {
    const menuTypeValue = this.model.menuType ? this.menuTypeDropdownList.find(model => model.id === this.model.menuType) : '';
    this.frmGroup.patchValue({
      name: this.model.name,
      banglaName: this.model.banglaName,
      menuType: menuTypeValue ? menuTypeValue : '',
      serialNo: this.model.serialNo,
      devCode: this.model.devCode,
      url: this.model.url,
      icon: this.model.icon,
      insert: this.model.insert,
      edit: this.model.edit,
      delete: this.model.delete,
      approve: this.model.approve,
      view: this.model.view,
    });
    this.getMenuTypeById();
  }

  generateModel(isCreate: boolean): any{
    if (isCreate){this.model.id = undefined; }
    this.model.name = this.frmGroup.value.name;
    this.model.banglaName = this.frmGroup.value.banglaName;
    this.model.menuType = this.frmGroup.value.menuType? this.frmGroup.value.menuType.id : null;
    this.model.menuTypeName = this.frmGroup.value.menuType ? this.frmGroup.value.menuType.name : null;
    this.model.serialNo = this.frmGroup.value.serialNo;
    this.model.url = this.frmGroup.value.url;
    this.model.devCode = this.frmGroup.value.devCode;
    this.model.parentId = this.frmGroup.value.parent ? this.frmGroup.value.parent.id : null;
    this.model.reportUploadId = this.frmGroup.value.report ? this.frmGroup.value.report.id : null;
    this.model.icon = this.frmGroup.value.icon;
    this.model.insert = this.frmGroup.value.insert;
    this.model.edit = this.frmGroup.value.edit;
    this.model.delete = this.frmGroup.value.delete;
    this.model.approve = this.frmGroup.value.approve;
    this.model.view = this.frmGroup.value.view;
    this.model.active = this.frmGroup.value.active;
  }


  selectDevCode(): void {
    const devCode: any = this.frmGroup.get('devCode');
    if (this.menuId == this.menuTypeService.REPORT_ID) {
      devCode.setValidators(Validators.required);
    } else {
      devCode.clearValidators();
    }
    devCode.updateValueAndValidity();
  }

}
