import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class MessageService {

  constructor(private http : HttpClient) { }

  private baseUrl : string = 'http://localhost:8081/ChatWAR/rest/chat/messages/';

  send(receiver : string, subject : string, content : string) {
    this.http.post(this.baseUrl + "user/" + localStorage.getItem('sessionId') + "/" + receiver + "/" + subject + "/" + content, null).subscribe();
  }

  sendToAll(subject : string, content : string) {
    this.http.post(this.baseUrl + "all/" + localStorage.getItem('sessionId') + "/" + subject + "/" + content, null).subscribe();
  }

  getMessages() {
    this.http.get(this.baseUrl + localStorage.getItem('sessionId')).subscribe();
  }
}
