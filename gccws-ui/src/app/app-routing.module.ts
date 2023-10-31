import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {UserLoginComponent} from "./main/module/super-admin/page/user-login/user-login.component";
import {ResetPasswordComponent} from "./main/module/super-admin/page/reset-password/reset-password.component";


const routes: Routes = [
  { path: '', redirectTo: 'sign-in', pathMatch: 'full' },
  { path: 'sign-in', component: UserLoginComponent },
  { path: 'reset-password', component: ResetPasswordComponent },
  { path: 'signed-in-redirect', pathMatch : 'full', redirectTo: 'dashboard-home' },

];

@NgModule({
  imports: [
    RouterModule.forRoot(routes, {
      scrollPositionRestoration: 'top',
      anchorScrolling: 'enabled',
      initialNavigation: 'enabledBlocking'
      // relativeLinkResolution: 'legacy'
    })
  ],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
