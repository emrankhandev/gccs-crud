import {Component, OnInit} from '@angular/core';
import {MatTableDataSource} from "@angular/material/table";
import {PageEvent} from "@angular/material/paginator";
import {MatDialog} from "@angular/material/dialog";
import {ApprovalTeamAddDialogComponent} from "../dialog/approval-team-add-dialog/approval-team-add-dialog.component";
import {locale as lngEnglish} from '../i18n/en';
import {locale as lngBangla} from '../i18n/bn';
import {TranslationLoaderService} from "../../../../../../core/service/translation-loader.service";
import {AppConstants} from "../../../../../../core/constants/app.constants";
import {AppUtils} from "../../../../../../core/utils/app.utils";
import {ApprovalTeamService} from "../../../service/approval-team.service";
import {ApprovalTeamModel} from "../../../model/approval-team-model";
import {animate, state, style, transition, trigger} from "@angular/animations";
import {PageableData} from "../../../../../../core/model/pageable-data";

@Component({
  selector: 'app-approval-team-list',
  templateUrl: './approval-team-list.component.html',
  styleUrls: ['./approval-team-list.component.scss'],
  animations: [
    trigger('detailExpand', [
      state('collapsed', style({height: '0px', minHeight: '0'})),
      state('expanded', style({height: '*'})),
      transition('expanded <=> collapsed', animate('0ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
    ]),
  ],
})
export class ApprovalTeamListComponent implements OnInit{

  /*property*/
  pagePermission: any;


  /*dataTable*/
  paginationArray: number[] = this.appConstants.DEFAULT_ARRAY;
  size: number = this.appConstants.DEFAULT_SIZE;
  page: number = this.appConstants.DEFAULT_PAGE;
  total: number = 0;
  searchValue: string = '';
  dataSource = new MatTableDataSource(new Array<ApprovalTeamModel>());
  displayedColumns: string[] = ['expanded', 'slNo', 'name', 'action',];

  /* Expandable table element*/
  expandedElement?: ApprovalTeamModel | null;


  /*object*/
  // model: UserRoleAssignModel = new UserRoleAssignModel();

  constructor(
    private matDialog: MatDialog,
    private translationLoaderService: TranslationLoaderService,
    private modelService: ApprovalTeamService,
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

  delete(obj: ApprovalTeamModel): void {
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

  openDeleteDialog(row: ApprovalTeamModel): void{
    this.appUtils.openDeleteDialog(row, this.delete.bind(this));
  }

  onChangePage(event: PageEvent): any {
    this.size = +event.pageSize; // get the pageSize
    this.page = +event.pageIndex; // get the current page
    this.getPageableModelList();
  }

  // openAddDialog(isEdit: boolean, model?: ApprovalTeamModel, ): void{
  //   model = model ? model : new ApprovalTeamModel();
  //   const dialogRef = this.matDialog.open(ApprovalTeamAddDialogComponent, this.appUtils.getAddDialogConfig());
  //   dialogRef.componentInstance.callBackMethod.subscribe(value => {
  //     this.getPageableModelList();
  //   });
  // }

  openAddDialog(isEdit: boolean, model?: ApprovalTeamModel, ): void{
    model = model ? model : new ApprovalTeamModel();
    const dialogConfig = this.appUtils.getDialogConfig();
    dialogConfig.data = {
      model: model,
      isEdit: isEdit,
    };
    const dialogRef = this.matDialog.open(ApprovalTeamAddDialogComponent, dialogConfig);
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
