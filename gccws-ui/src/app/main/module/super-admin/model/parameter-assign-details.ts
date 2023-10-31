import {CommonObjectProperty} from "../../../core/model/common-object-property";


export class ParameterAssignDetails extends CommonObjectProperty{

  parameterMasterId?: number;
  parameterMasterTitle?: string;
  parameterMasterName?: string;
  parameterMasterDataType?: string;
  parameterMasterSql?: string;
  parameterMasterChildId?: number;
  parameterMasterChildName?: string;
  dropdownListData?: string;
  parameterAssignMasterId?: number;
  parameterAssignMasterName?: string;
  isRequired?: boolean;
  serialNo?: number;
  isDependent?: boolean;
}
