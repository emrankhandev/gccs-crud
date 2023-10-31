import {Component, OnInit} from '@angular/core';
import {ReportUploadService} from "../../../service/report-upload.service";
import {ReportUpload} from "../../../model/report-upload";
import {MatTableDataSource} from "@angular/material/table";
import {PageEvent} from "@angular/material/paginator";
import {MatDialog} from "@angular/material/dialog";
import {SubReportMasterAddDialogComponent} from "../dialog/sub-report-master-add-dialog/sub-report-master-add-dialog.component";
import {locale as lngEnglish} from '../i18n/en';
import {locale as lngBangla} from '../i18n/bn';
import {TranslationLoaderService} from "../../../../../core/service/translation-loader.service";
import {AppConstants} from "../../../../../core/constants/app.constants";
import {AppUtils} from "../../../../../core/utils/app.utils";
import {SubReportMaster} from "../../../model/sub-report-master";
import {SubReportMasterService} from "../../../service/sub-report-master.service";
import {PageableData} from "../../../../../core/model/pageable-data";


/**
 * @Author		Emran Khan
 * @Since		  October 01, 2022
 * @version		1.0.0
 */

@Component({
  selector: 'app-sub-report-master-list',
  templateUrl: './sub-report-master-list.component.html',
  styleUrls: ['./sub-report-master-list.component.scss']
})
export class SubReportMasterListComponent implements OnInit{

  /*property*/
  pagePermission: any;


  /*dataTable*/
  paginationArray: number[] = this.appConstants.DEFAULT_ARRAY;
  size: number = this.appConstants.DEFAULT_SIZE;
  page: number = this.appConstants.DEFAULT_PAGE;
  total: number = 0;
  searchValue: string = '';
  dataSource = new MatTableDataSource(new Array<SubReportMaster>());
  displayedColumns: string[] = ['slNo', 'menuItem', 'reportUpload', 'action'];

  /*object*/
  model: SubReportMaster = new SubReportMaster();

  constructor(
    private matDialog: MatDialog,
    private translationLoaderService: TranslationLoaderService,
    private modelService: SubReportMasterService,
    private appConstants : AppConstants,
    private appUtils : AppUtils,
  ) {
    this.translationLoaderService.loadTranslations(lngEnglish, lngBangla);
  }

  // -----------------------------------------------------------------------------------------------------
  // @ Init
  // -----------------------------------------------------------------------------------------------------

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

  delete(obj: SubReportMaster): void {
    this.modelService.delete(obj).subscribe({
      next: (res) => {
        console.log(res);
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

  openDeleteDialog(row: SubReportMaster): void{
    this.appUtils.openDeleteDialog(row, this.delete.bind(this));
  }

  onChangePage(event: PageEvent): any {
    this.size = +event.pageSize; // get the pageSize
    this.page = +event.pageIndex; // get the current page
    this.getPageableModelList();
  }

  openAddDialog(isEdit: boolean, model?: SubReportMaster): void{
    model = model ? model : new SubReportMaster();
    const dialogConfig = this.appUtils.getDialogConfig();
    dialogConfig.data = {
      model: model,
      isEdit: isEdit
    };
    const dialogRef = this.matDialog.open(SubReportMasterAddDialogComponent, dialogConfig);
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
