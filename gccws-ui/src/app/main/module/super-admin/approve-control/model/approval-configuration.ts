import {CommonObjectProperty} from "../../../../core/model/common-object-property";

export class ApprovalConfiguration extends CommonObjectProperty{

  departmentId?: number;
  departmentName?: string;

  transactionTypeId?: number;
  transactionTypeName?: string;

  moduleId?: number;
  moduleName?: string;
  serialNo?: number;
  fromTeamId?: number;
  fromTeamName?: string;
  toTeamId?: number;
  toTeamName?: string;
  notifyAppUserId?: number;
  fromAmount?: number;
  toAmount?: number;

  approvalPermission: boolean;
  backPermission: boolean;
  changePermission: boolean;


  // isReadOnly?: boolean;


}

