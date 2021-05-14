import { Component, OnInit } from '@angular/core';
import { UserService } from '../services/user.service';
import { WsService } from '../services/ws.service';

@Component({
  selector: 'app-landing-page',
  templateUrl: './landing-page.component.html',
  styleUrls: ['./landing-page.component.css']
})
export class LandingPageComponent implements OnInit {

  liveData$ = this.wsService.messages$;

  displayedColumns: string[] = ['username'];
  registered : string[] = [];
  loggedIn : string[] = [];
  selected: string = null;
  
  constructor(private wsService : WsService, public userService : UserService) { 
    this.liveData$.subscribe({
      next : msg => this.handleMessage(msg as string)
    });
    this.wsService.connect();
    setTimeout(() => {this.userService.getLoggedIn(),this.userService.getRegistered();}, 500)
  }

  ngOnInit(): void {
    
  }
  handleMessage(message : string) {
    if(message.match('.+:.*')) {
      var type = message.split(':')[0];
      var content = message.split(':')[1];
      if(type == 'loggedInUsers') {
        this.handleLoggedInUsers(content);
      }
      else if(type == 'registeredUsers') {
        this.handleRegisteredUsers(content);
      }
    }
  }
  handleRegisteredUsers(content: string) {
    this.registered = content.split(',');
  }
  handleLoggedInUsers(content: string) {
    this.loggedIn = content.split(',');
  }

 

}
