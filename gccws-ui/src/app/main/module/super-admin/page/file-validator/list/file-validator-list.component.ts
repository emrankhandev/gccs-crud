import {Component, OnInit} from '@angular/core';
import {MatTableDataSource} from "@angular/material/table";
import {PageEvent} from "@angular/material/paginator";
import {MatDialog} from "@angular/material/dialog";
import {FileValidatorAddDialogComponent} from "../dialog/file-validator-add-dialog/file-validator-add-dialog.component";
import {locale as lngEnglish} from "../i18n/en";
import {locale as lngBangla} from "../i18n/bn";
import {TranslationLoaderService} from "../../../../../core/service/translation-loader.service";
import {AppConstants} from "../../../../../core/constants/app.constants";
import {AppUtils} from "../../../../../core/utils/app.utils";
import {FileValidator} from "../../../model/file-validator";
import {FileValidatorService} from "../../../service/file-validator.service";
import {PageableData} from "../../../../../core/model/pageable-data";


@Component({
  selector: 'app-file-validator',
  templateUrl: './file-validator-list.component.html',
  styleUrls: ['./file-validator-list.component.scss']
})
export class FileValidatorListComponent implements OnInit{

  /*property*/
  pagePermission: any;

  /*dataTable*/
  paginationArray: number[] = this.appConstants.DEFAULT_ARRAY;
  size: number = this.appConstants.DEFAULT_SIZE;
  page: number = this.appConstants.DEFAULT_PAGE;
  total: number = 0;
  searchValue: string = '';
  dataSource = new MatTableDataSource(new Array<FileValidator>());
  displayedColumns: string[] = ['slNo', 'name', 'fileExtensions', 'fileSize', 'fileHeight', 'fileWidth', 'action'];

  /*object*/
  model: FileValidator = new FileValidator();

  // -----------------------------------------------------------------------------------------------------
  // @ Init
  // -----------------------------------------------------------------------------------------------------
  constructor(
    private matDialog: MatDialog,
    private translationLoaderService: TranslationLoaderService,
    private modelService: FileValidatorService,
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

  delete(obj: FileValidator): void {
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

  openDeleteDialog(row: FileValidator): void{
    this.appUtils.openDeleteDialog(row, this.delete.bind(this));
  }

  onChangePage(event: PageEvent): any {
    this.size = +event.pageSize; // get the pageSize
    this.page = +event.pageIndex; // get the current page
    this.getPageableModelList();
  }



  openAddDialog(isEdit: boolean, model?: FileValidator): void{
     model = model ? model : new FileValidator();
     const dialogConfig = this.appUtils.getDialogConfig();
     dialogConfig.data = {
       model: model,
       isEdit: isEdit
     };
     const dialogRef = this.matDialog.open(FileValidatorAddDialogComponent, dialogConfig);
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
