package models;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Message implements Serializable {

	private static final long serialVersionUID = 1L;
	private User sender;
	private User receiver;
	private LocalDateTime time;
	private String subject;
	private String content;
	
	public Message() {}
	
	public Message(User sender, User receiver, LocalDateTime time, String subject, String content) {
		super();
		this.sender = sender;
		this.receiver = receiver;
		this.time = time;
		this.subject = subject;
		this.content = content;
	}

	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	public User getReceiver() {
		return receiver;
	}

	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}

	public LocalDateTime getTime() {
		return time;
	}

	public void setTime(LocalDateTime time) {
		this.time = time;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	
}
