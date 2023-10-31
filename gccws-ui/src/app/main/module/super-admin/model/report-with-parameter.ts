/**
 * @Author		Md. Mizanur Rahman
 * @Since		  August 30, 2022
 * @version		1.0.0
 */
import {CommonObjectProperty} from "../../../core/model/common-object-property";

export class ReportWithParameter extends CommonObjectProperty{
  reportMasterId?: number;
  reportMasterReportTitle?: string;
  parameterMasterId?: number;
  parameterMasterParameterTitle?: string;
  parameterMasterParameterName?: string;
  parameterMasterDataType?: string;
  parameterMasterSql?: string;
  dropdownListData?: string;
  serial?: number;
  required?: boolean;
}
