package chatmanager;

import java.util.List;

import javax.ejb.Remote;

import models.Message;
import models.User;

@Remote
public interface ChatManagerRemote {


	public boolean register(User user);
	
	public List<User> registeredUsers();

	public List<User> loggedInUsers();
	
	public boolean logOut(String id);
	
	public void saveMessage(Message message);
	
	public List<Message> allMessages(User user);
	
	public User getLoggedInUser(String id);
	
	public User getUserByUsername(String username);

	public boolean login(User user, String id);
	
	public List<Message> getMessages(String username);
}
