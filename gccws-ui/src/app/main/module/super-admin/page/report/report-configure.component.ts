import {Component, OnInit} from '@angular/core';
import {MatDialog} from "@angular/material/dialog";
import {locale as lngEnglish} from './i18n/en';
import {locale as lngBangla} from './i18n/bn';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Report} from "../../model/report";
import {MenuItem} from "../../../super-admin/model/menu-item";
import {ParameterAssignDetails} from "../../../super-admin/model/parameter-assign-details";
import {TranslationLoaderService} from "../../../../core/service/translation-loader.service";
import {AppUtils} from "../../../../core/utils/app.utils";
import {AppConstants} from "../../../../core/constants/app.constants";
import {ParameterMasterService} from "../../../super-admin/service/parameter-master.service";
import {MenuItemService} from "../../../super-admin/service/menu-item.service";
import {ReportService} from "../../service/report.service";
import {MenuTypeService} from "../../../../core/mock-data/menu-type.service";
import {LocalStoreUtils} from "../../../../core/utils/local-store.utils";
import {saveAs as importedSaveAs} from "file-saver";

/**
 * @Author		Md. Mizanur Rahman
 * @Since		  August 30, 2022
 * @version		1.0.0
 */
@Component({
  selector: 'app-report-configure',
  templateUrl: './report-configure.component.html',
  styleUrls: ['./report-configure.component.scss']
})

export class ReportConfigureComponent implements OnInit {

  /*property*/
  pagePermission: any;
  userId: number;
  appUser: any;
  isLoadingPdf: boolean = false;
  isLoadingDownload: boolean = false;
  /*form*/
  frmGroup: FormGroup;
  model: Report = new Report();

  // dropdown
  reportMasterDropdownList: MenuItem[] = new Array<MenuItem>();
  reportWithParameterList: ParameterAssignDetails[] = new Array<ParameterAssignDetails>();
  moduleDropdownList: MenuItem[] = new Array<MenuItem>();

  constructor(
    private formBuilder: FormBuilder,
    private matDialog: MatDialog,
    private translationLoaderService: TranslationLoaderService,
    private modelService: ReportService,
    private parameterMasterService: ParameterMasterService,
    private menuItemService: MenuItemService,
    private menuTypeService: MenuTypeService,
    private localStoreUtils : LocalStoreUtils,
    private appConstants : AppConstants,
    private appUtils : AppUtils,
  ) {
    this.translationLoaderService.loadTranslations(lngEnglish, lngBangla);
  }

  // -----------------------------------------------------------------------------------------------------
  // @ Init
  // -----------------------------------------------------------------------------------------------------

  ngOnInit(): void {
    //this.pagePermission = this.appUtils.findUserPagePermission();
    this.getUserId();
    this.setFormInitValue();
    this.getModuleList();
  }

  getUserId(): any {
    this.userId = 0;
    this.appUser = this.localStoreUtils.getUserInfo();
    if(this.appUser){
      this.userId = this.appUser.id ? this.appUser.id: 0;
    }
  }


  // -----------------------------------------------------------------------------------------------------
  // @ API calling
  // -----------------------------------------------------------------------------------------------------

  getModuleList(){
    this.menuItemService.getReportModuleByAppUser().subscribe(res => {
      this.moduleDropdownList = res.data;
    });
  }

  getReportMasterList(): void {
    const module = this.frmGroup.value.module === '' ? null : this.frmGroup.value.module;
    if(module){
      this.menuItemService.getReportByModuleIdAndAppUser(module.id).subscribe(res => {
        this.reportMasterDropdownList = res.data;
      });
    }else{
      this.frmGroup.patchValue({
        reportMaster: ''
      });
      this.generateComponent();
    }
  }

  selectModuleChange(): void {
    this.reportMasterDropdownList = [];
    this.reportWithParameterList = [];
    this.getReportMasterList();
  }

  refresh(): void{
    this.selectReportChange();
  }
  // -----------------------------------------------------------------------------------------------------
  // @ view method
  // -----------------------------------------------------------------------------------------------------


  setFormInitValue(): void {
    this.frmGroup = this.formBuilder.group({
      reportMaster: ['', Validators.required],
      module: ['', Validators.required],
    });
  }

  // -----------------------------------------------------------------------------------------------------
  // @ Utils
  // -----------------------------------------------------------------------------------------------------

  generateModel(isCreate: boolean): void {
    if (isCreate) {
      this.model.id = undefined;
    }
    this.model.reportMaster = this.frmGroup.value.reportMaster;
  }


  selectReportChange(): void {
    const id = this.frmGroup.value.reportMaster.devCode;
    if (id == null || id == '' || id == undefined) {
      this.reportWithParameterList = [];
      this.generateComponent();
    } else {
      this.modelService.getParamDataByDevCode(this.frmGroup.value.reportMaster.devCode).subscribe(res => {
        this.reportWithParameterList = res.data;
        this.generateComponent();
      });
    }
  }

  generateComponent(): void {
    var html = '<div style="height: auto; width: 100%; margin-top: 5px;"><form>';
    if (this.reportWithParameterList.length > 0) {
      html += '<p style="padding-buttom: 5px; font-size: 14px; font-weight: bold;">Parameters:</p>';
      this.reportWithParameterList.forEach(e => {
        const required = e.isRequired ? '<i style="color: red">*</i>' : '';
        if (e.parameterMasterDataType?.toUpperCase() === 'STRING') {
          html += '<div style="height: auto; width: 24%; float: left; margin-right: 10px; padding-top: 0px; "><lebel style="margin-buttom: 2px;">' + e.parameterMasterTitle + '</lebel>' + required + '</br>'
            + '<input type="text" id="' + e.parameterMasterName + '" name="' + e.parameterMasterName + '" value="" style="height: 30px; width: 100%; border: 1px solid; border-radius: 5px; padding: 10px; "></div>';
        }
        if (e.parameterMasterDataType?.toUpperCase() == 'INTEGER') {
          html += '<div style="height: auto; width: 24%; float: left; margin-right: 10px; padding-top: 0px;"><lebel style="margin-buttom: 2px; ">' + e.parameterMasterTitle + '</lebel>' + required + '</br>'
            + '<input type="number" min="0" id="' + e.parameterMasterName + '" name="' + e.parameterMasterName + '" value="" style="height: 30px; width: 100%; border: 1px solid; border-radius: 5px; padding: 10px; "></div>';
        }
        if (e.parameterMasterDataType?.toUpperCase() == 'DATE') {
          html += '<div style="height: auto; width: 24%; float: left; margin-right: 10px; padding-top: 0px;"><lebel style="margin-buttom: 2px; ">' + e.parameterMasterTitle + '</lebel>' + required + '</br>'
            + '<input type="date" id="' + e.parameterMasterName + '" name="' + e.parameterMasterName + '" value="" style="height: 30px; width: 100%; border: 1px solid; border-radius: 5px; padding: 10px; "></div>';
        }
        if (e.parameterMasterDataType?.toUpperCase() == 'LIST') {
          html += '<div style="height: auto; width: 24%; float: left; margin-right: 10px; padding-top: 0px;"><lebel style="margin-buttom: 2px; ">' + e.parameterMasterTitle + '</lebel>' + required + '</br>'
            + '<select data-dropdown id="' + e.parameterMasterName + '" name="' + e.parameterMasterName + '" style="height: 30px; width: 100%;  border: 1px solid; border-radius: 5px; padding: 10px; ">'
            + '<option value="0">Select One</option>'
            + e.dropdownListData
            + '</select></div>';
        }
      });
    }
    html += '</form></div>';
    // @ts-ignore
    document.getElementById('htmlParamsBody').innerHTML = html;

    // custom dropdown and event
    const dropdowns = document.querySelectorAll('[data-dropdown]');
    if (dropdowns.length > 0) {
      dropdowns.forEach(dropdown => {
        this.createCustomDropdown(dropdown);
      });
    }
  }

  printReport(): any {
    let params = new Map<string, string>();
    params.set('id', this.frmGroup.value.reportMaster.devCode);
    params.set('reportFormat', 'pdf');
    let form = document.querySelector('form');
    if (this.reportWithParameterList.length > 0) {

      // validation
      if (this.isParameterValid(this.reportWithParameterList)) {
        this.appUtils.showErrorMessage('অনুগ্রহ করে প্রয়োজনীয় প্যারামিটারে মান যোগ করুন', 'Please add value in required parameters');
        return;
      }

      // set paeams value
      this.reportWithParameterList.forEach(e => {
        if (e.parameterMasterDataType?.toUpperCase() == 'LIST') {
          // @ts-ignore
          params.set(e.parameterMasterName!, (<HTMLFormElement> form.querySelector('[name="' + e.parameterMasterName + '"]')).value);
        }else{
          // @ts-ignore
          params.set((<HTMLInputElement> document.getElementById(e.parameterMasterName)).name, (<HTMLInputElement> document.getElementById(e.parameterMasterName)).value);
        }
      });
    }

    // set params value to angular map
    const convMap = {};
    params.forEach((val: string, key: string) => {
      // @ts-ignore
      convMap[key] = val;
    });
    console.error(convMap);

    // get file from server
    const fileName = this.frmGroup.value.reportMaster.name + '.pdf';
    this.isLoadingPdf = true;
    this.modelService.printReport(convMap).subscribe(blob => {
      this.isLoadingPdf = false;
      window.open(window.URL.createObjectURL(blob));

    }, error => {
      this.isLoadingPdf = false;
    });
  }

  downloadReport(): any {
    let params = new Map<string, string>();
    params.set('id', this.frmGroup.value.reportMaster.devCode);
    params.set('reportFormat', 'pdf');
    let form = document.querySelector('form');
    if (this.reportWithParameterList.length > 0) {

      // validation
      if (this.isParameterValid(this.reportWithParameterList)) {
        this.appUtils.showErrorMessage('অনুগ্রহ করে প্রয়োজনীয় প্যারামিটারে মান যোগ করুন', 'Please add value in required parameters');
        return;
      }

      // set paeams value
      this.reportWithParameterList.forEach(e => {
        if (e.parameterMasterDataType?.toUpperCase() == 'LIST') {
          // @ts-ignore
          params.set(e.parameterMasterName!, (<HTMLFormElement> form.querySelector('[name="' + e.parameterMasterName + '"]')).value);
        }else{
          // @ts-ignore
          params.set((<HTMLInputElement> document.getElementById(e.parameterMasterName)).name, (<HTMLInputElement> document.getElementById(e.parameterMasterName)).value);
        }
      });
    }

    // set params value to angular map
    const convMap = {};
    params.forEach((val: string, key: string) => {
      // @ts-ignore
      convMap[key] = val;
    });
    console.error(convMap);

    // get file from server
    const fileName = this.frmGroup.value.reportMaster.name + '.xlsx';
    this.isLoadingDownload = true;
    this.modelService.downloadReport(convMap).subscribe(blob => {
      this.isLoadingDownload = false;
      importedSaveAs(blob, fileName);
    }, error => {
      this.isLoadingDownload = false;
    });
  }

  isParameterValid(paramList: any): boolean {

    for (const param of paramList) {
      if (param.isRequired) {
        const paramValue = (<HTMLInputElement> document.getElementById(param.parameterMasterName)).value;
        if (paramValue === '' || paramValue === '0' || paramValue == null) {
          return true;
        }
      }
    }
    return false;
  }

  // Create custom dropdown
  createCustomDropdown(dropdown: any): any {
    const options = dropdown.querySelectorAll('option');
    const optionsArr = Array.prototype.slice.call(options);

    const customDropdown = document.createElement('div');
    customDropdown.id = 'CUSTOM-'+ dropdown.name;
    customDropdown.classList.add('dropdown');
    customDropdown.style.position = 'relative';
    customDropdown.style.height = '30px';
    customDropdown.style.border = '1px solid';
    customDropdown.style.borderRadius = '5px';
    customDropdown.style.padding = '5px';
    customDropdown.addEventListener('mouseover', () => {
      customDropdown.style.cursor = 'pointer';
    });
    dropdown.insertAdjacentElement('afterend', customDropdown);

    const selected = document.createElement('div');
    selected.classList.add('dropdown__selected');
    selected.textContent = optionsArr[0].textContent;
    customDropdown.appendChild(selected);

    const menu = document.createElement('div');
    menu.classList.add('dropdown__menu');
    menu.style.display = 'none';
    menu.style.position = 'absolute';
    menu.style.top = '100%';
    menu.style.marginTop = '-45px';
    menu.style.left = '0';
    menu.style.width = '100%';
    menu.style.height = 'auto';
    menu.style.backgroundColor = '#fff';
    menu.style.zIndex = '5';
    menu.style.boxShadow = '0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19)';
    customDropdown.appendChild(menu);
    selected.addEventListener('click',  () => {
      if (menu.offsetParent !== null) {
        menu.style.display = 'none';
      }else {
        menu.style.display = 'block';
        // @ts-ignore
        menu.querySelector('input').focus();
      }
    });

    const search = document.createElement('input');
    search.placeholder = 'Search...';
    search.type = 'text';
    search.classList.add('dropdown__menu_search');
    search.style.display = 'block';
    search.style.width = '100%';
    search.style.border = '2px solid';
    search.style.padding = '10px';
    search.style.outline = '0';
    menu.appendChild(search);

    const menuItemsWrapper = document.createElement('div');
    menuItemsWrapper.classList.add('dropdown__menu_items');
    menuItemsWrapper.style.maxHeight = '210px';
    menuItemsWrapper.style.overflowY = 'auto';
    menu.appendChild(menuItemsWrapper);

    optionsArr.forEach(option => {
      const item = document.createElement('div');
      item.classList.add('dropdown__menu_item');
      item.style.padding = '10px';
      item.style.cursor = 'pointer';
      // @ts-ignore
      item.dataset.value = option.value;
      item.textContent = option.textContent;
      menuItemsWrapper.appendChild(item);

      item.addEventListener('click', () => {
        // @ts-ignore
        const value = item.dataset.value;
        const label = item.textContent;

        selected.textContent = label;
        dropdown.value = value;

        this.changeCustomListValue(dropdown);

        menu.style.display = 'none';
        // @ts-ignore
        menu.querySelector('input').value = '';
        menu.querySelectorAll('div').forEach(div => {
          if (div.classList.contains('selected')) {
            div.classList.remove('selected');
          }
          if (div.offsetParent === null) {
            div.style.display = 'block';
          }
        });
        item.classList.add('selected');
      });

      item.addEventListener('mouseover', () => {
        item.style.backgroundColor = '#F5F5F5';
      });

      item.addEventListener('mouseleave', () => {
        item.style.backgroundColor = '#fff';
      });

    });

    // @ts-ignore
    menuItemsWrapper.querySelector('div').classList.add('selected');
    search.addEventListener('input', () => {
      const customOptions = menu.querySelectorAll('.dropdown__menu_items div');
      const value = search.value.toLowerCase();
      const filteredItems = optionsArr.filter(item => item.innerHTML.toLowerCase().includes(value));
      const indexesArr = filteredItems.map(item => optionsArr.indexOf(item));

      optionsArr.forEach(option => {
        if (!indexesArr.includes(optionsArr.indexOf(option))) {
          (<HTMLDivElement> customOptions[optionsArr.indexOf(option)]).style.display = 'none';
        }else {
          if ((<HTMLDivElement> customOptions[optionsArr.indexOf(option)]).offsetParent === null) {
            (<HTMLDivElement> customOptions[optionsArr.indexOf(option)]).style.display = 'block';
          }
        }
      });
    });
    document.addEventListener('click', this.closeIfClickedOutside.bind(customDropdown, menu));
    dropdown.style.display = 'none';
  }

  // Close dropdown if clicked outside dropdown element
  // @ts-ignore
  closeIfClickedOutside(menu, e): any {
    if (e.target.closest('.dropdown') === null && e.target !== this && menu.offsetParent !== null) {
      menu.style.display = 'none';
    }
  }

  changeCustomListValue(parentDropdown: any){
    for(let i = 0; i < this.reportWithParameterList.length; i++){
      let obj = this.reportWithParameterList[i];
      if(obj.isDependent){
        if(parentDropdown.name == obj.parameterMasterName){
          this.modelService.getParamChildListData(obj.parameterMasterId!, parentDropdown.value).subscribe(res => {
            const childDropdown = document.querySelector('[name="' + obj.parameterMasterChildName + '"]');
            let childSelect = '<select data-dropdown id="' + obj.parameterMasterChildName + '" name="' + obj.parameterMasterChildName + '" style="height: 30px; width: 100%;  border: 1px solid; border-radius: 5px; padding: 10px; "><option value="0">Select One</option>' + res.data + '</select>';
            // @ts-ignore
            childDropdown.innerHTML = childSelect;
            const previousChildDropdownElement = document.getElementById("CUSTOM-" + obj.parameterMasterChildName);
            // @ts-ignore
            previousChildDropdownElement.remove();

            this.createCustomDropdown(childDropdown);
          });
        }
      }
    }
  }

  // END custom dropdown

}
