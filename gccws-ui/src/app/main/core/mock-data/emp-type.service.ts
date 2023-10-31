import {Injectable} from "@angular/core";

@Injectable({
  providedIn: 'root'
})
export class EmpTypeService{

  public OFFICER = 1;
  public STAFF = 2;

  getList(): EmpType[]{
    const list = [];
    list.push(new EmpType(this.OFFICER, 'Officer'));
    list.push(new EmpType(this.STAFF, 'Staff'));
    return list;
  }
}

export class EmpType {
  id: number;
  name: string;
  constructor(id: number, name: string){
    this.id = id;
    this.name = name;
  }
}
