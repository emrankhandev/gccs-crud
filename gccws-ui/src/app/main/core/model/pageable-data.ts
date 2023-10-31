export class PageableData {
  page: number;
  size: number;
  searchValue: string;
  intParam1?: number;
  intParam2: number;
  intParam3: number;
  intParam4: number;
  intParam5: number;
  intParam6: number;
  dateParam1: Date;
  dateParam2: Date;
  listIntParam1: number[] = new Array<number>();

  constructor(page: number, size: number, searchValue: string) {
    this.page = page;
    this.size = size;
    this.searchValue = searchValue;
  }

}
