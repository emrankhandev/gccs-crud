import {CommonObjectProperty} from "../../../core/model/common-object-property";
import {ParameterAssignMaster} from "./parameter-assign-master";
import {ParameterAssignDetails} from "./parameter-assign-details";


export class ParameterAssignModel extends CommonObjectProperty{

  master: ParameterAssignMaster;
  detailsList: ParameterAssignDetails[];
  index: number

}
