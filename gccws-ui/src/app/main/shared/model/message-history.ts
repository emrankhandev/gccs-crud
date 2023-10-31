import {CommonObjectProperty} from "../../core/model/common-object-property";

/**
 * @author	Md. Mizanur Rahman
 * @Email	  kmmizanurrahmanjp@gmail.com
 * @since	  March 14,2023
 * @version 1.0
 */

export class MessageHistory extends CommonObjectProperty{

  moduleId: number;
  moduleName: string;
  transactionId: number;
  transactionTable: string;
  transactionType: string;
  message: string;
  attachment: string;
  attachmentLocation: string;
  isRead: boolean;
  readDate: Date;
  isClose: boolean;
  isAction: boolean;
  link: string;
  authorityId: number;
  authorityName: string;
  receivedUserId: number;
  receivedUserName: string;
  publishDate: Date;
  description: string;
}
