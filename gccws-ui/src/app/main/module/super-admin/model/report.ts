/**
 * @Author		Md. Mizanur Rahman
 * @Since		  August 30, 2022
 * @version		1.0.0
 */
import {CommonObjectProperty} from "../../../core/model/common-object-property";
import {MenuItem} from "./menu-item";
import {ReportMaster} from "./report-master";

export class Report extends CommonObjectProperty{
  reportMaster?: ReportMaster;
  module?: MenuItem;
  reportFormat?: string;
}
