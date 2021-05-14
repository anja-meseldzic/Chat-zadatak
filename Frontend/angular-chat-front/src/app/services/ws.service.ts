import { Injectable } from '@angular/core';
import { webSocket, WebSocketSubject } from 'rxjs/webSocket';
import { catchError, tap, switchAll, share } from 'rxjs/operators';
import { EMPTY, Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class WsService {


  private identifier : string = localStorage.getItem('id') == null ? "" : localStorage.getItem('id');
  private WS_ENDPOINT : string = 'ws://localhost:8081/ChatWAR/ws/' + this.identifier;

  private socket$: WebSocketSubject<any>;
  private messagesSubject$ = new Subject();
  public messages$ = this.messagesSubject$.pipe(switchAll(), share(), catchError(e => { throw e }));
  
  public connect(): void {
    if (!this.socket$ || this.socket$.closed) {
      this.socket$ = this.getNewWebSocket();
      const messages = this.socket$.pipe(
        tap({
          error: error => console.log(error),
        }), catchError(_ => EMPTY));
      this.messagesSubject$.next(messages);
      this.socket$.subscribe({
        complete: () => { localStorage.removeItem('sessionId') }
      })
    }
  }
  
  private getNewWebSocket() {
    return webSocket({url: this.WS_ENDPOINT, deserializer: msg => msg.data});
  }

  sendMessage(msg: any) {
    this.socket$.next(msg);
  }

  close() {
    this.socket$.complete();
    localStorage.removeItem('sessionId');
  }
  constructor() { }
}
