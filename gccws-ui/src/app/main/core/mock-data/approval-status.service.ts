import {Injectable} from "@angular/core";

@Injectable({
  providedIn: 'root'
})
export class ApprovalStatusService {

  public SUBMIT = 1;
  public FORWARD = 2;
  public BACK = 3;
  public APPROVED = 4;
  public REJECT = 5;

  public SUBMIT_NAME = 'Submit';
  public FORWARD_NAME = 'Forward';
  public BACK_NAME = 'Back';
  public APPROVED_NAME = 'Approved';
  public REJECT_NAME = 'Reject';

  getList(): ApprovalStatus[]{
    const list = [];
    list.push(new ApprovalStatus(this.SUBMIT, this.SUBMIT_NAME));
    list.push(new ApprovalStatus(this.FORWARD, this.FORWARD_NAME));
    list.push(new ApprovalStatus(this.BACK, this.BACK_NAME));
    list.push(new ApprovalStatus(this.APPROVED, this.APPROVED_NAME));
    list.push(new ApprovalStatus(this.REJECT, this.REJECT_NAME));
    return list;
  }
}

export class ApprovalStatus {
  id: number;
  name: string;
  constructor(id: number, name: string) {
    this.id = id;
    this.name = name;
  }
}
