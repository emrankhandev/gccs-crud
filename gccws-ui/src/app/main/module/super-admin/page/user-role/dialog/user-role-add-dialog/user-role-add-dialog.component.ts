import {Component, EventEmitter, Inject, OnInit} from '@angular/core';
import {AbstractControl, FormArray, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {locale as lngEnglish} from "../../i18n/en";
import {locale as lngBangla} from "../../i18n/bn";
import {BehaviorSubject} from "rxjs";
import {UserRoleModel} from "../../../../model/user-role-model";
import {UserRoleService} from "../../../../service/user-role.service";
import {UserRoleDetails} from "../../../../model/user-role-details";
import {UserRoleMaster} from "../../../../model/user-role-master";
import {TranslationLoaderService} from "../../../../../../core/service/translation-loader.service";
import {MenuItemService} from "../../../../service/menu-item.service";
import {AppUtils} from "../../../../../../core/utils/app.utils";
import {MenuItem} from "../../../../model/menu-item";
import {MenuTypeService} from "../../../../../../core/mock-data/menu-type.service";


@Component({
    selector: 'app-user-role-master-add-dialog',
    templateUrl: './user-role-add-dialog.component.html',
    styleUrls: ['./user-role-add-dialog.component.scss']
})
export class UserRoleAddDialogComponent implements OnInit {

  /*property*/
  callBackMethod: EventEmitter<boolean> = new EventEmitter<boolean>();
  isEdit: boolean;
  isLoading: boolean = false;

  /*form*/
  frmGroup: FormGroup;
  model: UserRoleModel = new UserRoleModel();

  /*master details*/
  displayColumnsDetails = ['menuItem', 'insert', 'edit', 'delete', 'view', 'approve', 'action'];
  dataSourceDetails = new BehaviorSubject<AbstractControl[]>([]);
  rows: FormArray = this.formBuilder.array([]);
  frmGroupDetails: FormGroup = this.formBuilder.group({
    scope: this.rows
  });

  /*dropdownList*/
  menuItemDropdownList: MenuItem[] = new Array<MenuItem>();

  /*extra*/

  // -----------------------------------------------------------------------------------------------------
  // @ Init
  // -----------------------------------------------------------------------------------------------------
  constructor(
    public dialogRef: MatDialogRef<UserRoleAddDialogComponent>,
    @Inject(MAT_DIALOG_DATA) data: any,
    private translationLoaderService: TranslationLoaderService,
    private formBuilder: FormBuilder,
    private modelService: UserRoleService,
    private menuItemService: MenuItemService,
    private menuTypeService: MenuTypeService,
    private appUtils: AppUtils,
  ) {
    this.translationLoaderService.loadTranslations(lngEnglish, lngBangla);
    this.model = data.model;
    this.isEdit = data.isEdit;
  }

  ngOnInit(): void {
    this.setFormInitValue();
    this.addRow();
    this.getMenuItemActiveList();
    if (this.isEdit){
      this.edit();
    }
  }

  // -----------------------------------------------------------------------------------------------------
  // @ API calling
  // -----------------------------------------------------------------------------------------------------
  getMenuItemActiveList(): any {
    const menuType: string = this.menuTypeService.MENU_ID + ',' +this.menuTypeService.REPORT_ID;
    this.menuItemService.getByMenuType(menuType ).subscribe(res => {
      this.menuItemDropdownList = res.data;
      if (this.isEdit){
        this.model.detailsList.forEach((value, index) => {
          const menuItemValue = value.menuItemId ? this.menuItemDropdownList.find(model => model.id === value.menuItemId) : '';
          this.rows.at(index).patchValue({
            menuItem: menuItemValue ? menuItemValue : '',
          });
          this.getMenuItemById(this.rows.at(index));
        });
      }
    });
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
    console.log(this.model);
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

  getMenuItemById(row: any):void{
    const selectValue = row.value.menuItem;
    if(selectValue.id){
      this.menuItemService.getObjectById(selectValue.id).subscribe(res => {
        row.patchValue({
          menuItemFullObj: res.data
        });
      });
    }else {
      row.patchValue({
        menuItemFullObj: selectValue
      });
    }

    this.checkMenuItem(row);
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
  addRow(value?: UserRoleDetails, index?: number): any {
    const row = this.formBuilder.group({
      id: [value ? value.id : ''],
      menuItemFullObj: ['', ''],
      menuItem: ['', Validators.required],
      insert: [value ? value.insert :false, ''],
      edit: [value ? value.edit :false, ''],
      delete: [value ? value.delete :false, ''],
      approve: [value ? value.approve :false, ''],
      view: [value ? value.view :true, ''],
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

  checkMenuItem(row: any): void{
    const selectValue = row.value.menuItem;
    let count = 0;
    this.rows.getRawValue().forEach(e => {
      if (e.menuItem.id === selectValue.id){ count ++; }
    });
    if (count > 1){
      this.appUtils.itemAlreadyTakenMsg();
      row.patchValue({ menuItem: '' });
    }
  }

  // -----------------------------------------------------------------------------------------------------
  // @ Helper Method
  // -----------------------------------------------------------------------------------------------------

  setFormInitValue(): any {
    this.frmGroup = this.formBuilder.group({
      name: ['', Validators.required],
      banglaName: ['', ''],
      active: [true, ''],
    });
  }

  edit(): void {
    /*master*/
    this.frmGroup.patchValue({
      name: this.model.master.name,
      banglaName: this.model.master.banglaName,
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
      this.model.master = new UserRoleMaster();
      this.model.master.id = undefined;
    }
    this.model.master.name = this.frmGroup.value.name;
    this.model.master.banglaName = this.frmGroup.value.banglaName;
    this.model.master.active = this.frmGroup.value.active;

    /*details*/
    const detailsList: UserRoleDetails[] = [];
    this.rows.getRawValue().forEach(e => {
      e.menuItemId = e.menuItem ? e.menuItem.id : null;
      detailsList.push(e);
    });
    this.model.detailsList = detailsList;
  }

  /*master details*/
  updateView(): any {
    this.dataSourceDetails.next(this.rows.controls);
  }
}
