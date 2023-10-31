import {AbstractControl, ValidationErrors, ValidatorFn} from "@angular/forms";
import {FileValidator} from "../../module/super-admin/model/file-validator";
import {Injectable} from "@angular/core";
import {AppUtils} from "../utils/app.utils";

@Injectable({
  providedIn: 'root'
})
export class CommonValidator{

  constructor(
    private appUtils: AppUtils
  ){}

  // -----------------------------------------------------------------------------------------------------
  // @ File Validator
  // -----------------------------------------------------------------------------------------------------

  isValidImage(file: any, fileValidator: FileValidator ): boolean{
    if (this.checkExtension(file, fileValidator)){
      if (this.checkSize(file, fileValidator)){
        return true;
      }
    }
    return false;
  }

  checkExtension(file: any, fileValidator: FileValidator ): boolean{
    let res = false;
    const ext = file.name.substring(file.name.lastIndexOf('.') + 1);
    for(const extension of fileValidator.fileExtensions){
      if(ext.toLowerCase() === extension.toLowerCase()){
        res = true;
        break;
      }
    }
    if (res){
      return true;
    }else {
      this.appUtils.showErrorMessage('File extension must be ' + fileValidator.fileExtensions, 'File extension must be ' + fileValidator.fileExtensions);
      return false;
    }
  }

  checkSize(file: any, fileValidator: FileValidator ): boolean{
    const kbValue = fileValidator.fileSize / 1024;
    if (file.size > fileValidator.fileSize){
      this.appUtils.showErrorMessage('File size not more than ' + kbValue + ' KB', 'File size not more than ' + kbValue + ' KB');
      return false;
    }else {
      return true;
    }
  }

  checkWidthHeight(file: any, fileValidator: FileValidator):Promise<boolean>{
    const reader = new FileReader();
    reader.readAsDataURL(file);
    return new Promise((resolve,reject)=>{
      reader.onload = () => {
        const img = new Image();
        img.src = reader.result as string;
        img.onload = () => {
          const imgHeight = img.naturalHeight;
          const imgWidth = img.naturalWidth;
          console.log('Width and Height', imgWidth, imgHeight);
          if(imgHeight > fileValidator.fileHeight){
            this.appUtils.showErrorMessage(
              'File height not more than ' + fileValidator.fileHeight + ' Pixels',
              'File height not more than ' + fileValidator.fileHeight + ' Pixels'
            );
            resolve(false);
          }else  if(imgWidth > fileValidator.fileWidth){
            this.appUtils.showErrorMessage(
              'File width not more than ' + fileValidator.fileWidth + ' Pixels',
              'File width not more than ' + fileValidator.fileWidth + ' Pixels',
            );
            resolve(false);
          }
          resolve(true);
        };
      };
    });
  }

  // -----------------------------------------------------------------------------------------------------
  // @ Password Validator
  // -----------------------------------------------------------------------------------------------------

  static isContainsAnySpecialChar(value: any): boolean{
      const format = /[ `!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?~]/;
      return format.test(value);
  }

  static isContainsUpper(value: any): boolean{
      const format = /[A-Z]/;
      return format.test(value);
  }

  static isContainsLower(value: any): boolean{
      const format = /[a-z]/;
      return format.test(value);
  }

  static isContainsAlpha(value: any): boolean{
      const format = /[a-zA-Z]/;
      return format.test(value);
  }

  static isContainsNumeric(value: any): boolean{
      const format = /[0-9]/;
      return format.test(value);
  }

  static isContainsSequential(s: any): boolean{
      // Check for sequential numerical characters
      for ( const i in s) {
          if (+s[+i + 1] === +s[i] + 1 &&
              +s[+i + 2] === +s[i] + 2) {return false; }
      }
      // Check for sequential alphabetical characters
      for (const i in s){
          if (String.fromCharCode(s.charCodeAt(i) + 1) === s[+i + 1] &&
              String.fromCharCode(s.charCodeAt(i) + 2) === s[+i + 2]) {return false; }
      }

      return true;
  }




  // -----------------------------------------------------------------------------------------------------
  // @ Custom Validator
  // -----------------------------------------------------------------------------------------------------

  isNegativeNumber(value: number): boolean{
    if (value < 0){
        return true;
    }
    return false;
  }


  public mustMatch(controlPath: string, matchingControlPath: string): ValidatorFn {
    return (formGroup: AbstractControl): ValidationErrors | null => {

      // Get the control and matching control
      const control = formGroup.get(controlPath);
      const matchingControl = formGroup.get(matchingControlPath);

      // Return if control or matching control doesn't exist
      if ( !control || !matchingControl )
      {
        return null;
      }

      // Delete the mustMatch error to reset the error on the matching control
      if ( matchingControl.hasError('mustMatch') )
      {
        // @ts-ignore
        delete matchingControl.errors.mustMatch;
        matchingControl.updateValueAndValidity();
      }

      // Don't validate empty values on the matching control
      // Don't validate if values are matching
      if ( this.isEmptyInputValue(matchingControl.value) || control.value === matchingControl.value )
      {
        return null;
      }

      // Prepare the validation errors
      const errors = {mustMatch: true};

      // Set the validation error on the matching control
      matchingControl.setErrors(errors);

      // Return the errors
      return errors;
    };
  }

  isEmptyInputValue(value: any): boolean {
    return value == null || value.length === 0;
  }



}

