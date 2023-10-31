import {Injectable} from "@angular/core";
import {EmpType} from "./emp-type.service";

@Injectable({
  providedIn: 'root'
})
export class EmpCategoryTypeService{

  public PAYRA_PORT = 1;
  public DEPUTATION = 2;
  public NAVY_OFFICER = 3;

  getList(): EmpCategoryType[]{
    const list = [];
    list.push(new EmpType(this.PAYRA_PORT, 'Payra Port'));
    list.push(new EmpType(this.DEPUTATION, 'Deputation'));
    list.push(new EmpType(this.NAVY_OFFICER, 'Navy Officer'));
    return list;
  }
}

export class EmpCategoryType {
  id: number;
  name: string;
  constructor(id: number, name: string){
    this.id = id;
    this.name = name;
  }
}
