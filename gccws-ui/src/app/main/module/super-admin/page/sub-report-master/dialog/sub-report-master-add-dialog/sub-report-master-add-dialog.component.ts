import {Component, EventEmitter, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {ReportUpload} from "../../../../model/report-upload";
import {ReportUploadService} from "../../../../service/report-upload.service";
import {AppConstants} from "../../../../../../core/constants/app.constants";
import {AppUtils} from "../../../../../../core/utils/app.utils";
import {HttpClient} from "@angular/common/http";
import {SubReportMaster} from "../../../../model/sub-report-master";
import {DropdownModel} from "../../../../../../core/model/dropdown-model";
import {SubReportMasterService} from "../../../../service/sub-report-master.service";
import {MenuItemService} from "../../../../service/menu-item.service";
import {MenuItem} from "../../../../model/menu-item";
import {MenuTypeService} from "../../../../../../core/mock-data/menu-type.service";

/**
 * @Author		Emran Khan
 * @Since		  October 01, 2022
 * @version		1.0.0
 */

@Component({
  selector: 'app-sub-report-master-add-dialog',
  templateUrl: './sub-report-master-add-dialog.component.html',
  styleUrls: ['./sub-report-master-add-dialog.component.scss']
})

export class SubReportMasterAddDialogComponent implements OnInit {

  /*property*/
  callBackMethod: EventEmitter<boolean> = new EventEmitter<boolean>();
  isEdit: boolean;
  isLoading: boolean = false;

  /*form*/
  frmGroup: FormGroup;
  model: SubReportMaster = new SubReportMaster();
  menuItemModel: MenuItem = new MenuItem();
  reportUploadModel: ReportUpload = new ReportUpload();

  /*file*/

  /*dropdownList*/
  menuItemDropDownList: MenuItem[] = new Array<MenuItem>();
  reportUploadDropdownList: DropdownModel[] = new Array<DropdownModel>();

  // -----------------------------------------------------------------------------------------------------
  // @ Init
  // -----------------------------------------------------------------------------------------------------
  constructor(
    public dialogRef: MatDialogRef<SubReportMasterAddDialogComponent>,
    @Inject(MAT_DIALOG_DATA) data: any,
    private formBuilder: FormBuilder,
    private modelService: SubReportMasterService,
    private menuItemService: MenuItemService,
    private menuTypeService: MenuTypeService,
    private reportUploadService: ReportUploadService,
    private appConstants: AppConstants,
    private appUtils: AppUtils,
  ) {
    this.model = data.model;
    this.isEdit = data.isEdit;
  }

  ngOnInit(): void {
    this.setFormInitValue();
    this.getMenuItemList();
    this.getReportUploadList();
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
        const menuItemValue = this.model.menuItemId ? this.menuItemDropDownList.find(model => model.id === this.model.menuItemId) : '';
        this.frmGroup.patchValue({
          menuItem: menuItemValue ? menuItemValue : '',
        });
      }
    });
  }

  getReportUploadList(): any {
    this.reportUploadService.getSubreportDropdownList().subscribe(res => {
      this.reportUploadDropdownList = res.data;

      if (this.isEdit) {
        const reportUploadValue = this.model.reportUploadId ? this.reportUploadDropdownList.find(model => model.id === this.model.reportUploadId) : '';
        this.frmGroup.patchValue({
          reportUpload: reportUploadValue ? reportUploadValue : '',
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
  }

  // -----------------------------------------------------------------------------------------------------
  // @ Helper Method
  // -----------------------------------------------------------------------------------------------------

  setFormInitValue(): any {
    this.frmGroup = this.formBuilder.group({
      menuItem: ['', Validators.required],
      reportUpload: ['', Validators.required],
      active: [true],
    });
  }

  edit(): void {
    const menuItemValue = this.menuItemModel.id ? this.menuItemDropDownList.find(model => model.id === this.model.menuItemId) : '';
    const reportUploadValue = this.reportUploadModel.id ? this.reportUploadDropdownList.find(model => model.id === this.model.reportUploadId) : '';
    this.frmGroup.patchValue({
      menuItem: menuItemValue ? menuItemValue : '',
      reportUpload: reportUploadValue ? reportUploadValue : '',
      active: this.model.active,
    });
  }

  generateModel(isCreate: boolean): any{
    if (isCreate){this.model.id = undefined; }
    this.model.menuItemId = this.frmGroup.value.menuItem ? this.frmGroup.value.menuItem.id : null;
    this.model.reportUploadId = this.frmGroup.value.reportUpload ? this.frmGroup.value.reportUpload.id : null;
    this.model.active = true;
  }

}
