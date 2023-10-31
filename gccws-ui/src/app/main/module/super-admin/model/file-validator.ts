import {CommonObjectProperty} from "../../../core/model/common-object-property";

export class FileValidator extends CommonObjectProperty{
  name: string;
  fileExtensions: any;
  fileSize: number;
  fileHeight: number;
  fileWidth: number;
  isFixed: boolean;
}
