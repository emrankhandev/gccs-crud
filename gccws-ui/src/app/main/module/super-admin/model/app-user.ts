import {CommonObjectProperty} from "../../../core/model/common-object-property";


export class AppUser extends CommonObjectProperty{
  username?: string;
  password?: string;
  displayName?: string;
  email?: string;
  mobile?: string;
  passwordPolicyId?: number;
  passwordPolicyName?: string;
  userTypeId?:number;
  userTypeName?:string;

  empPersonalInfoId?: number;
  empPersonalInfoName?: string;

  customerInfoId?: number;
  customerInfoName?: string;

  /*extra*/
  otp: string;

}

