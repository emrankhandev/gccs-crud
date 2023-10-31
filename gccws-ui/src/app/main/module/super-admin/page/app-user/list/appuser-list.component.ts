import {Component, OnInit} from '@angular/core';
import {MatTableDataSource} from "@angular/material/table";
import {PageEvent} from "@angular/material/paginator";
import {MatDialog} from "@angular/material/dialog";
import {AppuserAddDialogComponent} from "../dialog/appuser-add-dialog/appuser-add-dialog.component";
import {AppUser} from "../../../model/app-user";
import {locale as lngEnglish} from "../i18n/en";
import {locale as lngBangla} from "../i18n/bn";
import {TranslationLoaderService} from "../../../../../core/service/translation-loader.service";
import {AppUserService} from "../../../service/app-user.service";
import {AppConstants} from "../../../../../core/constants/app.constants";
import {AppUtils} from "../../../../../core/utils/app.utils";
import {ActivatedRoute} from "@angular/router";
import {PageableData} from "../../../../../core/model/pageable-data";

@Component({
  selector: 'appuser',
  templateUrl: './appuser-list.component.html',
  styleUrls: ['./appuser-list.component.scss']
})
export class AppuserListComponent implements OnInit{

  /*property*/
  pagePermission: any;


  /*dataTable*/
  paginationArray: number[] = this.appConstants.DEFAULT_ARRAY;
  size: number = this.appConstants.DEFAULT_SIZE;
  page: number = this.appConstants.DEFAULT_PAGE;
  total: number = 0;
  searchValue: string = '';
  dataSource = new MatTableDataSource(new Array<AppUser>());
  displayedColumns: string[] = ['slNo', 'empPersonalInfoName', 'userName', 'passwordPolicy', 'displayName', 'status', 'action'];


  /*object*/
  model: AppUser = new AppUser();

  /*extra*/

  constructor(
    private matDialog: MatDialog,
    private translationLoaderService: TranslationLoaderService,
    private modelService: AppUserService,
    private appConstants : AppConstants,
    private activatedRoute: ActivatedRoute,

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

  delete(obj: AppUser): void {
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

  openDeleteDialog(row: AppUser): void{
    this.appUtils.openDeleteDialog(row, this.delete.bind(this));
  }

  onChangePage(event: PageEvent): any {
    this.size = +event.pageSize; // get the pageSize
    this.page = +event.pageIndex; // get the current page
    this.getPageableModelList();
  }

  openAddDialog(isEdit: boolean, model?: AppUser, ): void{
    model = model ? model : new AppUser();
    const dialogConfig = this.appUtils.getDialogConfig();
    dialogConfig.data = {
      model: model,
      isEdit: isEdit,
      empId: model.empPersonalInfoId,
    };
    const dialogRef = this.matDialog.open(AppuserAddDialogComponent, dialogConfig);
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

  onSubmit(): void {
    this.modelService.update(this.model).subscribe({
      next: (res ) => {
        this.appUtils.onServerResponse(res);
        if (res.status){
          this.model = res.data;
        }
        this.getPageableModelList();
      },
      error: (error) => {
        this.appUtils.onServerErrorResponse(error);
      }
    });
  }

  saveValue(model: AppUser): void{
    this.model = model;
    this.appUtils.openConfirmationDialog("Do you want to Submit?", this.onSubmit.bind(this));
  }


}
