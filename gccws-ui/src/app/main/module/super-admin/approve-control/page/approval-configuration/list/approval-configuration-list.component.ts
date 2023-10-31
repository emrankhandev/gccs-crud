import {Component, OnInit} from '@angular/core';
import {MatTableDataSource} from "@angular/material/table";
import {PageEvent} from "@angular/material/paginator";
import {MatDialog} from "@angular/material/dialog";
import {ApprovalConfigurationAddDialogComponent} from "../dialog/approval-configuration-add-dialog/approval-configuration-add-dialog.component";
import {locale as lngEnglish} from "../i18n/en";
import {locale as lngBangla} from "../i18n/bn";
import {TranslationLoaderService} from "../../../../../../core/service/translation-loader.service";
import {AppConstants} from "../../../../../../core/constants/app.constants";
import {AppUtils} from "../../../../../../core/utils/app.utils";
import {ApprovalConfiguration} from "../../../model/approval-configuration";
import {ApprovalConfigurationService} from "../../../service/approval-configuration.service";
import {PageableData} from "../../../../../../core/model/pageable-data";

@Component({
  selector: 'app-approval-configuration-list',
  templateUrl: './approval-configuration-list.component.html',
  styleUrls: ['./approval-configuration-list.component.scss']
})
export class ApprovalConfigurationListComponent implements OnInit{

  /*property*/
  pagePermission: any;

  /*dataTable*/
  paginationArray: number[] = this.appConstants.DEFAULT_ARRAY;
  size: number = this.appConstants.DEFAULT_SIZE;
  page: number = this.appConstants.DEFAULT_PAGE;
  total: number = 0;
  searchValue: string = '';
  dataSource = new MatTableDataSource(new Array<ApprovalConfiguration>());
  displayedColumns: string[] = [ 'slNo','department', 'transactionType','fromTeam', 'toTeam', 'notifyAppUser',  'serialNo',
    'action'];

  /*object*/
  model: ApprovalConfiguration = new ApprovalConfiguration();

  constructor(
    private matDialog: MatDialog,
    private translationLoaderService: TranslationLoaderService,
    private modelService: ApprovalConfigurationService,
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

  delete(obj: ApprovalConfiguration): void {
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

  openDeleteDialog(row: ApprovalConfiguration): void{
    this.appUtils.openDeleteDialog(row, this.delete.bind(this));
  }

  onChangePage(event: PageEvent): any {
    this.size = +event.pageSize; // get the pageSize
    this.page = +event.pageIndex; // get the current page
    this.getPageableModelList();
  }

  openAddDialog(isEdit: boolean, model?: ApprovalConfiguration, ): void{
    model = model ? model : new ApprovalConfiguration();
    const dialogConfig = this.appUtils.getDialogConfig();
    dialogConfig.data = {
      model: model,
      isEdit: isEdit,
    };
    const dialogRef = this.matDialog.open(ApprovalConfigurationAddDialogComponent, dialogConfig);
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
