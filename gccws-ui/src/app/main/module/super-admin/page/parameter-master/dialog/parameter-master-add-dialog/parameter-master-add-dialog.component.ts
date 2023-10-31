import {Component, EventEmitter, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {AppUtils} from "../../../../../../core/utils/app.utils";
import {ParameterMaster} from "../../../../model/parameter-master";
import {ParameterMasterService} from "../../../../service/parameter-master.service";
import {ReportDataType, ReportDataTypeService} from "../../../../../../core/mock-data/report-data-type.service";
import {AppConstants} from "../../../../../../core/constants/app.constants";

/**
 * @Author    Emran Khan
 * @Since     October 02, 2022
 * @version   1.0.0
 */

@Component({
    selector: 'app-parameter-master-add-dialog',
    templateUrl: './parameter-master-add-dialog.component.html',
    styleUrls: ['./parameter-master-add-dialog.component.scss']
})
export class ParameterMasterAddDialogComponent implements OnInit {

  /*property*/
  callBackMethod: EventEmitter<boolean> = new EventEmitter<boolean>();
  isEdit: boolean;
  isLoading: boolean = false;
  isListDataType: boolean = false;
  /*form*/
  frmGroup: FormGroup;
  model: ParameterMaster = new ParameterMaster();

  /*dropdownList*/
  reportDataTypeDropdownList: ReportDataType[] = new Array<ReportDataType>();
  childDropdownList: ParameterMaster[] = new Array<ParameterMaster>();

  /*extra*/


  // -----------------------------------------------------------------------------------------------------
  // @ Init
  // -----------------------------------------------------------------------------------------------------
  constructor(
    public dialogRef: MatDialogRef<ParameterMasterAddDialogComponent>,
    @Inject(MAT_DIALOG_DATA) data: any,
     private formBuilder: FormBuilder,
     private modelService: ParameterMasterService,
     private reportDataTypeService: ReportDataTypeService,
    private appConstants: AppConstants,
     private appUtils: AppUtils,
  ) {
    this.model = data.model;
    this.isEdit = data.isEdit;
  }

  ngOnInit(): void {
    this.setFormInitValue();
    this.getReportDataTypeList();
    this.getChildList();
    if (this.isEdit){
      this.edit();
    }
  }

  // -----------------------------------------------------------------------------------------------------
  // @ API calling
  // -----------------------------------------------------------------------------------------------------

  getReportDataTypeList(): any{
    this.reportDataTypeService.getList().forEach(e => {
      this.reportDataTypeDropdownList.push(e);
    });
  }

  getChildList(): any {
    this.modelService.getDropdownList().subscribe(res => {
      this.childDropdownList = res.data;
      if (this.isEdit){
        const childValue = this.model.childId ? this.childDropdownList.find(model => model.id === this.model.childId) : '';
        this.frmGroup.patchValue({
          child: childValue ? childValue : '',
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
       title: ['', Validators.required],
       name: ['', Validators.required],
       banglaName: ['', Validators.required],
       dataType: ['', Validators.required],
       sql: ['', ''],
       child: ['', ''],
       childRelationSql: ['', ''],
       active: [true]
     });
  }

  edit(): void {
    if (this.model.dataType === 'List') {
      this.isListDataType = true;
    } else {
      this.isListDataType = false;
    }
    this.frmGroup.patchValue({
      title: this.model.title,
      name: this.model.name,
      banglaName: this.model.banglaName,
      dataType: this.reportDataTypeDropdownList.find(model => model.name === this.model.dataType),
      sql: this.model.sql,
      childRelationSql: this.model.childRelationSql,
      active: this.model.active,
    });

  }

  generateModel(isCreate: boolean): any {
       if (isCreate){this.model.id = undefined; }
       this.model.title = this.frmGroup.value.title;
       this.model.name = this.frmGroup.value.name;
       this.model.banglaName = this.frmGroup.value.banglaName;
       this.model.dataType = this.frmGroup.value.dataType ? this.frmGroup.value.dataType.name : '';
       if (this.frmGroup.value.dataType.name === 'List') {
         this.model.sql = this.frmGroup.value.sql;
         this.model.childId = this.frmGroup.value.child ? this.frmGroup.value.child.id : null;
         this.model.childRelationSql = this.frmGroup.value.childRelationSql;
       } else {
         this.model.sql = '';
         this.model.childId = undefined;
         this.model.childRelationSql = '';
       }
       this.model.active = this.frmGroup.value.active;
  }

  selectDataTypeChange(): void {
    const sql = this.frmGroup.get('sql');

    const dataType = this.frmGroup.value.dataType.name;
    if (dataType === 'List') {
      // validation
      this.isListDataType = true;
      // @ts-ignore
      sql.setValidators(Validators.required);

    } else {
      this.isListDataType = false;
      // @ts-ignore
      sql.clearValidators();
    }
    // @ts-ignore
    sql.updateValueAndValidity();

  }

  selectChildChange(): void {
    const childRelationSql = this.frmGroup.get('childRelationSql');

    if (this.frmGroup.value.child.id) {
      // @ts-ignore
      childRelationSql.setValidators(Validators.required);
    } else {
      // @ts-ignore
      childRelationSql.clearValidators();
    }
    // @ts-ignore
    childRelationSql.updateValueAndValidity();
  }

}
