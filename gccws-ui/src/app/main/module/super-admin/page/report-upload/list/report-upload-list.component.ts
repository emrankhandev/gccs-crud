import {Component, OnInit} from '@angular/core';
import {ReportUploadService} from "../../../service/report-upload.service";
import {ReportUpload} from "../../../model/report-upload";
import {MatTableDataSource} from "@angular/material/table";
import {PageEvent} from "@angular/material/paginator";
import {MatDialog} from "@angular/material/dialog";
import {ReportUploadAddDialogComponent} from "../dialog/add-dialog/report-upload-add-dialog.component";
import {locale as lngEnglish} from '../i18n/en';
import {locale as lngBangla} from '../i18n/bn';
import {TranslationLoaderService} from "../../../../../core/service/translation-loader.service";
import {AppConstants} from "../../../../../core/constants/app.constants";
import {AppUtils} from "../../../../../core/utils/app.utils";
import {PageableData} from "../../../../../core/model/pageable-data";


/**
 * @Author		Md. Tawhidul  islam
 * @Since		  september 28, 2022
 * @version		1.0.0
 */

@Component({
  selector: 'app-report-upload-list',
  templateUrl: './report-upload-list.component.html',
  styleUrls: ['./report-upload-list.component.scss']
})
export class ReportUploadListComponent implements OnInit{

  /*property*/
  pagePermission: any;


  /*dataTable*/
  paginationArray: number[] = this.appConstants.DEFAULT_ARRAY;
  size: number = this.appConstants.DEFAULT_SIZE;
  page: number = this.appConstants.DEFAULT_PAGE;
  total: number = 0;
  searchValue: string = '';
  dataSource = new MatTableDataSource(new Array<ReportUpload>());
  displayedColumns: string[] = ['slNo', 'code', 'fileName', 'remarks', 'isSubreport', 'action'];

  /*object*/
  model: ReportUpload = new ReportUpload();

  constructor(
    private matDialog: MatDialog,
    private translationLoaderService: TranslationLoaderService,
    private modelService: ReportUploadService,
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
      console.log(res.data)
      this.dataSource = new MatTableDataSource(res.data.content);
      this.total = res.data.totalElements;
    });
  }

  delete(obj: ReportUpload): void {
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

  openDeleteDialog(row: ReportUpload): void{
    this.appUtils.openDeleteDialog(row, this.delete.bind(this));
  }

  onChangePage(event: PageEvent): any {
    this.size = +event.pageSize; // get the pageSize
    this.page = +event.pageIndex; // get the current page
    this.getPageableModelList();
  }

  openAddDialog(isEdit: boolean, model?: ReportUpload, ): void{
    model = model ? model : new ReportUpload();
    const dialogConfig = this.appUtils.getDialogConfig();
    dialogConfig.data = {
      model: model,
      isEdit: isEdit
    };
    const dialogRef = this.matDialog.open(ReportUploadAddDialogComponent, dialogConfig);
    dialogRef.componentInstance.callBackMethod.subscribe(value => {
      this.getPageableModelList();
    });
  }

  applyFilter(event: Event): void {
    this.searchValue = (event.target as HTMLInputElement).value;
    this.getPageableModelList();
  }

  download(obj: ReportUpload): any {
    const filename: any = obj.fileName;
    this.modelService.downloadFile(filename).subscribe((blob: any | Blob) =>{
      let url = window.URL.createObjectURL(blob);
      let a = document.createElement('a');
      document.body.appendChild(a);
      a.setAttribute('style', 'display: none');
      a.href = url;
      a.download = filename;
      a.click();
      window.URL.revokeObjectURL(url);
      a.remove();
    });
  }

  // -----------------------------------------------------------------------------------------------------
  // @ Utils
  // -----------------------------------------------------------------------------------------------------

}
