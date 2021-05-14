import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http : HttpClient) { }

  register(username, password){
    var data = new Object();
    data['username'] = username;
    data['password'] = password;
    this.http.post('http://localhost:8081/ChatWAR/rest/chat/users/register/' + localStorage.getItem('sessionId'), data, {headers : new HttpHeaders({ 'Content-Type': 'application/json' })}).subscribe();
  }

  isLoggedIn(){
    return localStorage.getItem('id') != null;
  }
  
  login(username, password){
    var data = new Object();
    data['username'] = username;
    data['password'] = password;
    this.http.post('http://localhost:8081/ChatWAR/rest/chat/users/logIn/' + localStorage.getItem('sessionId'), data).subscribe();
  }

  getRegistered(){
    this.http.get('http://localhost:8081/ChatWAR/rest/chat/users/registered/' + localStorage.getItem('sessionId')).subscribe();
  }

  getLoggedIn(){
    this.http.get('http://localhost:8081/ChatWAR/rest/chat/users/loggedIn/' + localStorage.getItem('sessionId')).subscribe();
  }
  logOut(){
    this.http.delete('http://localhost:8081/ChatWAR/rest/chat/users/loggedIn/' + localStorage.getItem('sessionId') + "/" + localStorage.getItem('id')).subscribe();
    localStorage.removeItem('id');
  }
}
