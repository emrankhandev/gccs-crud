import {CommonObjectProperty} from "../../../core/model/common-object-property";


export class ParameterMaster extends CommonObjectProperty{

  title?: string;
  name?: string;
  banglaName?: string;
  dataType?: string;
  sql?: string;

  childId?: number;
  childName?: string;
  childRelationSql?: string;
}
