import {Component, OnInit} from '@angular/core';
import {MatTableDataSource} from "@angular/material/table";
import {PageEvent} from "@angular/material/paginator";
import {MatDialog} from "@angular/material/dialog";
import {ParameterMasterAddDialogComponent} from "../dialog/parameter-master-add-dialog/parameter-master-add-dialog.component";
import {locale as lngEnglish} from "../i18n/en";
import {locale as lngBangla} from "../i18n/bn";
import {TranslationLoaderService} from "../../../../../core/service/translation-loader.service";
import {AppConstants} from "../../../../../core/constants/app.constants";
import {AppUtils} from "../../../../../core/utils/app.utils";
import {ParameterMaster} from "../../../model/parameter-master";
import {ParameterMasterService} from "../../../service/parameter-master.service";
import {PageableData} from "../../../../../core/model/pageable-data";

/**
 * @Author    Emran Khan
 * @Since     October 02, 2022
 * @version   1.0.0
 */

@Component({
  selector: 'app-parameter-master',
  templateUrl: './parameter-master-list.component.html',
  styleUrls: ['./parameter-master-list.component.scss']
})
export class ParameterMasterListComponent implements OnInit{

  /*property*/
  pagePermission: any;

  /*dataTable*/
  paginationArray: number[] = this.appConstants.DEFAULT_ARRAY;
  size: number = this.appConstants.DEFAULT_SIZE;
  page: number = this.appConstants.DEFAULT_PAGE;
  total: number = 0;
  searchValue: string = '';
  dataSource = new MatTableDataSource(new Array<ParameterMaster>());
  displayedColumns: string[] = ['slNo', 'title', 'name', 'banglaName', 'dataType', 'sql', 'child', 'status', 'action'];

  /*object*/
  model: ParameterMaster = new ParameterMaster();

  // -----------------------------------------------------------------------------------------------------
  // @ Init
  // -----------------------------------------------------------------------------------------------------
  constructor(
    private matDialog: MatDialog,
    private translationLoaderService: TranslationLoaderService,
    private modelService: ParameterMasterService,
    private appConstants : AppConstants,
    private appUtils : AppUtils,
  ) {
    this.translationLoaderService.loadTranslations(lngEnglish, lngBangla);
  }


  ngOnInit(): void {
    this.pagePermission = this.appUtils.findUserPagePermission();
    this.getPageableModelList();
  }

  // -----------------------------------------------------------------------------------------------------
  // @ API calling
  // -----------------------------------------------------------------------------------------------------

  getPageableModelList(): any {
    this.modelService.getListWithPaginationData(new PageableData (this.page, this.size, this.searchValue)).subscribe(res => {
      this.dataSource = new MatTableDataSource(res.data.content);
      this.total = res.data.totalElements;
    });
  }

  delete(obj: ParameterMaster): void {
    this.modelService.delete(obj).subscribe({
      next: (res) => {
        this.appUtils.onServerResponse(res, this.getPageableModelList.bind(this))
      },
      error: (error) => {
        this.appUtils.onServerErrorResponse(error);
      }
    });
  }

  // -----------------------------------------------------------------------------------------------------
  // @ view method
  // -----------------------------------------------------------------------------------------------------

  openDeleteDialog(row: ParameterMaster): void{
    this.appUtils.openDeleteDialog(row, this.delete.bind(this));
  }

  onChangePage(event: PageEvent): any {
    this.size = +event.pageSize; // get the pageSize
    this.page = +event.pageIndex; // get the current page
    this.getPageableModelList();
  }


  openAddDialog(isEdit: boolean, model?: ParameterMaster, ): void{
    model = model ? model : new ParameterMaster();
    const dialogConfig = this.appUtils.getDialogConfig();
    dialogConfig.data = {
      model: model,
      isEdit: isEdit
    };
    const dialogRef = this.matDialog.open(ParameterMasterAddDialogComponent, dialogConfig);
    dialogRef.componentInstance.callBackMethod.subscribe(value => {
      this.getPageableModelList();
    });
  }

  applyFilter(event: Event): void {
    this.searchValue = (event.target as HTMLInputElement).value;
    this.getPageableModelList();
  }

  // -----------------------------------------------------------------------------------------------------
  // @ Utils
  // -----------------------------------------------------------------------------------------------------

}
