/**
 * @Author		Md. Mizanur Rahman
 * @Since		  August 30, 2022
 * @version		1.0.0
 */
import {CommonObjectProperty} from "../../../core/model/common-object-property";


export class ReportMaster extends CommonObjectProperty{
  reportTitle?: string;
  moduleId?: number;
  moduleName?: string;
  reportUploadId?: number;
  reportUploadFileName?: string;
  reportUploadFileNameJasper?: string;
  serial?: number;
}
