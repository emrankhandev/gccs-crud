import {Injectable} from "@angular/core";

@Injectable({
  providedIn: 'root'
})
export class NotificationTypeService{

  public VESSEL_DECLARATION = 1;
  public SHIPPING_AGENT_SERVICE_REQUEST = 2;
  public BILL_SUBMIT_MARINE_TO_ACCOUNT = 3;
  public NOC_APPLICATION = 4;
  public NOC_APPLICATION_APPROVE = 5;
  public AGENT_REGISTRATION = 6;
  public BILL_SUBMIT_TO_SHIPPING_AGENT = 7;
  public SHIPPING_AGENT_BILL_SUBMIT_TO_ACCOUNT = 8;
  public POST_JOURNAL_TO_MARINE_AND_AGENT = 9;
  public POST_JOURNAL_TO_TRAFFIC_AND_AGENT = 10;
  public C_AND_F_AGENT_REQUEST = 11;
  public C_AND_F_AGENT_REQUEST_APPROVE = 12;
  public BILL_SUBMIT_TO_C_AND_F_AGENT = 13;
  public C_AND_F_AGENT_BILL_SUBMIT_TO_ACCOUNT = 14;


  getList(): NotificationType[]{
    const list = [];
    list.push(new NotificationType(this.AGENT_REGISTRATION, 'Agent Registration'));
    list.push(new NotificationType(this.VESSEL_DECLARATION, 'Vessel Declaration'));
    list.push(new NotificationType(this.SHIPPING_AGENT_SERVICE_REQUEST, 'Shipping Agent Service Request'));
    list.push(new NotificationType(this.BILL_SUBMIT_MARINE_TO_ACCOUNT, 'Bill Submit to Account'));
    list.push(new NotificationType(this.NOC_APPLICATION, 'Noc Application'));
    list.push(new NotificationType(this.NOC_APPLICATION_APPROVE, 'Noc Application Approve'));
    list.push(new NotificationType(this.BILL_SUBMIT_TO_SHIPPING_AGENT, 'Bill Submit To Shipping Agent'));
    list.push(new NotificationType(this.SHIPPING_AGENT_BILL_SUBMIT_TO_ACCOUNT, 'Shipping Agent Bill Submit To Account'));
    list.push(new NotificationType(this.POST_JOURNAL_TO_MARINE_AND_AGENT, 'Post Journal To Marine And Agent'));
    list.push(new NotificationType(this.POST_JOURNAL_TO_TRAFFIC_AND_AGENT, 'Post Journal To Traffic And Agent'));
    list.push(new NotificationType(this.C_AND_F_AGENT_REQUEST, 'C&F Agent Request'));
    list.push(new NotificationType(this.C_AND_F_AGENT_REQUEST_APPROVE, 'C&F Agent Request Approve'));
    list.push(new NotificationType(this.BILL_SUBMIT_TO_C_AND_F_AGENT, 'Bill Submit To C&F Agent'));
    list.push(new NotificationType(this.C_AND_F_AGENT_BILL_SUBMIT_TO_ACCOUNT, 'C&F Agent Bill Submit To Account'));
    return list;
  }

}

export class NotificationType {
  id: number;
  name: string;
  constructor(id: number, name: string){
    this.id = id;
    this.name = name;
  }
}
