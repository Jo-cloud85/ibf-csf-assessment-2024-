import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MainComponent } from './views/main.component';
import { PictureComponent } from './views/picture.component';

const routes: Routes = [
  {path: '', component: MainComponent},
  {path: 'job/:id/:company', component: PictureComponent},
  {path: '**', redirectTo: '/', pathMatch: 'full'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: true})],
  exports: [RouterModule]
})
export class AppRoutingModule { }