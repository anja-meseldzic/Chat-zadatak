import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { UserService } from './services/user.service';

@Injectable({
  providedIn: 'root'
})
export class RouteGuardService implements CanActivate{

  constructor(private userService: UserService, private router:Router) { }
  
  canActivate(route: ActivatedRouteSnapshot): boolean {
    const onlyLoggedIn : boolean = route.data.onlyLoggedIn;    
    if (onlyLoggedIn && !this.userService.isLoggedIn()) {
      this.router.navigate(['']);
      return false;
    }
    return true;
  }
}
