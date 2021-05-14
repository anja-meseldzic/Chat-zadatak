import { Component, Input, OnInit, SimpleChanges, ViewChild } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MessageService } from 'src/app/services/message.service';
import { MessageDto } from '../message-dto';
import { WsService } from '../services/ws.service';

@Component({
  selector: 'app-messages',
  templateUrl: './messages.component.html',
  styleUrls: ['./messages.component.css']
})
export class MessagesComponent implements OnInit {

  constructor(private messageService : MessageService, private wsService : WsService) { }

  liveData$ = this.wsService.messages$;

  @Input() selected : string = null;

  public allMessages : MessageDto[] = [];
  public displayedMessages : MessageDto[] = [];

  public subject : string = "";
  public content : string = "";

  displayedColumns: string[] = ['date', 'message'];

  dataSource = new MatTableDataSource<MessageDto>(this.displayedMessages);

  ngOnInit(): void {
    this.liveData$.subscribe({
      next : msg => this.handleMessage(msg as string)
    });
    this.wsService.connect();
    setTimeout(() => {this.messageService.getMessages()}, 500)
  }

  ngOnChanges(changes: SimpleChanges) {
    this.setUpDisplayedMessages();
  }

  setUpDisplayedMessages() {
    this.displayedMessages = [];
    this.sortMessagesByDateTimeCreated();
    for(var message of this.allMessages) {
      if(message.otherUsername == this.selected) {
        this.displayedMessages.push(message);
      }
    }
    this.dataSource.data = this.displayedMessages;
  }

  sortMessagesByDateTimeCreated() {
    for(var message of this.allMessages) {
      var dateTimeBase = message.dateTime.toString().split('.')[0]
      var dateBase = dateTimeBase.split('T')[0]
      var timeBase = dateTimeBase.split('T')[1]
      message['date'] = new Date(Number(dateBase.split('-')[0]), Number(dateBase.split('-')[1]), Number(dateBase.split('-')[2]), Number(timeBase.split(':')[0]), Number(timeBase.split(':')[1]), Number(timeBase.split(':')[2]))
    }
    this.allMessages.sort((m1, m2) => m1['date'].getTime() - m2['date'].getTime())
  }

  filterMessagesBySelectedUser() {
    for(var message of this.allMessages) {
      if(message.otherUsername == this.selected) {
        this.displayedMessages.push(message);
      }
    }
  }

  handleMessage(message : string) {
    if(message.match('.+:.*')) {
      var type = message.split(':')[0];
      if(type == 'messageList') {
        this.handleMessageList(message);
      }
      else if(type == 'message') {
        this.handleNewMessage(message);
      }
    }
  }

  handleMessageList(message : string) {
    message = message.substring(12, message.length)
    this.allMessages = JSON.parse(message);
    this.setUpDisplayedMessages();
  }

  handleNewMessage(message : string) {
    message = message.substring(8, message.length)
    this.allMessages.push(JSON.parse(message));
    this.setUpDisplayedMessages();
  }

  send() {
    this.messageService.send(this.selected, this.subject, this.content)
    this.subject = ""
    this.content = ""
  }

  sendToAll() {
    this.messageService.sendToAll(this.subject, this.content)
    this.subject = ""
    this.content = ""
  }

}
