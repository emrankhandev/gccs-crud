import {Component, EventEmitter, Inject, OnInit} from '@angular/core';
import {locale as lngEnglish} from './i18n/en';
import {locale as lngBangla} from './i18n/bn';
import {MAT_DIALOG_DATA} from '@angular/material/dialog';

import {TranslationLoaderService} from "../../../core/service/translation-loader.service";

@Component({
    selector: 'app-confirmation-dialog',
    templateUrl: './confirmation-dialog.component.html',
    styleUrls: ['./confirmation-dialog.component.scss']
})
export class ConfirmationDialogComponent implements OnInit {

    title;
    message;
    closeEventEmitter: EventEmitter<boolean> = new EventEmitter<boolean>();

    constructor(
      @Inject(MAT_DIALOG_DATA) data: any,
      private _fuseTranslationLoaderService: TranslationLoaderService
    ) {
        this.title = data.title;
        this.message = data.message;
        this._fuseTranslationLoaderService.loadTranslations(lngEnglish, lngBangla);
    }

    ngOnInit(): void {
    }

    confirm(value: boolean): void {
        this.closeEventEmitter.emit(value);
    }
}
