import {Component, OnInit} from '@angular/core';
import {MatTableDataSource} from "@angular/material/table";
import {PageEvent} from "@angular/material/paginator";
import {MatDialog} from "@angular/material/dialog";
import {locale as lngEnglish} from '../i18n/en';
import {locale as lngBangla} from '../i18n/bn';
import {animate, state, style, transition, trigger} from "@angular/animations";
import {TranslationLoaderService} from "../../../../../core/service/translation-loader.service";
import {AppConstants} from "../../../../../core/constants/app.constants";
import {AppUtils} from "../../../../../core/utils/app.utils";
import {PageableData} from "../../../../../core/model/pageable-data";
import {NotificationAddDialogComponent} from "../dialog/add-dialog/notification-add-dialog.component";
import {NotificationModel} from "../../../model/notification-model";
import {NotificationService} from "../../../service/notification.service";

@Component({
  selector: 'app-notification-list',
  templateUrl: './notification-list.component.html',
  styleUrls: ['./notification-list.component.scss'],
  animations: [
    trigger('detailExpand', [
      state('collapsed', style({height: '0px', minHeight: '0'})),
      state('expanded', style({height: '*'})),
      transition('expanded <=> collapsed', animate('0ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
    ]),
  ],
})
export class NotificationListComponent implements OnInit{

  /*property*/
  pagePermission: any;

  /*dataTable*/
  paginationArray: number[] = this.appConstants.DEFAULT_ARRAY;
  size: number = this.appConstants.DEFAULT_SIZE;
  page: number = this.appConstants.DEFAULT_PAGE;
  total: number = 0;
  searchValue: string = '';
  dataSource = new MatTableDataSource(new Array<NotificationModel>());
  displayedColumns: string[] = ['expanded', 'slNo', 'notificationType', 'action'];

  /* Expandable table element*/
  expandedElement?: NotificationModel | null;

  /*object*/
 // model: NotificationModel = new NotificationModel();

  constructor(
    private matDialog: MatDialog,
    private translationLoaderService: TranslationLoaderService,
    private modelService: NotificationService,
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
      this.dataSource = new MatTableDataSource(res.data.content.map((m, index)=> ({
        ...m,
        index: index + 1
      })));
      this.total = res.data.totalElements;
    });
  }

  delete(obj: NotificationModel): void {
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

  openDeleteDialog(row: NotificationModel): void{
    this.appUtils.openDeleteDialog(row, this.delete.bind(this));
  }

  onChangePage(event: PageEvent): any {
    this.size = +event.pageSize; // get the pageSize
    this.page = +event.pageIndex; // get the current page
    this.getPageableModelList();
  }

  openAddDialog(isEdit: boolean, model?: NotificationModel, ): void{
    model = model ? model : new NotificationModel();
    const dialogConfig = this.appUtils.getDialogConfig();
    dialogConfig.data = {
      model: model,
      isEdit: isEdit,
    };
    const dialogRef = this.matDialog.open(NotificationAddDialogComponent, dialogConfig);
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
