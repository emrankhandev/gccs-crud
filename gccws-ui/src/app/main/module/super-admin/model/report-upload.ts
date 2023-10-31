import {CommonObjectProperty} from "../../../core/model/common-object-property";

/**
 * @Author		Md. Tawhidul Islam
 * @Since		  September 28, 2022
 * @version		1.0.0
 */



export class ReportUpload extends CommonObjectProperty{
  code?: string;
  fileLocation?: string;
  fileName?: string;
  fileNameParams?: string;
  remarks?: string;
  isSubreport?: boolean;


}
