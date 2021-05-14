package chatmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.LocalBean;
import javax.ejb.Remote;
import javax.ejb.Singleton;
import javax.ejb.Stateful;
import javax.jws.soap.SOAPBinding.Use;

import models.Message;
import models.User;

// TODO Implement the rest of Client-Server functionalities 
/**
 * Session Bean implementation class ChatBean
 */
@Singleton
@LocalBean
@Remote(ChatManagerRemote.class)
public class ChatManagerBean implements ChatManagerRemote {

	private List<User> allUsers = new ArrayList<User>();
	private HashMap<String,User> loggedInUsers = new HashMap<String, User>();
	private List<Message> messages = new ArrayList<Message>();
	
	
	/**
	 * Default constructor.
	 */
	public ChatManagerBean() {
	}

	@Override
	public boolean register(User user) {
		for(User u : allUsers) {
			if(u.getUsername().equals(user.getUsername())) {
				return false;
			}
		}
		allUsers.add(user);
		return true;
	}

	@Override
	public boolean login(User user, String id) {
		boolean exists = allUsers.stream().anyMatch(u->u.getUsername().equals(user.getUsername()) && u.getPassword().equals(user.getPassword()));
		if(exists) loggedInUsers.put(id, user);
		return exists;
	}

	@Override
	public List<User> loggedInUsers() {
		return loggedInUsers.values().stream().distinct().collect(Collectors.toList());
	}

	@Override
	public List<User> registeredUsers() {
		return allUsers;
	}

	@Override
	public boolean logOut(String id) {
		if(loggedInUsers.keySet().stream().anyMatch(i -> i.equals(id))) {
			loggedInUsers.remove(id);
			return true;
		}
		else {
			return false;
		}
	}
	
	@Override
	public void saveMessage(Message message) {
		messages.add(message);
	}

	@Override
	public List<Message> allMessages(User user) {
		List<Message> userMessages = new ArrayList<Message>();
		for(Message m : messages)
			if (m.getSender().getUsername().equals(user.getUsername()) || m.getReceiver().getUsername().equals(user.getUsername()))
				userMessages.add(m);
		userMessages.sort((m1, m2) -> m1.getTime().compareTo(m2.getTime()));
		return userMessages;
	}
	
	public User getLoggedInUser(String id) {
		if(loggedInUsers.keySet().stream().anyMatch(i -> i.equals(id))) {
			return loggedInUsers.get(id);
		}
		else {
			return null;
		}
	}
	
	@Override
	public List<Message> getMessages(String username) {
		List<Message> userMessages = new ArrayList<Message>();
		for(Message m : messages)
			if (m.getSender().getUsername().equals(username) || m.getReceiver().getUsername().equals(username))
				userMessages.add(m);
		userMessages.sort((m1, m2) -> m1.getTime().compareTo(m2.getTime()));
		return userMessages;
	}
	
	@Override
	public User getUserByUsername(String username) {
		return allUsers.stream().filter(u -> u.getUsername().equals(username)).findFirst().orElse(null);
	}

	
	
}
