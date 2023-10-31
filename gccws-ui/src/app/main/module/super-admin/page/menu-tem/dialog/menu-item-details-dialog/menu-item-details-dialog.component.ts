import {Component, EventEmitter, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {MenuItem} from "../../../../model/menu-item";
import {Menu} from "@angular/cdk/menu";
import {MenuItemService} from "../../../../service/menu-item.service";
import {AppUtils} from "../../../../../../core/utils/app.utils";
import {MatTableDataSource} from "@angular/material/table";
import {TranslationLoaderService} from "../../../../../../core/service/translation-loader.service";
import {locale as lngEnglish} from "../../i18n/en";
import {locale as lngBangla} from "../../i18n/bn";
import {MenuTypeService} from "../../../../../../core/mock-data/menu-type.service";

/**
 * @Author		Rima
 * @Since		  September 29, 2022
 * @version		1.0.0
 */
@Component({
    selector: 'app-menu-item-details-dialog',
    templateUrl: './menu-item-details-dialog.component.html',
    styleUrls: ['./menu-item-details-dialog.component.scss']
})
export class MenuItemDetailsDialogComponent implements OnInit {

  /*property*/
  callBackMethod: EventEmitter<boolean> = new EventEmitter<boolean>();
  panelOpenState = false;

  /*form*/
  model: MenuItem = new MenuItem();

  /*dropdownList*/

  /*extra*/
  /*list*/
  menuItemList: MenuItem[] = new Array<MenuItem>();

  /*dataTable*/
  dataSource = new MatTableDataSource(new Array<MenuItem>());
  // -----------------------------------------------------------------------------------------------------
  // @ Init
  // -----------------------------------------------------------------------------------------------------
  constructor(
    public dialogRef: MatDialogRef<MenuItemDetailsDialogComponent>,
    @Inject(MAT_DIALOG_DATA) data: any,
    public menuItemService: MenuItemService,
    public menuTypeService: MenuTypeService,
    private translationLoaderService: TranslationLoaderService,
    private appUtils: AppUtils,
  ) {
    this.translationLoaderService.loadTranslations(lngEnglish, lngBangla);
    this.model = data.model;
  }

  ngOnInit(): void {
   //this.getMenuItemList()
  }

  // -----------------------------------------------------------------------------------------------------
  // @ API calling
  // -----------------------------------------------------------------------------------------------------

  getMenuItemList(): void {
    //console.log(this.model.course.id)
    this.menuItemService.getByMenuItemId(this.model.id).subscribe(res => {
      console.log(res.data)
      this.menuItemList= res.data;
    });
  }


  // -----------------------------------------------------------------------------------------------------
  // @ view method
  // -----------------------------------------------------------------------------------------------------

  closeDialog(): void {
      this.dialogRef.close();
  }


  // -----------------------------------------------------------------------------------------------------
  // @ Helper Method
  // -----------------------------------------------------------------------------------------------------



}
