import {Component, OnInit} from '@angular/core';
import {locale as lngEnglish} from "../i18n/en";
import {locale as lngBangla} from "../i18n/bn";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {SmsFactoryModel} from "../../../../super-admin/model/sms-factory-model";
import {TranslationLoaderService} from "../../../../../core/service/translation-loader.service";
import {SmsFactoryService} from "../../../../super-admin/service/sms-factory.service";
import {AppUtils} from "../../../../../core/utils/app.utils";
import {SmsFactory} from "../../../../super-admin/model/sms-factory";
import {ActivatedRoute} from "@angular/router";
import {DropdownModel} from "../../../../../core/model/dropdown-model";


@Component({
  selector: 'app-sms-dashboard',
  templateUrl: './sms-factory-list.component.html',
  styleUrls: ['./sms-factory-list.component.scss']
})
export class SmsFactoryListComponent implements OnInit{

  /*property*/
  pagePermission: any;
  isEdit: boolean;
  isLoading: boolean = false;

  /*form*/
  frmGroup: FormGroup;
  model: SmsFactoryModel = new SmsFactoryModel();

  /*dropdownList*/
  groupDropdownList: DropdownModel[] = new Array<DropdownModel>();

  /*object*/
  csvMobileNoList: string [] = [];

  /*field control*/
  fileField =true;
  mobileField =true;

  /*extra*/
  courseId: any = null;
  courseUrl: any = null;

  constructor(
    private translationLoaderService: TranslationLoaderService,
    private formBuilder: FormBuilder,
    private modelService: SmsFactoryService,
    public appUtils: AppUtils,
    private route: ActivatedRoute
  ) {
    this.translationLoaderService.loadTranslations(lngEnglish, lngBangla);
  }

  // -----------------------------------------------------------------------------------------------------
  // @ Init
  // -----------------------------------------------------------------------------------------------------

  ngOnInit(): void {
    this.pagePermission = this.appUtils.findUserPagePermission();
    this.setFormInitValue();
  }

  // -----------------------------------------------------------------------------------------------------
  // @ API calling
  // -----------------------------------------------------------------------------------------------------



  onSave(): any {
    this.generateModel();
    if(this.model.detailsList.length == 0){
      this.appUtils.showErrorMessage('মোবাইল নাম্বার প্রয়োজন বা গ্রুপ নির্বাচন করুন', 'Mobile no is required or select Group');
      return;
    }
    this.isLoading = true;
    this.modelService.create(this.model).subscribe({
      next: (res) => {
        this.isLoading = false;
        this.appUtils.onServerResponse(res, this.clearFormData.bind(this));
      },
      error: (error) => {
        this.isLoading = false;
        this.appUtils.onServerErrorResponse(error);
      }
    });
  }

  // -----------------------------------------------------------------------------------------------------
  // @ view method
  // -----------------------------------------------------------------------------------------------------

  clearFormData(): any {
    this.setFormInitValue();
    this.isEdit = false;
    this.fileField = true;
    this.mobileField = true;
    this.csvMobileNoList = [];
  }

  /*file*/
  onFileSelect(event: any): any {
    if (event.target.files.length > 0) {
      const file = event.target.files[0];
      const reader = new FileReader();
      reader.readAsText(file);
      reader.onload = () => {
        const csvData = reader.result;
        const csvRecordsArray : string [] = ( csvData as string ).split(/\r\n|\n/);
        const headersRow = this.getHeaderArray(csvRecordsArray);
        this.getDataRecordsArrayFromCSVFile(csvRecordsArray, headersRow.length);
      };
      reader.onerror = () => {
        this.appUtils.showErrorMessage('not able to process csv file', 'not able to process csv file');
      };
    }
  }



  // -----------------------------------------------------------------------------------------------------
  // @ Helper Method
  // -----------------------------------------------------------------------------------------------------

  setFormInitValue(): any {
    this.frmGroup = this.formBuilder.group({
      smsType: ['Text', ''],
      group: ['', ''],
      file: ['', Validators.pattern('^.+\\.(csv)$')],
      mobile: ['', ''],
      sms: ['', Validators.required],
    });
  }

  generateModel(): any{
    const smsType: string = this.frmGroup.value.smsType;
    const mobile: string = this.frmGroup.value.mobile;
    const sms: string = this.frmGroup.value.sms;
    const mobileNos : string [] = mobile.split(',');
    const detailsList: SmsFactory[] = [];

    if(this.frmGroup.value.smsType == 'Text'){
      this.model.detailsList = this.frmGroup.value.smsType;
    }
    if(this.frmGroup.value.smsType == 'Unicode'){
      this.model.detailsList = this.frmGroup.value.smsType;
    }

    mobileNos.forEach(mobileNo => {
      if (mobileNo){
        detailsList.push(this.getSmsFactory(smsType, mobileNo, sms));
      }
    });
    this.csvMobileNoList.forEach(mobileNo => {
      detailsList.push(this.getSmsFactory(smsType, mobileNo, sms));
    });
    this.model.detailsList = detailsList;
  }

  getSmsFactory(smsType: string, mobileNo: string, sms: string): SmsFactory{
    const smsFactory: SmsFactory = new SmsFactory();
    smsFactory.smsType = smsType;
    smsFactory.mobileNo = this.mobileNoCheck(mobileNo);/*check first char 0 or not*/
    smsFactory.smsText = sms;
    return smsFactory;
  }

  mobileNoCheck(mobileNo: string): string{
    const firstChar = Array.from(mobileNo)[0];
    return firstChar === '0' ? mobileNo : '0'+mobileNo;
  }

  /*csv file helper*/
  getHeaderArray(csvRecordsArr: any): any {
    const headers = (csvRecordsArr[0] as string).split(',');
    const headerArray = [];
    for (const j of headers) {
      headerArray.push(j);
    }
    return headerArray;
  }

  getDataRecordsArrayFromCSVFile(csvRecordsArray: string [], headerLength: any): void {
    this.csvMobileNoList = [];
    csvRecordsArray.forEach((value, index) => {
      const mainRec = this.csvToArray(value);
      const currentRecord = mainRec[0];
      if (currentRecord.length === headerLength) {
        const value = currentRecord[0].trim();
        if (index != 0 && value){
          this.csvMobileNoList.push(value)
        }
      }
    });
  }

  csvToArray(text: any): any {
    let p = '';
    let row = [''];
    const ret = [row];
    let i = 0;
    let r = 0;
    let s = !0;
    for (let l of text) {
      if ('"' === l) {
        if (s && l === p) {
          row[i] += l;
        }
        s = !s;
      } else if (',' === l && s) {
        l = row[++i] = '';
      }
      else if ('\n' === l && s) {
        if ('\r' === p) {
          row[i] = row[i].slice(0, -1);
        }
        row = ret[++r] = [l = '']; i = 0;
      } else {
        row[i] += l;
      }

      p = l;
    }
    return ret;
  }

}
