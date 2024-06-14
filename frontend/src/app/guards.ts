import { ActivatedRouteSnapshot, CanActivateFn, CanDeactivateFn, Router, RouterStateSnapshot } from "@angular/router";
import { PictureComponent } from "./views/picture.component";
import { Observable } from "rxjs";
import { inject } from "@angular/core";

export const leavePost: CanDeactivateFn<PictureComponent> =
  (comp: PictureComponent, route: ActivatedRouteSnapshot, state: RouterStateSnapshot):
    boolean | Promise<boolean> | Observable<boolean> => {

    const router = inject(Router);

    if (!comp.isFormDirty()) {
      return confirm('Are you sure you want to discard image?');
    }
    return true;
}