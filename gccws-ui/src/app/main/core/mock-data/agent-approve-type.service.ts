import {Injectable} from "@angular/core";

@Injectable({
  providedIn: 'root'
})
export class AgentApproveTypeService{

  public PENDING = 1;
  public CONFIRM = 2;
  public REJECT = 3;
  public IN_PROGRESS = 4;

  getList(): AgentApproveType[]{
    const list = [];
    list.push(new AgentApproveType(this.PENDING, 'Pending'));
    list.push(new AgentApproveType(this.IN_PROGRESS, 'In Progress'));
    list.push(new AgentApproveType(this.CONFIRM, 'Approved'));
    list.push(new AgentApproveType(this.REJECT, 'Reject'));
    return list;
  }
}

export class AgentApproveType {
  id: number;
  name: string;
  constructor(id: number, name: string){
    this.id = id;
    this.name = name;
  }
}
