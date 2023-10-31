import {Injectable} from "@angular/core";

@Injectable({
  providedIn: 'root'
})
export class StatusTypeService{

  public PRESENT = 1;
  public ABSENT = 2;
  public LATE_IN = 3;
  public EARLY_OUT = 4;
  public LATE_IN_EARLY_OUT = 5;
  public LEAVE = 6;
  public CLIENT_VISIT = 7;
  public HOLIDAY = 8;
  public WEEKEND = 9;


  getList(): StatusType[]{
    const list = [];
    list.push(new StatusType(this.PRESENT, 'Present'));
    list.push(new StatusType(this.ABSENT, 'Absent'));
    list.push(new StatusType(this.LATE_IN, 'Late-In'));
    list.push(new StatusType(this.EARLY_OUT, 'Early-Out'));
    list.push(new StatusType(this.LATE_IN_EARLY_OUT, 'L/E'));
    list.push(new StatusType(this.LEAVE, 'On Leave'));
    list.push(new StatusType(this.CLIENT_VISIT, 'Client-Visit'));
    list.push(new StatusType(this.HOLIDAY, 'Holiday'));
    list.push(new StatusType(this.WEEKEND, 'Weekend'));
    return list;
  }
}

export class StatusType {
  id: number;
  name: string;
  constructor(id: number, name: string){
    this.id = id;
    this.name = name;
  }
}
