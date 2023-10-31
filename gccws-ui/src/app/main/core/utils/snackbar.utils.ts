import {Injectable} from '@angular/core';
import {MatSnackBar} from '@angular/material/snack-bar';

@Injectable({
    providedIn: 'root'
})
export class SnackbarHelper {

  /*property*/
  TEXT_SERVER_ERROR: string = 'Failed to complete transaction!!';
  TEXT_OK: string = 'OK';
  TEXT_OK_BN: string = 'ওকে';
  NO_DATA: string = 'No Data Found !';
  NO_DATA_BN: string = 'কোন তথ্য পাওয়া যায়নি!';

  PERMISSION_MSG: string = 'you do not have this permission';
  PERMISSION_MSG_BN: string = 'আপনার এই অনুমতি নেই';


  constructor(
    private snackBar: MatSnackBar
  ) {}

  openSuccessSnackBar(message: string, action: string): void {
    this.snackBar.open(message, action, {
      duration: 5000,
      horizontalPosition: 'right',
      verticalPosition: 'bottom',
      panelClass: ['success-snackbar']
    });
  }

  openErrorSnackBar(message: string, action: string): void {
    this.snackBar.open(message, action, {
      duration: 5000,
      horizontalPosition: 'right',
      verticalPosition: 'bottom',
      panelClass: ['error-snackbar']
    });
  }

}
