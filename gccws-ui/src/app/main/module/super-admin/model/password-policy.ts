import {CommonObjectProperty} from "../../../core/model/common-object-property";


export class PasswordPolicy extends CommonObjectProperty{

  name?: string;
  minLength: number;
  sequential?: boolean;
  specialChar?: boolean;
  alphanumeric?: boolean;
  upperLower?: boolean;
  matchUsername?: boolean;
  passwordAge?: number;
  devCode?: number;

}
