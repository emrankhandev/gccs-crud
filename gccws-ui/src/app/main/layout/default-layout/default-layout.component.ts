import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {LocalStoreUtils} from "../../core/utils/local-store.utils";
import {navItems} from "./_nav";
import {MenuItemService} from "../../module/super-admin/service/menu-item.service";
import {MenuItem} from "../../module/super-admin/model/menu-item";

@Component({
  selector: 'app-dashboard',
  templateUrl: './default-layout.component.html',
  styleUrls: ['./default-layout.component.scss']
})
export class DefaultLayoutComponent implements OnInit{

  /*property*/
 // public navItems = navItems;
   public navItems: any = [];
  userInfo: any;
  public perfectScrollbarConfig = {
    suppressScrollX: true,
  };


  // -----------------------------------------------------------------------------------------------------
  // @ Init
  // -----------------------------------------------------------------------------------------------------
  constructor(
    private cdr: ChangeDetectorRef,
    private localStoreUtils: LocalStoreUtils
  ) {}

  ngOnInit(): void {
    this.userInfo = this.localStoreUtils.getUserInfo();
  }



  // -----------------------------------------------------------------------------------------------------
  // @ call back method
  // -----------------------------------------------------------------------------------------------------
  setNavData($event: any): any {
    this.navItems = $event;
    this.cdr.detectChanges();
  }

  // -----------------------------------------------------------------------------------------------------
  // @ helper
  // -----------------------------------------------------------------------------------------------------



}
