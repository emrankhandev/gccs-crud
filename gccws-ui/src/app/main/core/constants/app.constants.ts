import {Injectable} from '@angular/core';

@Injectable({
    providedIn: 'root'
})
export class AppConstants {

  /*module*/
  moduleSuperAdmin: string = 'sya/';
  moduleHr: string = 'hr/';
  modulePayroll: string = 'payroll/';
  moduleAccounts: string = 'accounts/';
  moduleBilling: string = 'billing/';
  moduleBudget: string = 'budget/';
  moduleBiman: string = 'biman/';
  moduleInventory :string ='inventory/'

  moduleCommon: string = 'common/';

  /*pageable*/
  DEFAULT_ARRAY: number[] = [5, 10, 25, 100];
  DEFAULT_SIZE: number = 10;
  DEFAULT_PAGE: number = 0;

  /*field*/
  DEFAULT_TEXT_AREA_SIZE = 250;

  /*add dialog*/

    constructor(
    ) {
    }

}
