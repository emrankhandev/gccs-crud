 import {Injectable} from '@angular/core';

@Injectable({
    providedIn: 'root'
})
export class MenuTypeService {

    public MODULE_ID = 1;
    //public MENU_GROUP_ID = 2;
    public MENU_ID = 3;
    public REPORT_ID = 4;

    getList(): MenuType[]{
        const list = [];
        list.push(new MenuType(this.MODULE_ID, 'Module'));
        //list.push(new MenuType(this.MENU_GROUP_ID, 'Menu Group'));
        list.push(new MenuType(this.MENU_ID, 'Menu'));
        list.push(new MenuType(this.REPORT_ID, 'Report'));
        return list;
    }
}

export class MenuType {
    id: number;
    name: string;
    constructor(id: number, name: string){
        this.id = id;
        this.name = name;
    }
}
