import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {HeaderComponent} from '@coreui/angular';
import {AuthService} from "../../../core/service/auth.service";
import {Router} from "@angular/router";
import {TranslateService} from "@ngx-translate/core";
import {LocalStoreUtils} from "../../../core/utils/local-store.utils";
import {MenuItemService} from "../../../module/super-admin/service/menu-item.service";
import {locale as lngEnglish} from "../../../layout/default-layout/default-header/i18n/en";
import {locale as lngBangla} from "../../../layout/default-layout/default-header/i18n/bn";
import {TranslationLoaderService} from "../../../core/service/translation-loader.service";
import {ApprovalHistory} from "../../../module/super-admin/approve-control/model/approval-history";
import {ApprovalHistoryService} from "../../../module/super-admin/approve-control/service/approval-history.service";
import {ApprovalStatusService} from "../../../core/mock-data/approval-status.service";
import {
  ApprovalConfigurationService
} from "../../../module/super-admin/approve-control/service/approval-configuration.service";
import {ApprovalTransactionTypeService} from "../../../core/mock-data/approval-transaction-type.service";
import {ApprovalSubmitUtils} from "../../../core/utils/approval-submit.utils";
import {AppUtils} from "../../../core/utils/app.utils";
import {ApprovalUser} from "../../../module/super-admin/approve-control/model/approval-user";
import {SnackbarHelper} from "../../../core/utils/snackbar.utils";
import {MatDialog} from "@angular/material/dialog";
import {FileService} from "../../../shared/service/file.service";
import {MessageHistory} from "../../../shared/model/message-history";
import {MessageHistoryService} from "../../../shared/service/message-history.service";
import {NoticeShowDialogComponent} from "../../../shared/component/notice/notice-show-dialog.component";

@Component({
  selector: 'app-default-header',
  templateUrl: './default-header.component.html',
  styleUrls: ['./default-header.component.scss']
})
export class DefaultHeaderComponent extends HeaderComponent implements OnInit{

  /*property*/
  callBackMethod: EventEmitter<boolean> = new EventEmitter<boolean>();
  @Input() sidebarId: string = "sidebar";
  @Output() setNav = new EventEmitter<any>();
  selectedLanguage: any;
  langEn = 'en';
  userInfo: any;
  searchValue: string = '';


  /*list*/
  menuItemList: any[] = [];

  /*dropdownList*/
  notificationList: ApprovalHistory[] = new Array<ApprovalHistory>();
  messageList: MessageHistory[] = new Array<MessageHistory>();
  // @ts-ignore
  customerInfo: CustomerInfo = new Array<CustomerInfo>();

  /*extra*/
  notificationNo: number;
  interval: any;
  messageNo: number;

  // -----------------------------------------------------------------------------------------------------
  // @ Init
  // -----------------------------------------------------------------------------------------------------

  constructor(
    private authService: AuthService,
    private router: Router,
    private _translateService: TranslateService,
    private translationLoaderService: TranslationLoaderService,
    private menuItemService: MenuItemService,
    private approvalHistoryService: ApprovalHistoryService,
    public approvalStatusService: ApprovalStatusService,
    private approvalConfigService: ApprovalConfigurationService,
    private messageHistoryService: MessageHistoryService,
    public approvalSubmitUtils : ApprovalSubmitUtils,

    private approvalTransactionTypeService: ApprovalTransactionTypeService,
    public appUtils : AppUtils,
    private localStoreUtils : LocalStoreUtils,
    private snackbarHelper: SnackbarHelper,
    private matDialog: MatDialog,
  ) {
    super();
    if (!this.localStoreUtils.getFromCookie(this.localStoreUtils.KEY_SELECTED_LANG)){
      this.localStoreUtils.setToCookie(this.localStoreUtils.KEY_SELECTED_LANG, this.langEn);
    }
    this.selectedLanguage = this.localStoreUtils.getFromCookie(this.localStoreUtils.KEY_SELECTED_LANG);
    this.translationLoaderService.loadTranslations(lngEnglish, lngBangla);
  }

  ngOnInit(): void {
    this.userInfo = this.localStoreUtils.getUserInfo();
    if(this.userInfo.empPersonalInfoId){
      this.getEmpInfoById();
    }
    if(this.userInfo.customerInfoId){
      this.getCustomerInfoById();
    }
    this.getNotificationList();
    this.getMessageList();
    this.getMenuItemActiveList();
    this.interval = setInterval(() => {
      this.getNotificationList();
      this.getMessageList();
    }, 10000);
  }

  ngOnDestroy() {
    clearInterval(this.interval);
  }

  // -----------------------------------------------------------------------------------------------------
  // @ API calling
  // -----------------------------------------------------------------------------------------------------
  getNotificationList(){
    this.approvalHistoryService.getNotificationListByUserId().subscribe(res => {
      const tempList = new Array<ApprovalHistory>();
      res.data.forEach(value => {
        tempList.push(this.addUIProperty(value));
      });
      this.notificationList = tempList;
      // console.log(this.notificationList);
      let notSeenList = [];
      notSeenList = this.notificationList.filter(e => !e.isSeen);
      this.notificationNo = notSeenList.length;
    });
  }

  getMessageList(){
    this.messageHistoryService.getByUserId().subscribe(res => {
      this.messageList = res.data;
      let tempList = [];
      this.messageList.forEach( e => {
        if(e.isRead == false || e.isRead == null){
          tempList.push(e);
        }
      });
      this.messageNo = tempList.length;
      // console.log(res.data);
    });
  }

  onClickSubmit(approvalHistory: ApprovalHistory): void{
    if(approvalHistory.approvalStatusId === this.approvalStatusService.BACK){
      this.approvalConfigService.getSubmitUserByDepartmentAndAppUserId(Number(approvalHistory.departmentId), Number(this.userInfo.id), Number(approvalHistory.transactionTypeId)).subscribe(res => {
        const approveUser: ApprovalUser = res.data;
        if(!approvalHistory.approvalStatusId  || approveUser){
          this.openSubmitDialog(approvalHistory, approveUser);
        }else {
          this.appUtils.showErrorMessage(this.snackbarHelper.PERMISSION_MSG_BN,this.snackbarHelper.PERMISSION_MSG);
        }
      });
    }else {
      this.approvalConfigService.getApproveAndForwardUserByDepartmentAndAppUserId(Number(approvalHistory.departmentId), Number(this.userInfo.id), Number(approvalHistory.transactionTypeId)).subscribe(res => {
        const approveUser: ApprovalUser = res.data;
        if(!approvalHistory.approvalStatusId  || approveUser){
          this.openSubmitDialog(approvalHistory, approveUser);
        }else {
          this.appUtils.showErrorMessage(this.snackbarHelper.PERMISSION_MSG_BN,this.snackbarHelper.PERMISSION_MSG);
        }
      });
    }
  }

  isClick(row: ApprovalHistory, model?: MessageHistory):any {
    this.approvalHistoryService.getObjectById(Number(row.id)).subscribe(res => {
      res.data.isSeen = true;
      this.approvalHistoryService.update(res.data).subscribe();
    });
    //if(row.isClose) return;
    this.onClickSubmit(row);
  }

  isCross(row: ApprovalHistory): void {
    row.isCross = true;
    this.approvalHistoryService.update(row).subscribe(res => this.getNotificationList());
  }

  isClickMessage(model: MessageHistory):any {
    model.isRead = true;
    model.readDate = new Date();
    this.messageHistoryService.update(model).subscribe(res => this.getMessageList());
    model = model ? model : new MessageHistory();
    const dialogConfig = this.appUtils.getDialogConfig();
    dialogConfig.data = {
      model: model,
      isMeeting: false
    };
    let dialogRef;
    if(model.transactionTable === 'BILL_VESSEL_DECLARATION'){
      this.router.navigateByUrl('/', {skipLocationChange: true}).then(() =>
        this.router.navigate(['/billing/bill-transaction/vessel-declaration'])
      );
     // this.router.navigate(['/billing/bill-transaction/vessel-declaration']);
    }
    else if(model.transactionTable === 'BILL_NOC_APPLICATION'){

      if(model.link == 'link'){
        //application
        this.router.navigateByUrl('/', {skipLocationChange: true}).then(() =>
          this.router.navigate(['/account/noc/noc-list'])
        );
       // this.router.navigate(['/account/noc/noc-list']);
      }else{
        //approve
        if(model.isAction){
          this.router.navigateByUrl('/', {skipLocationChange: true}).then(() =>
            this.router.navigate(['/billing/bill-transaction/noc-application-agent'])
          );
         // this.router.navigate(['/billing/bill-transaction/noc-application-agent']);
        }else{
          dialogRef = this.matDialog.open(NoticeShowDialogComponent, dialogConfig);
        }
      }
    }
    else if(model.transactionTable === 'BILL_CUSTOMER_INFO'){
      if(model.link == 'agent-request'){
        this.router.navigateByUrl('/', {skipLocationChange: true}).then(() =>
          this.router.navigate(['/billing/bill-transaction/agent-approve'])
        );
        //this.router.navigate(['/billing/bill-transaction/agent-approve']);
      }
      else{
        dialogRef = this.matDialog.open(NoticeShowDialogComponent, dialogConfig);
        //this.logout();
      }
    }
    else if(model.transactionTable === 'BILL_SERVICE_REQUEST_MASTER'){
      this.router.navigateByUrl('/', {skipLocationChange: true}).then(() =>
        this.router.navigate(['/billing/bill-transaction/service-request'])
      );
     // this.router.navigate(['/billing/bill-transaction/service-request']);
    }
    else if(model.transactionTable === 'BILL_BILLING_MASTER'){
      this.router.navigateByUrl('/', {skipLocationChange: true}).then(() =>
        this.router.navigate(['\'/billing/bill-transaction/billing'])
      );
      //this.router.navigate(['/billing/bill-transaction/billing']);
    }
    else if(model.transactionTable === 'BILL_BILL_PAYMENT'){
      if(model.link == 'request'){ // 7 : 13
        if(model.attachment == '7'){
          this.router.navigateByUrl('/', {skipLocationChange: true}).then(() =>
            this.router.navigate(['/billing/bill-transaction/bill-payment-shipping-agent'])
          );
         // this.router.navigate(['/billing/bill-transaction/bill-payment-shipping-agent']);
        }else{
          this.router.navigateByUrl('/', {skipLocationChange: true}).then(() =>
            this.router.navigate(['/billing/bill-transaction/bill-payment-candf'])
          );
         // this.router.navigate(['/billing/bill-transaction/bill-payment-candf']);
        }
      }
      else if(model.link == 'PAY'){ //8: 14
        this.router.navigateByUrl('/', {skipLocationChange: true}).then(() =>
          this.router.navigate(['/billing/bill-transaction/billing-cover-page'])
        );
       // this.router.navigate(['/billing/bill-transaction/billing-cover-page']);
      }
      else{
        dialogRef = this.matDialog.open(NoticeShowDialogComponent, dialogConfig);
      }
    }
    else if(model.transactionTable === 'C_AND_F_SERVICE_REQUEST'){
      if(model.link == 'req'){
        this.router.navigateByUrl('/', {skipLocationChange: true}).then(() =>
          this.router.navigate(['/billing/bill-transaction/c-and-f-agent-request'])
        );
       // this.router.navigate(['/billing/bill-transaction/c-and-f-agent-request']);
      }
      else if(model.link == 'approved'){
        this.router.navigateByUrl('/', {skipLocationChange: true}).then(() =>
          this.router.navigate(['/billing/bill-transaction/c-and-f-agent-request-agent'])
        );
       // this.router.navigate(['/billing/bill-transaction/c-and-f-agent-request-agent']);

      }
      else{
        dialogRef = this.matDialog.open(NoticeShowDialogComponent, dialogConfig);
      }
    }
    else {
      dialogRef = this.matDialog.open(NoticeShowDialogComponent, dialogConfig);
    }

  }

  isCloseMessage(model: MessageHistory, event: Event){
    model.isClose = true;
    event.stopPropagation();
    this.messageHistoryService.update(model).subscribe(res => this.getMessageList());
  }

  isClearAll(event: Event){
    event.stopPropagation();
    this.messageList.forEach(item => {
      item.isClose = true;
      this.messageHistoryService.update(item).subscribe(res => this.getMessageList());
    });
  }

  isMessageItemDisable(model: MessageHistory): boolean {
    //this.messageNo = this.messageNo - 1 ;
    return false;
  }

  getMenuItemActiveList(): any {
    this.menuItemService.getPageByAppUserId().subscribe(res => {
      this.menuItemList = res.data;
      this.localStoreUtils.setToLocalStore(this.localStoreUtils.KEY_MENU_ITEM_URL, this.menuItemList);
      this.setLanguage(this.selectedLanguage);
    });
  }

  getEmpInfoById(): any {
    // this.empPersonalInfoService.getObjectById(this.userInfo.empPersonalInfoId).subscribe(res => {
    //   this.empPersonalInfo = res.data;
    // });
  }

  getCustomerInfoById(): any {
    // this.customerInfoService.getObjectById(this.userInfo.customerInfoId).subscribe(res => {
    //   this.customerInfo = res.data;
    //   console.log(res.data)
    // });
  }

  openShowDialog(): void{
    // this.empPersonalInfoService.getObjectById(this.userInfo.empPersonalInfoId).subscribe(res => {
    //   let model: EmpPersonalInfo = res.data ? res.data : new EmpPersonalInfo();
    //   const dialogConfig = this.appUtils.getDialogConfig('90%', '90%');
    //   dialogConfig.data = {
    //     model: model,
    //   };
    //   // const dialogRef = this.matDialog.open(TrafficUserProfileShowDialogComponent, dialogConfig);
    //   // dialogRef.componentInstance.callBackMethod.subscribe(value => {
    //   // });
    // });

  }

  // -----------------------------------------------------------------------------------------------------
  // @ view method
  // -----------------------------------------------------------------------------------------------------
  logout(): void{
    this.authService.signOut();
    this.router.navigate(['']);
  }

  resetPassword(): void {
    this.router.navigateByUrl('/reset-password');
  }

  approvalHistory(): void {
    this.router.navigateByUrl(
      '/super-admin/approve-control/approval-history',
    );
  }


  setLanguage(lang: any): void {

    // Set the selected language for the toolbar
    this.selectedLanguage = lang;

    // Use the selected language for translations
    this._translateService.use(lang);

    // set lang to cookies
    // this.cookieService.set(this.langCookiesName, lang);
    this.localStoreUtils.setToCookie(this.localStoreUtils.KEY_SELECTED_LANG, lang)

    this.generateNavJson(lang);
  }


  generateNavJson(lang: any){
    const isLocal: boolean = lang === this.langEn ? false : true;

    /*generate module list*/
    const moduleList: any[] = [];
    this.menuItemList.forEach(value => {
      const module = value.parent;
      if (!moduleList.find(model => model.id === module.id)){
        moduleList.push(module);
      }
    });
    moduleList.sort((a, b) => (a.serialNo > b.serialNo) ? 1 : ((b.serialNo > a.serialNo) ? -1 : 0));

    /*generate group list*/
    // const menuGroupList: any[] = [];
    // this.menuItemList.forEach(value => {
    //   const menuGroup = value.parent;
    //   if (!menuGroupList.find(model => model.id === menuGroup.id)){
    //     menuGroupList.push(menuGroup);
    //   }
    // });
    // menuGroupList.sort((a, b) => (a.serialNo > b.serialNo) ? 1 : ((b.serialNo > a.serialNo) ? -1 : 0));

    /*generate menu list*/
    const menuList: any[] = [];
    this.menuItemList.forEach(value => {
      const menu = value;
      if (!menuList.find(model => model.id === menu.id)){
        menuList.push(menu);
      }
    });
    menuList.sort((a, b) => (a.serialNo > b.serialNo) ? 1 : ((b.serialNo > a.serialNo) ? -1 : 0));


    /*generate nav json*/
    const navItems: any[] = [];
    navItems.push({
      name: isLocal ? 'ড্যাশবোর্ড' : 'Dashboard',
      url: '/dashboard-home',
      iconComponent: { name: 'cil-home' },
    },);

    moduleList.forEach(value => {
      navItems.push({
        name: isLocal ? value.banglaName : value.name,
        url: value.url,
        iconComponent: { name: value.icon },
        children: this.getMenu(menuList, value.id, isLocal)
      });
    });
    this.setNav.emit(navItems);
  }

  getMenuGroup(menuGroupList: any[], menuList: any[], moduleId: any, isLocal: boolean): any[]{
    const menuGroups: any[] = [];
    menuGroupList.forEach(value => {
      if (value.parent.id === moduleId){
        menuGroups.push({
          name: isLocal ? value.banglaName : value.name,
          url: value.parent.url + value.url,
          children: this.getMenu(menuList, value.id, isLocal)
        });
      }
    });
    return menuGroups;
  }

  getMenu(menuList: any[], menuGroupId: any, isLocal: boolean): any[]{
    const menus: any[] = [];
    menuList.forEach(value => {
      if (value.parent.id === menuGroupId){
        menus.push({
          name: isLocal ?  value.banglaName : value.name,
          url: value.parent.url + value.url,
        });
      }
    });
    return menus;
  }

  openSubmitDialog(approvalHistory: ApprovalHistory, approveUser: ApprovalUser): void{
    const transactionData = {
      transactionId : approvalHistory.transactionId,
      approvalHistoryId : approvalHistory.id,
      totalAmount : approvalHistory.totalAmount,
      transactionDescription : approvalHistory.transactionDescription,
      isSkipApprovalAction : !approvalHistory.approvalStatusId ? true : false,
    }
    if(approvalHistory.isClose) {
      transactionData.isSkipApprovalAction = true;
    };
    if(approvalHistory.transactionTypeId === this.approvalTransactionTypeService.JOURNAL_ENTRY){
      this.approvalSubmitUtils.openAccountTransactionApprovalSubmitDialog(transactionData, Number(approvalHistory.departmentId), Number(approvalHistory.approvalTransactionTypeId), approveUser);
    }
    else if(approvalHistory.transactionTypeId === this.approvalTransactionTypeService.SALARY_APPROVAL){
      this.approvalSubmitUtils.openSalaryApprovalSubmitDialog(transactionData, Number(approvalHistory.departmentId), Number(approvalHistory.approvalTransactionTypeId), approveUser);
    }

  }

  /*isNotificationItemDisable(row: ApprovalHistory): boolean{
    if(!row.approvalStatusId){
      return false;
    }else if(row.isSeen && row.isClose){
      return true;
    }
    return false;
  }*/
  // -----------------------------------------------------------------------------------------------------
  // @ util method
  // -----------------------------------------------------------------------------------------------------

  // openEditProfileDialog(): void{
  //   const dialogConfig = this.appUtils.getDialogConfig('90%', '90%');
  //   dialogConfig.data = {
  //     isEditProfile: true,
  //     model: this.empPersonalInfo,
  //     isEdit: true
  //   };
  //   // const dialogRef = this.matDialog.open(EmployeeProfileAddDialogComponent, dialogConfig);
  //   // dialogRef.componentInstance.callBackMethod.subscribe(value => {
  //   // });
  // }
  //

  // -----------------------------------------------------------------------------------------------------
  //@ Helper
  // -----------------------------------------------------------------------------------------------------
  addUIProperty(approvalHistory: ApprovalHistory): ApprovalHistory{
    if(approvalHistory.approvalStatusId && approvalHistory.approvalStatusName === this.approvalStatusService.SUBMIT_NAME ){
      approvalHistory.iconName = 'cil-exit-to-app';
      approvalHistory.detailText = approvalHistory.approvalStatusName + " "+ approvalHistory.transactionTypeName;
      approvalHistory.iconColor = 'notify-img';
    }
    else if(approvalHistory.approvalStatusName === this.approvalStatusService.FORWARD_NAME){
      approvalHistory.iconName = 'cil-media-skip-forward';
      approvalHistory.detailText = approvalHistory.approvalStatusName + " "+ approvalHistory.transactionTypeName;
      approvalHistory.iconColor = 'forward';
    }
    else if(approvalHistory.approvalStatusName === this.approvalStatusService.BACK_NAME){
      approvalHistory.iconName = 'cil-media-skip-backward';
      approvalHistory.detailText = approvalHistory.approvalStatusName + " "+ approvalHistory.transactionTypeName;
      approvalHistory.iconColor = 'back';
    }
    else if(approvalHistory.approvalStatusName === this.approvalStatusService.REJECT_NAME){
      approvalHistory.iconName = 'cil-x-circle';
      approvalHistory.detailText = approvalHistory.approvalStatusName + " "+ approvalHistory.transactionTypeName;
      approvalHistory.iconColor = 'reject';
    }
    else if(!approvalHistory.approvalStatusId && approvalHistory.approvalStatusName === this.approvalStatusService.APPROVED_NAME){
      approvalHistory.iconName = 'cil-bell';
      approvalHistory.detailText = "Approved "+ approvalHistory.transactionTypeName;
      approvalHistory.iconColor = 'approve';
    }else{
      approvalHistory.iconName = 'cil-bell';
      approvalHistory.detailText = approvalHistory.approvalStatusName + " "+ approvalHistory.transactionTypeName;
      approvalHistory.iconColor = 'default';
    }
    return approvalHistory;
  }


}
