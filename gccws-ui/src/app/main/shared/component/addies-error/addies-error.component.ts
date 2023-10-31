import {Component, Input, OnInit} from '@angular/core';
import {FormControl, FormGroup} from '@angular/forms';

@Component({
    selector: 'addies-error',
    templateUrl: './addies-error.component.html',
    styleUrls: ['./addies-error.component.scss']
})

export class AddiesErrorComponent implements OnInit{

    @Input() formGroup: FormGroup;
    @Input() controlName: string;

    ngOnInit(): void {

    }

  get formField(): any{
    return this.formGroup.get(this.controlName);
  }


}
