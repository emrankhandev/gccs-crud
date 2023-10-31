import {CommonObjectProperty} from "../../../core/model/common-object-property";


export class MenuItem extends CommonObjectProperty{

  name?: string;
  banglaName?: string;
  menuType?: number;
  menuTypeName?: string;
  serialNo?: number;
  url?: string;
  icon?: string;
  parentId?: number;
  reportUploadId?:number;
  reportUploadName?:string;
  insert?: boolean;
  edit?: boolean;
  delete?: boolean;
  approve?: boolean;
  view?: boolean;
  devCode?: number;

}
