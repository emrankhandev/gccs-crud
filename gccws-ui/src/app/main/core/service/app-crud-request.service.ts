import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {CommonResponseList, CommonResponseObject, CommonResponsePageable} from "../model/common-response";
import {DropdownModel} from "../model/dropdown-model";
import {PageableData} from "../model/pageable-data";


export abstract class AppCrudRequestService<I> {

    protected constructor(protected httpClient: HttpClient,
                          protected _BASE_URL: string) {
    }

    // common
    create(i: I): Observable<CommonResponseObject<I>> {
      return this.httpClient.post<CommonResponseObject<I>>( this._BASE_URL, i);
    }

    update(i: I): Observable<CommonResponseObject<I>> {
        return this.httpClient.put<CommonResponseObject<I>>( this._BASE_URL, i);
    }

    delete(i: I): Observable<CommonResponseObject<I>> {
        const httpOptions = {
            headers: new HttpHeaders({ 'Content-Type': 'application/json' }), body: i
        };
        return this.httpClient.delete<CommonResponseObject<I>>( this._BASE_URL, httpOptions);
    }

   getObjectById(id: number): Observable<CommonResponseObject<I>> {
    return this.httpClient.get<CommonResponseObject<I>>( this._BASE_URL + '/' + 'get-by-id' + '/' + id);
   }

  getDropdownList(): Observable<CommonResponseList<DropdownModel>> {
    return this.httpClient.get<CommonResponseList<DropdownModel>>( this._BASE_URL + '/' + 'dropdown-list');
  }

  getListWithPaginationData(pageableData : PageableData): Observable<CommonResponsePageable<I>> {
    return this.httpClient.put<CommonResponsePageable<I>>( this._BASE_URL + '/' + 'pageable-data' , pageableData);
  }

  /*utils*/
  filterSearchValue(searchValue: string): string{
    searchValue = searchValue.replace(/[&\/\\#,+()$~%.'":*?<>{}]/g, '');
    searchValue = searchValue ? searchValue : '0';
    return searchValue;
  }


}
