import {CommonObjectProperty} from "../../../core/model/common-object-property";

export class UserRoleDetails extends CommonObjectProperty{

  menuItemId?: number;
  menuItemName?: string;
  insert?: boolean;
  edit?: boolean;
  delete?: boolean;
  approve?: boolean;
  view?: boolean;

}

