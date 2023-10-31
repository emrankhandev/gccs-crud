import {Injectable} from '@angular/core';
import {environment} from "../../../../environments/environment";
import {SnackbarHelper} from "./snackbar.utils";
import {MatDialog, MatDialogConfig} from "@angular/material/dialog";
import {ConfirmationDialogComponent} from "../../shared/component/confirmation-dialog/confirmation-dialog.component";
import {TranslateService} from "@ngx-translate/core";
import {Router} from "@angular/router";
import {LocalStoreUtils} from "./local-store.utils";
import {ReportService} from "../../module/super-admin/service/report.service";
import {EmpCategoryTypeService} from "../mock-data/emp-category-type.service";


@Injectable({
  providedIn: 'root'
})
export class AppUtils {

  /*validator id*/
  DEV_CODE_REPORT_UPLOAD_VALIDATOR = 1;
  DEV_CODE_SIGNATURE_UPLOAD_VALIDATOR = 3;
  DEV_CODE_PROFILE_IMAGE_UPLOAD_VALIDATOR = 4;
  DEV_CODE_CUSTOMER_DOCUMENT_UPLOAD_VALIDATOR = 5;
  DEV_CODE_VESSEL_DOCUMENT_UPLOAD_VALIDATOR = 6;
  DEV_CODE_C_AND_F_AGENT_REQUEST_DOCUMENT_UPLOAD_VALIDATOR = 7;
  DEV_CODE_JOURNAL_DOCUMENT_UPLOAD_VALIDATOR = 8;
  DEV_CODE_BILL_COVER_PAGE_DOCUMENT_UPLOAD_VALIDATOR = 9;
  DEV_CODE_NOC_DOCUMENT_UPLOAD_VALIDATOR = 10;
  DEV_CODE_CUSTOMER_LOGO_UPLOAD_VALIDATOR = 11;
  DEV_CODE_BILL_PAYMENT_DOCUMENT_UPLOAD_VALIDATOR = 12;

  /*MODULE_ID*/
  PAYROLL_MODULE_ID = 17;
  ACCOUNT_MODULE_ID = 29;

  /*dev code*/
  DIVISION_ID = 5;
  DISTRICT_ID = 6;
  UPAZILA_ID = 7;
  GENDER_ID = 8;
  NATIONALITY_ID = 9;
  RELIGION_ID = 10;
  BLOOD_GROUP_ID = 11;
  MARITIAL_STATUS_ID = 12;
  RELATION_TYPE_ID = 13;

  DESIGNATION_ID = 15;
  CLASS_ID = 16;
  GRADE_ID = 17;
  ACCOUNT_TYPE_ID = 18;
  DEPARTMENT_ID = 2;
  SECTION_ID = 3;
  SUB_SECTION_ID = 4;
  IN_ACTIVE_ID = 36;
  OFFICE_ID = 1;
  BUDGET_GROUP_ID=19;
  ORGANIZATION_ID=20;
  CURRENCY_ID =21;
  TARIFF_ITEM_ID =22;
  SERVICE_TYPE_ID =23;
  VESSEL_TYPE_ID = 25;
  CARGO_UNIT_ID = 26;
  REQUEST_TYPE_ID =27;
  IMPORTER_ID =28;
  EXPORTER_ID =29;
  BASIS_OF_CHARGE_ID =30;
  CUSTOMER_TYPE_ID = 31;
  CATEGORY_ID = 32;
  UNIT_ID = 33;

  // For Tariff
  PORT_DUES = 1;
  PILOTAGE = 2;
  TUGS = 3;

  /*details id*/

  /*dialog property*/
  DIALOG_HEIGHT = 'auto';
  ADD_DIALOG_WIDTH = '60%';
  CONFIRM_DIALOG_WIDTH = '30%';
  CONFIRM_DIALOG_HEIGHT = this.DIALOG_HEIGHT;
  DIALOG_CLASS_PANEL = 'dialog-background';

  /*search*/
  SEARCH_SEPARATOR: string = '008800';

  /*translate property*/
  langEn: string = 'en';

  /*pattern*/
  ADD_DATE_WITH_TIME = '1970-01-01T';
  DATE_PIPE_PATTERN = 'dd-MM-yyyy';
  PIPE_TIME_FORMAT = 'hh:mm a';
  PIPE_DAY_NAME_FORMAT = 'EEE';
  FORMAT_DATE = 'yyyy-MM-dd';
  TIME_FORMAT = 'HH:mm';
  TIME_FORMAT_PATTERN = '([01]?[0-9]|2[0-3]):[0-5][0-9]';
  COMPARE_DATE = '1970-01-01';

  /*number decimal*/
  DIGIT_INFO: number = 2;

  constructor(
    private matDialog: MatDialog,
    private snackbarHelper: SnackbarHelper,
    private translate: TranslateService,
    private router: Router,
    private localStoreUtils: LocalStoreUtils,
    private empCategoryTypeService: EmpCategoryTypeService,

  ) {}

  // -----------------------------------------------------------------------------------------------------
  // @ TRANSLATE
  // -----------------------------------------------------------------------------------------------------

  isLocalActive(): boolean {
    return this.translate.currentLang === this.langEn ? false : true;
  }

  // -----------------------------------------------------------------------------------------------------
  // @ API
  // -----------------------------------------------------------------------------------------------------
  public getBaseURL(): string{
    return environment.app.baseApiEndPoint;
  }

  public getPrivateBaseURL(): string{
    return this.getBaseURL() + environment.app.apiEndPointPrivate ;
  }

  public getPublicBaseURL(): string{
    return this.getBaseURL() + environment.app.apiEndPointPublic ;
  }

  // -----------------------------------------------------------------------------------------------------
  // @ SnackBar
  // -----------------------------------------------------------------------------------------------------
  onServerResponse(res: any, reloadPage?: any): void {
    const message =  res.message;
    const okay = this.snackbarHelper.TEXT_OK;
    if (res.status) {
      this.snackbarHelper.openSuccessSnackBar(message, okay);
      if (reloadPage) { reloadPage();}
    } else {
      this.snackbarHelper.openErrorSnackBar(message, okay);
    }
  }

  onServerErrorResponse(error: any): void {
    this.snackbarHelper.openErrorSnackBar(this.snackbarHelper.TEXT_SERVER_ERROR, this.snackbarHelper.TEXT_OK);
  }

  public detailsLastEntryDeleteMsg(): void{
    const message = this.isLocalActive() ? 'অন্তত একটি আইটেম প্রয়োজন!' : 'At least one item needed!';
    const ok = this.isLocalActive() ? this.snackbarHelper.TEXT_OK_BN :this.snackbarHelper.TEXT_OK;
    this.snackbarHelper.openErrorSnackBar(message, ok);
  }

  public itemAlreadyTakenMsg(): void{
    const message = this.isLocalActive() ? 'আইটেম ইতিমধ্যে নেওয়া হয়েছে !' : 'Item already taken !';
    const ok = this.isLocalActive() ? this.snackbarHelper.TEXT_OK_BN :this.snackbarHelper.TEXT_OK;
    this.snackbarHelper.openErrorSnackBar(message, ok);
  }

  public radioBtnSelectMsg(): void{
    const message = this.isLocalActive() ? 'কারণ নির্বাচন করতে হবে!' : 'Must be selected reason !';
    const ok = this.isLocalActive() ? this.snackbarHelper.TEXT_OK_BN :this.snackbarHelper.TEXT_OK;
    this.snackbarHelper.openErrorSnackBar(message, ok);
  }

  showSuccessMessage(msgBn: string, msgEn: string): void {
    const message = this.isLocalActive() ? msgBn : msgEn;
    const ok = this.isLocalActive() ? this.snackbarHelper.TEXT_OK_BN :this.snackbarHelper.TEXT_OK;
    this.snackbarHelper.openSuccessSnackBar(message, ok);
  }

  showErrorMessage(msgBn: string, msgEn: string): void {
    const message = this.isLocalActive() ? msgBn : msgEn;
    const ok = this.isLocalActive() ? this.snackbarHelper.TEXT_OK_BN :this.snackbarHelper.TEXT_OK;
    // const ok = this.snackbarHelper.TEXT_OK;
    this.snackbarHelper.openErrorSnackBar(message, ok);
  }

  // -----------------------------------------------------------------------------------------------------
  // @ Dialog
  // -----------------------------------------------------------------------------------------------------

  openDeleteDialog(viewModel: any , callBackDelete: any): void {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.disableClose = false;
    dialogConfig.autoFocus = false;
    dialogConfig.width = this.CONFIRM_DIALOG_WIDTH;
    dialogConfig.height = this.CONFIRM_DIALOG_HEIGHT;
    dialogConfig.panelClass = this.DIALOG_CLASS_PANEL;
    dialogConfig.data = {message: 'Do you want to delete?'};
    const dialogRef = this.matDialog.open(ConfirmationDialogComponent, dialogConfig);
    dialogRef.componentInstance.closeEventEmitter.subscribe(res => {
      if (res) {
        callBackDelete(viewModel);
      }
      dialogRef.close(true);
    });
  }

  getDialogConfig(width?: string, height?: string): MatDialogConfig{
    const dialogConfig = new MatDialogConfig();
    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = false;
    dialogConfig.width = width ? width : this.ADD_DIALOG_WIDTH;
    dialogConfig.height = height ? height : this.DIALOG_HEIGHT;
    dialogConfig.panelClass = this.DIALOG_CLASS_PANEL;
    return dialogConfig;
  }

  getAddDialogConfig(isEdit: boolean, model: any, width?: string, height?: string): MatDialogConfig{
    const dialogConfig = new MatDialogConfig();
    dialogConfig.disableClose = false;
    dialogConfig.autoFocus = false;
    dialogConfig.width = width ? width : this.ADD_DIALOG_WIDTH;
    dialogConfig.height = height ? height : this.DIALOG_HEIGHT;
    dialogConfig.panelClass = this.DIALOG_CLASS_PANEL;
    dialogConfig.data = {
      model: model,
      isEdit: isEdit,
    };
    return dialogConfig;
  }


  openConfirmationDialog( message:any, callBackMethod: any,): void {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.disableClose = false;
    dialogConfig.autoFocus = false;
    dialogConfig.width = this.CONFIRM_DIALOG_WIDTH;
    dialogConfig.height = this.CONFIRM_DIALOG_HEIGHT;
    dialogConfig.panelClass = this.DIALOG_CLASS_PANEL;
    dialogConfig.data = {message:message};
    const dialogRef = this.matDialog.open(ConfirmationDialogComponent, dialogConfig);
    dialogRef.componentInstance.closeEventEmitter.subscribe(res => {
      if (res && callBackMethod) {
        callBackMethod();
      }
      dialogRef.close(true);
    });
  }

  // -----------------------------------------------------------------------------------------------------
  // @ Date
  // -----------------------------------------------------------------------------------------------------
  convertJsonDateIntoDate(jsonDate: Date | undefined): any{
    return jsonDate ?  new Date(jsonDate).toISOString().split('T')[0] : null;
  }

  isDateGreaterThenCurrentDate(date: Date, msgBn: string, msgEn: string): boolean{
    let retrunValue = false;
    if (!date){
      retrunValue = false;
    }
    if (new Date(date) > new Date() || new Date(date) == new Date()){
      const message = this.isLocalActive() ? msgBn +' বর্তমান তারিখের চেয়ে বড় হতে পারে না!' : msgEn + ' cannot be greater then current Date !';
      const ok = this.isLocalActive() ? this.snackbarHelper.TEXT_OK_BN :this.snackbarHelper.TEXT_OK;
      this.snackbarHelper.openErrorSnackBar(message, ok);
      retrunValue = true;
    }
    return retrunValue;
  }

  isDateLessThanCurrentDate(date: Date, msgBn: string, msgEn: string): boolean{
    const currentDate = this.getDateOnly(new Date());

    let returnValue = false;
    if (!date){
      returnValue = false;
    }
    if (new Date(date) < currentDate){
      const message = this.isLocalActive() ? msgBn +' বর্তমান তারিখের চেয়ে ছোট হতে পারে না!' : msgEn + ' cannot be less then current Date !';
      const ok = this.isLocalActive() ? this.snackbarHelper.TEXT_OK_BN :this.snackbarHelper.TEXT_OK;
      this.snackbarHelper.openErrorSnackBar(message, ok);
      returnValue = true;
    }
    return returnValue;
  }


  isFromDateAndToDateValid(fromDate: Date, toDate: Date, msgBan?: string, msgEng?: string): boolean{
    if (fromDate && toDate){
      if (this.getDateOnly(fromDate) > this.getDateOnly(toDate)){
        let message = '';
        if(msgBan && msgEng){
          message = this.isLocalActive() ? msgBan : msgEng;
        }else{
          message = this.isLocalActive() ? 'তারিখ থেকে তারিখ পর্যন্ত বেশি হতে পারে না' : 'From Date cannot be greater than To Date!';
        }
        const ok = this.isLocalActive() ? this.snackbarHelper.TEXT_OK_BN :this.snackbarHelper.TEXT_OK;
        this.snackbarHelper.openErrorSnackBar(message, ok);
        return false;
      }else{
        return true;
      }
    }else{
      return true;
    }
  }


  isDatePresentInFinYear(finYear: any, inputDate: Date): boolean{
    let response: boolean = true;
    if(!finYear || !inputDate){
      response = false;
    }
    const transactionDate = new Date(inputDate);
    const startDate =new Date(finYear.extraFromDate) ;
    const endDate = new Date(finYear.extraToDate);
    if(transactionDate <= startDate || transactionDate >= endDate){
      this.showErrorMessage('লেনদেনের তারিখ শুরু এবং শেষ তারিখের মধ্যে হতে হবে !' , 'Transaction date must be between the start and end date !');
      response = false;
    }
    return response;
  }


  getCurrentDate(): any {
    const currentDate = new Date();
    return this.convertJsonDateIntoDate(currentDate);
  }

  getFirstDateOfCurrentMonth(): any {
    const currentDate = new Date();
    currentDate.setDate(1);
    return this.convertJsonDateIntoDate(currentDate);
  }

  // -----------------------------------------------------------------------------------------------------
  // @ TIME
  // -----------------------------------------------------------------------------------------------------
  formatTime(fromGroup: any, control: string, formValue: any): void {
    const formTimeFormValue = formValue.value.replace(':', '');
    // console.log(formTimeFormValue);
    const formTime = formTimeFormValue.length === 3 ? '0' + formTimeFormValue : formTimeFormValue;
    // const formTime = formTimeFormValue.length === 2 ?  formTimeFormValue + '00' : formTimeFormValue;
    if (formTime.length === 4) {
      // console.log(formTime.substring(0, 2));
      let minute = formTime.substring(2, 4);
      if ( Number(minute) > 60){
        minute = 59;
      }else {
        minute = formTime.substring(2, 4);
      }
      // console.log(minute);
      const formatTime = formTime.substring(0, 2) + ':' + minute;
      fromGroup.patchValue({
        [control]: formatTime
      });
    }
  }

  getTimeInDate(timeString: string | undefined): Date {
    if(timeString){
      const now = new Date();
      // @ts-ignore
      now.setHours(Number(timeString.split(':')[0]));
      // @ts-ignore
      now.setMinutes(Number(timeString.split(':')[1]));
      return now;
    }else{
      return new Date();
    }
  }

  // -----------------------------------------------------------------------------------------------------
  // @ PERMISSION
  // -----------------------------------------------------------------------------------------------------
  findUserPagePermission(): any {
    let pagePermission = null;
    let routerUrl = this.router.url;
    routerUrl = routerUrl.split(';')[0];
    const menuItemUrl: any [] = this.localStoreUtils.getFromLocalStore(this.localStoreUtils.KEY_MENU_ITEM_URL);
    menuItemUrl.forEach(value => {
      const menuUrl = value.parent.url + value.url;
      if (menuUrl === routerUrl){
        pagePermission = value;
      }
    });

    if (!pagePermission) {
      this.router.navigateByUrl('dashboard-home');
    }
    return pagePermission;
  }

  getModuleId():any{
    const pagePermission = this.findUserPagePermission();
    return pagePermission.parent.parent.id
  }


  // -----------------------------------------------------------------------------------------------------
  // @ Report
  // -----------------------------------------------------------------------------------------------------

  printSalarySheetEnForSalaryApproval(financialYearId: any, monthId: any, departmentId: any, empCatTypeId: any, reportService: ReportService){
    const params = new Map<string, string>();

    let reportId = '' ;
    if(empCatTypeId === this.empCategoryTypeService.PAYRA_PORT){
      reportId =  '2' ;
    }
    else if(empCatTypeId === this.empCategoryTypeService.DEPUTATION){
      reportId =  '3' ;
    }
    else if (empCatTypeId === this.empCategoryTypeService.NAVY_OFFICER){
      reportId =  '4' ;
    }

    params.set('id', reportId);
    params.set('P_MODULE_ID', "17");
    params.set('P_FIN_YEAR_ID', financialYearId);
    params.set('P_MONTH_ID', monthId);
    // params.set('P_DEPARTMENT_ID', departmentId);
    this.printReport(reportService, params);
  }

  printBonusSheetEnForBonusApproval(financialYearId: any, monthId: any, departmentId: any, empCatTypeId: any, reportService: ReportService, controlLoader?: any){
    const params = new Map<string, string>();
    params.set('id', "13"); // id = dev code
    params.set('P_MODULE_ID', "17"); //module_id = parent id
    params.set('P_FIN_YEAR_ID', financialYearId);
    params.set('P_MONTH_ID', monthId);
    params.set('P_EMP_CAT_TYPE_ID', empCatTypeId);
    // params.set('P_DEPARTMENT_ID', departmentId);
    this.printReport(reportService, params, controlLoader);
  }

  printServiceBill(serviceBillId: any, reportService: ReportService){
    const params = new Map<string, string>();
    params.set('id', '15'); // id = dev code
    params.set('P_MODULE_ID', "41"); //module_id = parent id
    params.set('P_MAIN_ID', serviceBillId);
    this.printReport(reportService, params);
  }

  printServiceAgentBill(serviceAgentBillId: any, reportService: ReportService){
    const params = new Map<string, string>();
    params.set('id', '16'); // id = dev code
    params.set('P_MODULE_ID', "41"); //module_id = parent id
    params.set('P_MAIN_ID', serviceAgentBillId);
    this.printReport(reportService, params);
  }

  printJournal(id: any, reportService: ReportService, controlLoader: any){
    const params = new Map<string, string>();
    params.set('id', '14'); // // id = dev code
    params.set('P_MODULE_ID', "72"); //module_id = parent id
    params.set('P_MAIN_ID', id);
    this.printReport(reportService, params, controlLoader);
  }


  printReport(reportService: ReportService,reportParam: Map<string, string>, controlLoader?: any): any {
    reportParam.set('reportFormat', 'pdf');
    const convMap = {};
    reportParam.forEach((val: string, key: string) => {
      // @ts-ignore
      convMap[key] = val;
    });
    console.error(convMap);
    if(controlLoader){
      controlLoader(true);
    }
    // get file from server
    reportService.printReport(convMap).subscribe(blob => {
      if(controlLoader){
        controlLoader(false);
      }
      window.open(window.URL.createObjectURL(blob));
    }, error => {
      if(controlLoader){
        controlLoader(false);
      }
    });

  }

  // -----------------------------------------------------------------------------------------------------
  // @ Report Public
  // -----------------------------------------------------------------------------------------------------
  // printPublicCertificate(enrollId: any, reportService: ReportPublicService){
  //   const params = new Map<string, string>();
  //   params.set('id', '34');
  //   params.set('P_ENROLL_ID', enrollId);
  //   this.printPublicReport(reportService, params);
  // }


  // printPublicReport(reportService: ReportPublicService,reportParam: Map<string, string>): any {
  //   reportParam.set('reportFormat', 'pdf');
  //   const convMap = {};
  //   reportParam.forEach((val: string, key: string) => {
  //     // @ts-ignore
  //     convMap[key] = val;
  //   });
  //   // get file from server
  //   reportService.printReport(convMap).subscribe(blob => window.open(window.URL.createObjectURL(blob)));
  // }
  // -----------------------------------------------------------------------------------------------------
  // @ Course duration
  // -----------------------------------------------------------------------------------------------------

  getDecimal(number: any, decimal: any): any{
    return Number(number).toFixed(decimal);
  }

  pad(num: number, size: number): string {
    if(num.toString().length < size){
      return '0' + num;
    }else{
      return num.toString();
    }
  }

  randomGenerator(startNumber: number,endNumber:number): number{
    let range : number= endNumber - startNumber + 1;
    return Math.trunc((Math.random()*range)) + startNumber;
  }

  getDateOnly(date: Date): Date {
    console.log(date)
    date.setHours(0, 0, 0);   // Set hours, minutes and seconds
    return date;
  }

  formatTimeFromDate(date: Date): String {
    let hours = date.getHours();
    let minute = date.getMinutes();
    return hours +':'+ minute;
  }

  getTimeFromFormattedTime(timeFormat: string): number{ //12:13
    let times = timeFormat.split(':');
    let time = times[0] + times[1]
    return Number(time); //1213
  }
}
