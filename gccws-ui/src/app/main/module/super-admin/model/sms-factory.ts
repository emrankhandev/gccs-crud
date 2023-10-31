import {CommonObjectProperty} from "../../../core/model/common-object-property";


export class SmsFactory extends CommonObjectProperty{

  entryDate?: Date;

  mobileNo?: string;

  smsText?: string;

  smsType?: string;

  /* after response */
  MessageId?: string;

  Status?: string;

  StatusText?: string;

  ErrorCode?: string;

  ErrorText?: string;

  CurrentCredit?: string

  /*extra*/

}

