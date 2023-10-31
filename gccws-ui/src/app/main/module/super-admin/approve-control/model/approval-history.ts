import {CommonObjectProperty} from "../../../../core/model/common-object-property";

export class ApprovalHistory extends CommonObjectProperty{

  departmentId?: number;
  departmentName?: string;
  approvalTransactionTypeId?: number;
  approvalTransactionTypeName?: string;
  transactionId?: number;
  transactionTypeId?: number;
  transactionTypeName?: string;
  transactionTableName?: string;
  transactionDescription?: string;
  // routerUrl?: string;
  fromTeamId?: number;
  fromTeamName?: string;
  toTeamId?: number;
  toTeamName?: string;
  fromAppUserId?: number;
  fromAppUserName?: string;
  defaultAppUserId?: number;
  defaultAppUserName?: string;
  approvalStatusId?: number;
  approvalStatusName?: string;
  totalAmount?: number;
  approvalComment?: string;
  isSeen?: boolean;
  seenDate?: Date;
  isClose?: boolean;
  isCross?: boolean;
  entryDate: Date;

  // extra UI property
  iconName: string;
  detailText: string;
  iconColor: string;
  fromHeading: string;


}

