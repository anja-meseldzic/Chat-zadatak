import { Component } from '@angular/core';
import {MatInputModule} from '@angular/material/input';
import { UserService } from './services/user.service';
import { WsService } from './services/ws.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'angular-chat-front';

  liveData$ = this.wsService.messages$;

  constructor(private wsService : WsService, public userService : UserService) {
    this.liveData$.subscribe({
      next : msg => this.handleMessage(msg as string)
    });
  }
  
  ngOnInit(): void {
    this.wsService.connect();
  }

  handleMessage(message : string) {
    if(message.match('.+:.*')) {
      var type = message.split(':')[0];
      var content = message.split(':')[1];
      if(type == 'sessionId')
        localStorage.setItem('sessionId', content);
    }
  }
}
