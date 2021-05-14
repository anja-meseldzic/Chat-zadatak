package agents;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateful;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import chatmanager.ChatManagerRemote;
import messagemanager.AgentMessage;
import messagemanager.MessageOptions;
import models.User;
import ws.WSEndPoint;

/**
 * Sledece nedelje cemo prebaciti poruke koje krajnji korisnik treba da vidi da
 * se salju preko Web Socketa na front-end (klijentski deo aplikacije)
 * 
 * @author Aleksandra
 *
 */
@Stateful
@Remote(Agent.class)
public class UserAgent implements Agent {


	private static final long serialVersionUID = 1L;
	private String agentId;

	@EJB
	private ChatManagerRemote chatManager;

	@EJB
	WSEndPoint ws;

	@Override
	public void init(String id) {
		this.agentId = id;
	}

	@Override
	public void handleMessage(AgentMessage msg) {
		MessageOptions option = msg.getOptions();

		switch (option) {

			case REGISTER: {
				String username = msg.getAttributes().get("username");
				String password = msg.getAttributes().get("password");
				register(username, password);
	
				break;
			}
			case LOG_IN: {
				String username = msg.getAttributes().get("username");
				String password = msg.getAttributes().get("password");
				login(username, password);
	
				break;
			}
			case GET_ALL:{
				getRegistered();
				break;
			}
			case GET_ALL_LOGGED_IN:{
				getAllLoggedIn();
				break;
			}
			case LOG_OUT:{
				String id  = msg.getAttributes().get("id");
				logOut(id);
				break;
			}
			case SEND_ALL:{
				String subject = msg.getAttributes().get("subject");
				String content = msg.getAttributes().get("content");
				sendToEveryone(subject,content);
				break;
			}
			case SEND_USER:{
				String receiver = msg.getAttributes().get("receiver");
				String subject = msg.getAttributes().get("subject");
				String content = msg.getAttributes().get("content");
				sendMessage(receiver, subject, content);
				break;
			}
			case GET_ALL_MESSAGES:{
				getAllMessages();
				break;
			}
		}
	}

	

	@Override
	public String getAgentId() {
		return agentId;
	}

	private void register(String username, String password) {
		if (chatManager.register(new User(username, password))) {
			ws.send(agentId, "register:You have successfully registered!");
			ws.sendToAllLoggedIn(createAllRegisteredMessage(chatManager.registeredUsers()));
		} else {
			ws.send(agentId, "register:The username has been taken.");
		}
	}

	private void login(String username, String password) {

		String currentUsername = ws.getUsernameBoundToSession(agentId);
		if (currentUsername == null) {
			String id = UUID.randomUUID().toString();
			if (chatManager.login(new User(username, password), id)) {
				ws.bindUsernameToSession(username, agentId);
				ws.send(agentId, "login:OK " + id);
				ws.sendToAllLoggedIn(createAllLoggedInMessage(chatManager.loggedInUsers()));
			} else {
				ws.send(agentId, "login:Password or username are incorrect.");
			}
		} else {
			ws.send(agentId, "login:You are already logged in.");
		}

	}
	
	private void getRegistered(){
		if(ws.getUsernameBoundToSession(agentId) != null) {
			ws.send(agentId, createAllRegisteredMessage(chatManager.registeredUsers()));
		}
	}
	
	private void getAllLoggedIn() {
		if(ws.getUsernameBoundToSession(agentId) != null) {
			ws.send(agentId,createAllLoggedInMessage(chatManager.loggedInUsers()));
		}
	}
	
	private void logOut(String id) {
		User user = chatManager.getLoggedInUser(id);
		if(user != null && user.getUsername().equals(ws.getUsernameBoundToSession(agentId)) && chatManager.logOut(id)) {
			ws.unbindUsernameFromSession(agentId);
			ws.send(agentId, "logout:OK");
			ws.sendToAllLoggedIn(createAllLoggedInMessage(chatManager.loggedInUsers()));
		} else {
			ws.send(agentId, "logout:Logging out faild!");
		}
	}
	
	private void sendMessage(String receiver, String subject, String content) {
		String username = ws.getUsernameBoundToSession(agentId);
		if(chatManager.getUserByUsername(receiver) != null && username != null) {
			User receiver1 = chatManager.getUserByUsername(receiver);
			User sender = chatManager.getUserByUsername(username);
			models.Message message = new models.Message(sender, receiver1, LocalDateTime.now(), subject, content);
			chatManager.saveMessage(message);
			ws.sendToOneLoggedIn(receiver, getMessageTextMessage(message));
			ws.send(agentId, getMessageListTextMessage(chatManager.getMessages(username)));
		}
	}
	
	private void sendToEveryone(String subject, String content) {
		List<User> registered = chatManager.registeredUsers();
		for(User user : registered)
			sendMessage(user.getUsername(), subject, content);
	}
	
	private void getAllMessages() {
		if(ws.getUsernameBoundToSession(agentId) != null)
			ws.send(agentId, getMessageListTextMessage(chatManager.getMessages(ws.getUsernameBoundToSession(agentId))));
	}
	
	private String createAllRegisteredMessage(List<User> registered) {
		String registeredUsers = "";
		registeredUsers  =  "registeredUsers:";
		
		for (User u : registered) {
			registeredUsers = registeredUsers + u.getUsername();
			registeredUsers = registeredUsers + ",";
		}
		return registeredUsers.substring(0,registeredUsers.length()-1);
	}
	
	private String createAllLoggedInMessage(List<User> loggedIn) {
		String loggedInUsers = "";
		loggedInUsers  =  "loggedInUsers:";
		
		for (User u : loggedIn) {
			loggedInUsers = loggedInUsers + u.getUsername();
			loggedInUsers = loggedInUsers + ",";
		}
		return loggedInUsers.substring(0, loggedInUsers.length()-1);
	}
	
	private String getMessageListTextMessage(List<models.Message> messages) {
		StringBuilder messageList = new StringBuilder();
		messageList.append("messageList:");
		messageList.append("[");
		for(models.Message m : messages) {
			String otherUsername = m.getSender().getUsername();
			boolean incoming = true;
			if(ws.getUsernameBoundToSession(agentId).equals(otherUsername)) {
				otherUsername = m.getReceiver().getUsername();
				incoming = false;
			}
			messageList.append("{");
			messageList.append("\"otherUsername\":\"");
			messageList.append(otherUsername);
			messageList.append("\", \"incoming\":\"");
			messageList.append(incoming);
			messageList.append("\", \"subject\":\"");
			messageList.append(m.getSubject());
			messageList.append("\", \"content\":\"");
			messageList.append(m.getContent());
			messageList.append("\", \"dateTime\":\"");
			messageList.append(m.getTime());
			messageList.append("\" }, ");
		}
		if(messageList.length() > 13)
			messageList.deleteCharAt(messageList.lastIndexOf(","));
		messageList.append("]");
		return messageList.toString();
	}
	
	private String getMessageTextMessage(models.Message message) {
		StringBuilder messageText = new StringBuilder();
		messageText.append("message:");
		messageText.append("{");
		messageText.append("\"otherUsername\":\"");
		messageText.append(message.getSender().getUsername());
		messageText.append("\", \"incoming\":\"");
		messageText.append(true);
		messageText.append("\", \"subject\":\"");
		messageText.append(message.getSubject());
		messageText.append("\", \"content\":\"");
		messageText.append(message.getContent());
		messageText.append("\", \"dateTime\":\"");
		messageText.append(message.getTime());
		messageText.append("\" }");
		return messageText.toString();
	}
	
}
