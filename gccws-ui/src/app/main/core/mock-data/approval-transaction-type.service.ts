import {Injectable} from "@angular/core";

@Injectable({
  providedIn: 'root'
})
export class ApprovalTransactionTypeService {

  public JOURNAL_ENTRY = 1;
  public SALARY_APPROVAL=2;
  public BONUS_APPROVAL=3;



  getList(): ApprovalTransactionType[]{
    const list = [];
   list.push(new ApprovalTransactionType(this.JOURNAL_ENTRY, 'Journal Entry',  'ACC_JOURNAL_ENTRY', '/abc/xyz'));
    list.push(new ApprovalTransactionType(this.SALARY_APPROVAL, 'Salary Approval',  'PAYROLL_SALARY_APPROVAL', '/abc/xyz'));
    list.push(new ApprovalTransactionType(this.BONUS_APPROVAL, 'Bonus Approval',  'PAYROLL_BONUS_APPROVAL', '/abc/xyz'));

    return list;
  }
}

export class ApprovalTransactionType {
  id: number;
  name: string;
  transactionId: number;
  tableName: string;
  transactionDescription: string;
  routerUrl: string;
  totalAmount: number;
  approvalHistoryId: number;
  constructor(id: number, name: string, tableName: string, routerUrl: string) {
    this.id = id;
    this.name = name;
    this.tableName = tableName;
    this.routerUrl = routerUrl;
  }
}
