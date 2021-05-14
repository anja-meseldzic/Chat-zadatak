import { Route } from '@angular/compiler/src/core';
import { Component, OnInit } from '@angular/core';
import { UserService } from '../services/user.service';
import { WsService } from '../services/ws.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';


@Component({
  selector: 'app-home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.css']
})
export class HomePageComponent implements OnInit {

  public username1 : string;
  public password1: string;
  public username : string;
  public password : string;

  liveData$ = this.wsService.messages$;

  constructor(private userService : UserService, private wsService: WsService, private snackBar : MatSnackBar, private router : Router) { 
    this.liveData$.subscribe({
      next : msg => this.handleMessage(msg as string)
    });
  }

  ngOnInit(): void {
  }

  login(){
    this.userService.login(this.username,this.password);
  }
  register(){
    this.userService.register(this.username1,this.password1);
  }

  handleMessage(message : string) {
    if(message.match('.+:.*')) {
      var type = message.split(':')[0];
      var content = message.split(':')[1];
      if(type == 'login')
        this.handleLogin(content);
      else if(type == 'register')
        this.handleRegistration(content);
    }
  }

  handleLogin(message : string) {
    if(message.startsWith('OK ')) {
      localStorage.setItem('id', message.substring(3));
      this.router.navigate(['home']);
    }
    else {
      this.openSnackBar(message);
    }
  }

  handleRegistration(message : string) {
    this.openSnackBar(message);
  }

  openSnackBar(message : string) {
    this.snackBar.open(message, 'Okay', {duration : 5000});
  }
}
